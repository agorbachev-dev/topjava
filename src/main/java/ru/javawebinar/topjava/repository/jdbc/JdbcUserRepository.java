package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    private final Validator validator;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate, Validator validator) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.validator = validator;
    }

    @Override
    @Transactional
    public User save(User user) {
        validate(user);
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
            updateRoles(user);
        } else {
            int count = namedParameterJdbcTemplate.update(
                    "UPDATE users SET name=:name, email=:email, password=:password, " +
                            "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource);

            int[] batchUpdateCount = updateRoles(user);
            if (count == 0 || batchUpdateCount.length == 0) {
                return null;
            }
        }
        return user;
    }

    private void validate(User user) {
        Set<ConstraintViolation<User>> validate = validator.validate(user);
        if (validate.size() > 0) {
            throw new ConstraintViolationException(validate);
        }
    }

    @Override
    @Transactional
    public boolean delete(@DecimalMin("100000") int id) {
        deleteRoles(id);
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public @Size(min = 1, max = 1) User get(@DecimalMin("100000") int id) {
        List<User> users = jdbcTemplate.query("SELECT u.*, string_agg(role, ',') as roles\n" +
                "FROM users u\n" +
                "         LEFT JOIN user_roles ur on u.id = ur.user_id\n" +
                "WHERE id=?\n" +
                "GROUP BY id, name, email", ROW_MAPPER, id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public @Size(min = 1, max = 1) User getByEmail(@Email String email) {
        List<User> users = jdbcTemplate.query("SELECT u.*, role FROM users u LEFT JOIN user_roles ur on u.id = ur.user_id WHERE email=?",
                new Object[]{email},
                rs -> {
                    List<User> rsUsers = new ArrayList<>();
                    Set<Role> roles = new HashSet<>();
                    User currUser = new User();
                    while (rs.next()) {
                        currUser.setId(rs.getInt("id"));
                        currUser.setName(rs.getString("name"));
                        currUser.setEmail(rs.getString("email"));
                        currUser.setPassword(rs.getString("password"));
                        currUser.setRegistered(rs.getDate("registered"));
                        currUser.setEnabled(rs.getBoolean("enabled"));
                        currUser.setCaloriesPerDay(rs.getInt("calories_per_day"));
                        roles.add(Role.valueOf(rs.getString("role")));
                    }
                    currUser.setRoles(roles);
                    rsUsers.add(currUser);
                    return rsUsers;
                });
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT u.*, string_agg(role, ',') as roles\n" +
                "FROM users u\n" +
                "         LEFT JOIN user_roles ur on u.id = ur.user_id\n" +
                "GROUP BY id, name, email\n" +
                "ORDER BY name, email", ROW_MAPPER);
    }

    @Transactional
    public int[] updateRoles(@NotNull User user) {
        validate(user);
        deleteRoles(user.getId());
        List<Role> roles = new ArrayList<>(user.getRoles());
        return jdbcTemplate.batchUpdate("INSERT into user_roles (user_id, role)  values  (?,?)",
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, user.getId());
                        ps.setString(2, roles.get(i).name());
                    }

                    public int getBatchSize() {
                        return roles.size();
                    }
                });
    }

    @Transactional
    public int deleteRoles(@DecimalMin("100000") int id) {
        return jdbcTemplate.update("DELETE FROM user_roles where user_id = ?", id);
    }
}

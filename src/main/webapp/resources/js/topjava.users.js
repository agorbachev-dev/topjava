// $(document).ready(function () {
$(function () {
    makeEditable({
            ajaxUrl: "admin/users/",
            datatableApi: $("#usersTable").DataTable({
                "paging": false,
                "info": true,
                "columns": [
                    {
                        "data": "name"
                    },
                    {
                        "data": "email"
                    },
                    {
                        "data": "roles"
                    },
                    {
                        "data": "enabled"
                    },
                    {
                        "data": "registered"
                    },
                    {
                        "defaultContent": "Edit",
                        "orderable": false
                    },
                    {
                        "defaultContent": "Delete",
                        "orderable": false
                    }
                ],
                "order": [
                    [
                        0,
                        "asc"
                    ]
                ]
            })
        }
    );
});

function changeUserActive(id, enabled) {
    $.ajax({
        url: context.ajaxUrl + "activity/" + id,
        type: "POST",
        data: 'enabled=' + !enabled
    }).done(function () {
        updateTable();
        location.reload();
    })
}
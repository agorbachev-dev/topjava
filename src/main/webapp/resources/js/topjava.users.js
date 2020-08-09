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

function changeUserActive(id) {
    let checked = $(this).className.split(/\s+/);
    let element = $(this);
    $.ajax({
        url: context.ajaxUrl + "activity/" + id,
        type: "POST",
        data: 'enabled=' + checked
    }).done(function () {
        if (checked) {
            element.parent().parent().removeClass("userDisabled");
            element.parent().parent().addClass("userEnabled");
        } else {
            element.parent().parent().addClass("userDisabled");
            element.parent().parent().removeClass("userEnabled");
        }
        updateTable();
    })
}

function changeClass(elem) {

}
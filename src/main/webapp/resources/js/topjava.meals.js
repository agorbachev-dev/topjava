$(function () {
    makeEditable({
        ajaxUrl: "ajax/meals/",
        datatableApi: $("#mealsTable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime"
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
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
                    "desc"
                ]
            ]
        })
    });
});

function filter() {
    $("#filterForm").data() = JSON.stringify($("#filterForm").serializeArray());
    $.ajax({
        type: "POST",
        url: context.ajaxUrl + "filter",
        data: $("#filterForm").serialize(),
    }).done(function (data) {
        context.datatableApi.clear().rows.add(data).draw();
        return false;
    });
}

function clearFilter() {
    filterparams = null;
    $("#filterForm")[0].reset();
    updateTable();
    return false;
}

function filterOnReload() {
    if (filterparams != null) {
        populate("#filterForm", $.parseJSON(filterparams));
    }
    return false;
}

function populate(frm, data) {
    $.each(data, function (key, value) {
        $('[name=' + key + ']', frm).val(value);
    });
}

$(window).on("load", function () {
    filterOnReload()
});
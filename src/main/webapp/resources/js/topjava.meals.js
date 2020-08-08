let ctx = {
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
};

function filter() {
    let form = $("#filterForm");
    localStorage.setItem("mealFilter", JSON.stringify(form.serializeArray()));
    $.ajax({
        type: "POST",
        url: context.ajaxUrl + "filter",
        data: form.serialize(),
    }).done(function (data) {
        context.datatableApi.clear().rows.add(data).draw();
        return false;
    });
}

function clearFilter() {
    localStorage.setItem("mealFilter","");
    $("#filterForm")[0].reset();
    updateTable();
    return false;
}

function filterOnReload() {
    if (localStorage.getItem("mealFilter").length > 2){
        populateForm();
        filter();
    }
    return false;
}

$(window).on("load", function () {
    makeEditable(ctx);
    filterOnReload();
});

const populateForm = () => {
    let formElementsArray = $("#filterForm")[0].elements;
    let json = localStorage.getItem("mealFilter");
    if (json.length > 2) {
        const savedData = JSON.parse(json); // get and parse the saved data from localStorage
        for (let i = 0; i < savedData.length; i++) {
            formElementsArray[savedData[i].name].value = savedData[i].value;
        }
    }
};
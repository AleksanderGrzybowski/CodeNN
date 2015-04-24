$('#send').click(function () {
    console.log("Click");
    $.getJSON("/CodeNN/main/ask", {
        format: "json",
        snippet: $('#snippet').val()
    }).done(function (data) {
        console.log("Got it! From server:");
        console.log(data);
        if (data.success == true) {
            console.log("Displaying chart");
            delete data.success;
            displayChart(data);
        } else {
            console.log("Displaying error");
            displayError(data.message);
        }
    }).fail(function (jqxhr, textStatus, error) {
        displayError("An unknown error: " + textStatus + " " + error)
    })

});

function displayError(errorString) {
    $('#chart').remove();
    var newDiv = $('<div></div>');
    newDiv.text("There was an error: " + errorString);
    newDiv.addClass("bg-danger")
    newDiv.attr('id', 'chart');
    $('#forchart').append(newDiv);
}

function displayChart(data) {
    $('#chart').remove();
    var newDiv = $('<div></div>');
    newDiv.attr('id', 'chart');
    $('#forchart').append(newDiv);


    var dataArray = [];
    for (key in data) {
        dataArray.push([key, data[key]])
    }
    console.log(dataArray);

    $('#chart').highcharts({
        chart: {
            type: 'pie',
            options3d: {
                enabled: true,
                alpha: 45,
                beta: 0
            }
        },
        title: {
            text: 'Browser market shares at a specific website, 2014'
        },
        plotOptions: {
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                depth: 35
            }
        },
        series: [{
            type: 'pie',
            name: 'Browser share',
            data: dataArray
        }]
    });

}



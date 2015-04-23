$('#send').click(function () {
    console.log("Click");
    $.getJSON("/CodeNN/main/ask", {
        format: "json",
        snippet: $('#snippet').val()
    }).done(function (data) {
        console.log("Got it! From server:");
        console.log(JSON.stringify(data));
        $('#response').text(JSON.stringify(data));
        displayChart(data);
    }).fail(function () {
        alert("Error. WTF?")
    })

});

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



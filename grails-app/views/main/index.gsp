<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>CodeNN</title>
</head>

<body>

<div class="row">
    <div class="col-md-4 col-md-push-1">
        <h1 class="text-center">CodeNN</h1>
        <h5 class="text-center">A neural network code recognition system</h5>

        <div class="form-group">

            <textarea id="snippet" rows="10" class="form-control" style="margin-bottom: 20px;"></textarea>

            <div class="text-center">
                <button id="send" class="btn btn-primary">Send</button>
            </div>
        </div>

    </div>

    <div class="col-md-4 col-md-push-1" id="forchart">
        <p id="response" class="bg-success">Here be response</p>
    </div>
</div>


<div id="container"></div>

</body>


<r:script>
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







</r:script>

</html>
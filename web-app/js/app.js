var premadeSnippets = {
    c: 'int main(void)\n\tprintf("Hello world");\n\treturn 0;\n}',
    bash: '# set timezone\nif [ -z "$TZ" -a -e /etc/timezone ]; then\n\tTZ=`cat /etc/timezone`\n\texport TZ\nfi\n',
    java: 'public class HelloWorld {\n\tpublic static void main(String[] args)\n\t\tSystem.out.println("Hello world");\n\t\treturn 0;\n\t}\n}'
};

for (var lang in premadeSnippets) {
    $("#" + lang).click(function (l) {
            return function () { // i can't express enough how i hate JS
                $('#snippet').text(premadeSnippets[l])
            }
        }(lang)
    )
}

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

$("textarea").keydown(function (e) {
    if (e.keyCode === 9) { // tab was pressed
        // get caret position/selection
        var start = this.selectionStart;
        var end = this.selectionEnd;

        var $this = $(this);
        var value = $this.val();

        // set textarea value to: text before caret + tab + text after caret
        $this.val(value.substring(0, start)
            + "\t"
            + value.substring(end));

        // put caret at right position again (add one for the tab)
        this.selectionStart = this.selectionEnd = start + 1;

        // prevent the focus lose
        e.preventDefault();
    }
});

function displayError(errorString) {
    $('#chart').remove();
    var newDiv = $('<div></div>');
    newDiv.text("There was an error: " + errorString);
    newDiv.addClass("bg-danger");
    newDiv.attr('id', 'chart');
    $('#forchart').append(newDiv);
}

function displayChart(data) {
    $('#chart').remove();
    var newDiv = $('<div></div>');
    newDiv.attr('id', 'chart');
    $('#forchart').append(newDiv);

    // convert object to two arrays [keys] and [values]

    var langs = [];
    var stats = [];

    for (var property in data) {
        if (data.hasOwnProperty(property)) {
            langs.push(property);
            stats.push(data[property])
        }
    }

    console.log(langs);
    console.log(stats);


    $('#chart').highcharts({
        chart: {
            type: 'bar'
        },
        title: {
            text: 'Results'
        },
        xAxis: {
            categories: langs,
            title: {
                text: null
            }
        },
        yAxis: {
            min: 0,

            labels: {
                overflow: 'justify'
            }
        },
        plotOptions: {
            bar: {
                dataLabels: {
                    enabled: false
                }
            }
        },
        legend: {
            layout: 'vertical',
            align: 'right',
            verticalAlign: 'top',
            x: -40,
            y: 100,
            floating: true,
            borderWidth: 1,
            backgroundColor: ((Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'),
            shadow: true
        },
        credits: {
            enabled: false
        },

        series: [{
            name: 'Probability',
            data: stats
        }]
    });
}
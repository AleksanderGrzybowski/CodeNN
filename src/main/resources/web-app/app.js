$(function () {

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
        $.getJSON("/ask", {
            format: "json",
            snippet: $('#snippet').val()
        }).done(function (data) {
            console.log("Got it! From server:");
            console.log(data);
            displayChart(data);
        }).fail(function (jqxhr, textStatus, error) {
            alert("An unknown error: " + textStatus + " " + error)
        });
    });

    // hack for working tabs in textarea from stack (?)
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

    function displayChart(data) {
        var $chart = $('#chart');
        $chart.empty();

        // convert object to two arrays [keys] and [values]
        // highcharts needs that

        var langs = [];
        var stats = [];

        for (var property in data) {
            if (data.hasOwnProperty(property)) {
                langs.push(property);
                stats.push(data[property])
            }
        }

        $chart.highcharts({
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
                backgroundColor: '#FFFFFF',
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
});
import $ from 'jquery';
import highcharts from 'highcharts';
import 'bootstrap/dist/css/bootstrap.css';

function setupSnippetButtons() {
    const premadeSnippets = {
        c: 'int main(void)\n\tprintf("Hello world");\n\treturn 0;\n}',
        bash: '# set timezone\nif [ -z "$TZ" -a -e /etc/timezone ]; then\n\tTZ=`cat /etc/timezone`\n\texport TZ\nfi\n',
        java: 'public class HelloWorld {\n\tpublic static void main(String[] args)\n\t\tSystem.out.println("Hello world");\n\t\treturn 0;\n\t}\n}'
    };

    for (const lang in premadeSnippets) {
        $("#" + lang).click(() => $('#snippet').text(premadeSnippets[lang]));
    }
}

function enableTabsInTextarea() {
    $("textarea").keydown(function (e) {
        if (e.keyCode === 9) { // tab was pressed
            // get caret position/selection
            const start = this.selectionStart;
            const end = this.selectionEnd;

            const $this = $(this);
            const value = $this.val();

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
}

function setupSendButton() {
    $('#send').click(() => {
        $.getJSON("/ask", {
            format: "json",
            snippet: $('#snippet').val()
        }).done(data => displayChart(data))
          .fail((jqxhr, textStatus, error) => alert("An unknown error: " + textStatus + " " + error));
    });
}

function displayChart(data) {
    $('#welcome-message').remove();
    $('#chart').empty();

    // convert object to two arrays [keys] and [values]
    // highcharts needs that

    const langs = [];
    const stats = [];

    for (const property in data) {
        if (data.hasOwnProperty(property)) {
            langs.push(property);
            stats.push(data[property])
        }
    }

    highcharts.chart('chart', {
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

$(() => {
    setupSnippetButtons();
    setupSendButton();
    enableTabsInTextarea();
});
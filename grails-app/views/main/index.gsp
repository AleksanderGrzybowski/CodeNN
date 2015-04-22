<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>CodeNN</title>
</head>

<body>

<h1>CodeNN</h1>
<textarea id="snippet" cols="100" rows="5"></textarea>
<button id="send" class="btn btn-primary">Wyślij</button>
<p id="response" class="bg-success">Here be response</p>

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
            $('#response').text(JSON.stringify(data))
        }).fail(function() {
            alert("Error. WTF?")
        })

    })

</r:script>

</html>
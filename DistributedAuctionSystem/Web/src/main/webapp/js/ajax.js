

$(document).ready(function(){
    console.log("setting submission ajax handler");
    $("#bid_form").on("submit", function(event){
        event.preventDefault();
        let result = document.getElementById("result");
        if(result.hasChildNodes()){
            for (let child of $("#result").childNodes)
                result.removeChild(child);
        }
        result.appendChild(document.createTextNode("Submitting your bid..."))
        let formValues= $(this).serialize();

        $.post("/web/bid", formValues, function(data){
            for(let child of result.childNodes)
                result.removeChild(child);
            result.appendChild(document.createTextNode(`Result: ${data}`));
        });
    });
    console.log("submission ajax handler set succesfully")
});



$("#bid_form").submit(function(e) {

    e.preventDefault(); // avoid to execute the actual submit of the form.

    var form = $(this);
    var url = form.attr('action');
    console.log(form);

    $.ajax({
        type: "POST",
        url: url,
        data: form.serialize(), // serializes the form's elements.
        success: function(data)
        {
            alert(data); // show response from the php script.
        }
    });


});
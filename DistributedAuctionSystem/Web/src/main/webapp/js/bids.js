

$(document).ready(function(){
    console.log("setting submission ajax handler");
    $("#bid_form").on("submit", function(event){
        event.preventDefault();
        let result = document.getElementById("result");
        if(result.hasChildNodes()){
            for (let child of result.childNodes)
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
    console.log("setting delete bid ajax handler")
    $(".delete-bid-button").on("click", function(event){
        event.preventDefault();
        let result = document.getElementById("result");
        if(result.hasChildNodes()){
            for (let child of result.childNodes)
                result.removeChild(child);
        }
        result.appendChild(document.createTextNode("Sending your delete bid request..."))
        let params = {
            auctionID: document.getElementById("auction_id").value,
            bidID: bid_id = $(this).closest("tr").attr('id')
        };
        $.post("/web/deleteBid", params,
            function(data){
                console.log(data);
                if(result.hasChildNodes()){
                    for (let child of result.childNodes)
                        result.removeChild(child);
                }
                result.appendChild(document.createTextNode(`Result: ${data}`));
                document.getElementById(bid_id).remove();
        });
    });
    console.log("delete bid ajax handler set succesfully")
});

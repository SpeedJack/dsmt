

$(document).ready(registerHandlers);

function registerHandlers() {
    console.log("setting submission ajax handler");
    $("#bid_form").on("submit", makeBidHandler);
    console.log("submission ajax handler set succesfully")
    console.log("setting delete bid ajax handler")
    $(".delete-bid-button").on("click", deleteBidHandler);
    console.log("delete bid ajax handler set succesfully")
}

function makeBidHandler(event){
    event.preventDefault();
    /*
    let result = document.getElementById("result");
    if(result.hasChildNodes()){
        for (let child of result.childNodes)
            result.removeChild(child);
    }*/
    let data = new FormData(event.target);
    console.log(data);
    data = Object.fromEntries(data.entries());
    //result.appendChild(document.createTextNode("Submitting your bid..."))
    data.action = "make";
    $("#offers_bid_table_container").load("/web/bid", data, makeBidDoneHandler);
}

function makeBidDoneHandler(data){
   /* let result = document.getElementById("result");
    for(let child of result.childNodes)
        result.removeChild(child);

    result.appendChild(document.createTextNode(`Result: RECEIVED!`));
    */
    registerHandlers();
}

function deleteBidHandler(event){
    event.preventDefault();
    //let result = document.getElementById("result");
    /*if(result.hasChildNodes()){
        for (let child of result.childNodes)
            result.removeChild(child);
    }
    result.appendChild(document.createTextNode("Sending your delete bid request..."))
    */let params = {
        auctionID: document.getElementById("auction_id").value,
        bidID: bid_id = $(this).closest("tr").attr('id'),
        action: "delete"
    };
    $("#offers_bid_table_container").load("/web/bid", params, deleteBidDoneHandler);
}

function deleteBidDoneHandler(data){
    /*let result = document.getElementById("result");
    if(result.hasChildNodes()){
        for (let child of result.childNodes)
            result.removeChild(child);
    }

    result.appendChild(document.createTextNode(`Result: RECEIVED!`));
     */
    registerHandlers();
}
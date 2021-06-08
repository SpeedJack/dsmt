

$(document).ready(registerHandlers);

function registerHandlers() {
    console.log("setting submission ajax handler");
    $("#bid_form").off("submit").on("submit", makeBidHandler);
    console.log("submission ajax handler set succesfully")
    console.log("setting delete bid ajax handler")
    $('.delete-bid-button').off('click').on("click", deleteBidHandler);
    console.log("delete bid ajax handler set succesfully")
}

function makeBidHandler(event){
    event.preventDefault();

    let data = new FormData(event.target);
    console.log(data);
    data = Object.fromEntries(data.entries());
    data.action = "make";
    $("#offers_bid_table_container").load("/web/bid", data, registerHandlers);
}


function deleteBidHandler(event){
    event.preventDefault();

    let params = {
        auctionID: document.getElementById("auction_id").value,
        bidID: bid_id = $(this).closest("tr").attr('id'),
        action: "delete"
    };

    $("#offers_bid_table_container").load("/web/bid", params, registerHandlers);
}

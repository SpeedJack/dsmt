let socket = null;
//$(document).ready(registerHandlers);

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
    data = Object.fromEntries(data.entries());
    data.action = "make";
    $.post("/web/bid", data)
        .done(() => {
                $("#details").load(`/web/auction?action=detail&auctionID=${get_auction_id()} #details`, registerHandlers);
        })
        .fail( () =>  {
            console.log("ERROR!");
            $("#errorMessage").text("Your bid is not valid!");
        });
    //$("#details").load(`/web/auction?action=detail&auctionID=${get_auction_id()} #details`, registerHandlers);
    //$("#offers_bid_table_container").load("/web/bid", data) //, registerHandlers);
}


function deleteBidHandler(event){
    event.preventDefault();
    let params = {
        auctionID: document.getElementById("auction_id").value,
        bidID: $(this).closest("tr").attr('id'),
        action: "delete"
    };
    $.post("/web/bid", params)
        .done(() => {
                $("#details").load(`/web/auction?action=detail&auctionID=${get_auction_id()} #details`, registerHandlers);
        })
        .fail( () => $("#errorMessage").text("Your bid is not valid!"));
    //$("#details").load(`/web/auction?action=detail&auctionID=${get_auction_id()} #details`, registerHandlers);
    //$("#offers_bid_table_container").load("/web/bid", params) //, registerHandlers);
}

window.addEventListener('load', (event) => {
    registerHandlers();
    console.log('opening socket');
    init_socket(socket,
        (event) => {
            console.log(`[received message]: ${event.data}`);
            $("#details").load(`/web/auction?action=detail&auctionID=${get_auction_id()} #details`, registerHandlers);

            //if(event.data === "CLOSE")
              //  $("#details").load(`/web/auction?action=detail&update=true&auctionID=${get_auction_id()}`)
            //else
            //    $("#details")
            //    .load(`/web/auction?action=detail&update=true&auctionID=${get_auction_id()} #details`);
        })
});


function get_auction_id(){
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    const auctionID = urlParams.get("auctionID");
    return auctionID;
}


function init_socket(socket, onmessage) {
    let auction = get_auction_id();
    console.log(auction);

    socket = new WebSocket(`ws://localhost:8080/ws/auction/${auction}`);
    socket.onopen = function(e) {
        console.log(`[open] Connection established`);
    };

    socket.onmessage = onmessage;

    socket.onclose = function(event) {
        if (event.wasClean) {
            console.log(`[close] Connection closed cleanly, code=${event.code} reason=${event.reason}`);
        } else {
            // e.g. server process killed or network down
            // event.code is usually 1006 in this case
            console.log('[close] Connection died');
        }
    };

    socket.onerror = function(error) {
        console.log(`[error] ${error.message}`);
    };

}
let socket = null;

window.addEventListener('load', (event) => {
    console.log('opening socket');
    init_socket(socket,
        (event) => {
            console.log(`[received message]: ${event.data}`);
            if(event.data === "CLOSE")
                $("html").load(`/web/auction?action=detail&auctionID=${get_auction_id()}`)
            else
                $("#details")
                .load(`/web/auction?action=detail&update=true&auctionID=${get_auction_id()}`);
        })
});


function get_auction_id(){
    return document.getElementById("auction_id").value;
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
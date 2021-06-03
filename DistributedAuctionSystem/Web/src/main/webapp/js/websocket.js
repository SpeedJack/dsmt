
var socket = null;
function get_auction_id(){
    return document.getElementById("auction_id").value;
}
window.addEventListener('load', (event) => {
    console.log('opening socket');
    init_socket(socket, get_auction_id())
});

function init_socket(socket, auction) {
    console.log(auction);
    socket = new WebSocket(`ws://localhost:8080/ws/auction/${auction}`);
    socket.onopen = function(e) {
        console.log(`[open] Connection established`);
    };

    socket.onmessage = function(event) {
        console.log(`[message] Data received from server: ${event.data}`);
    };

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
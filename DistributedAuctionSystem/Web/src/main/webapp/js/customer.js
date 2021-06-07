let socket = null;

window.addEventListener('load', (event) => {
    console.log('opening socket');
    init_socket(socket,
        (event) => {
            console.log(`[received message]: ${event.data}`)
            updateAuctionStats();
        })
});

function updateAuctionStats(data){
    let id = get_auction_id();

}

function updateAvailable(data){
    if (!data || data === [])
        return;
    let sold = 0;
    for (let bid in data){
        sold += bid.quantity;
    }
    let sold_elem = document.getElementById("gain");
    if(sold_elem.hasChildNodes()){
        for(let child of sold_elem.childNodes)
            sold_elem.removeChild();
    }
    sold_elem.appendChild(document.createTextNode(sold.toString()));
}

function updateWinner(data){
    if (!data || data === [])
        return;
    let win = false
    for (let bid in data){
        if (bid.hasOwnProperty("user") && bid.user === document.getElementById("user_id").value)
            win = true;
    }
    let win_elem = document.getElementById("winner");
    if(win_elem.hasChildNodes()){
        for(let child of win_elem.childNodes)
            win_elem.removeChild(child);
    }
    win_elem.appendChild(document.createTextNode(win ? "Your bid is a winning one!" : "Your bid is not winning"));
}

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
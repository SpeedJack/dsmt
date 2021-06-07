let socket = null;

window.addEventListener('load', (event) => {
    console.log('opening socket');
    init_socket(socket,
        (event) => {
            console.log(`[received message]: ${event.data}`)
            updateGain(event.data);
            updateSold(event.data);
        })
});

function updateGain(data){
    if (!data || data === [])
        return;
    let gain = 0;
    for (let bid in data){
        gain += bid.quantity * bid.value;
    }
    let gain_elem = document.getElementById("gain");
    if(gain_elem.hasChildNodes()){
        for(let child of gain_elem.childNodes)
            gain_elem.removeChild(child);
    }
    gain_elem.appendChild(document.createTextNode(gain.toString()));
}

function updateSold(data){
    if (!data || data === [])
        return;
    let sold = 0;
    for (let bid in data){
        sold += bid.quantity;
    }
    let sold_elem = document.getElementById("gain");
    if(sold_elem.hasChildNodes()){
        for(let child of sold_elem.childNodes)
            sold_elem.removeChild(child);
    }
    sold_elem.appendChild(document.createTextNode(sold.toString()));
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
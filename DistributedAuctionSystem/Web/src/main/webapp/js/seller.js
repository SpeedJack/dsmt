let socket = null;

window.addEventListener('load', (event) => {
    console.log('opening socket');
    init_socket(socket,
        (event) => {
            console.log(`[received message]: ${event.data}`);
            $("#details")
                .load(`/web/auction?action=detail&auctionID=${get_auction_id()} #details`,
                {action: "detail",
                auctionID: get_auction_id()});
    })
});

function updateGain(data){
    let gain = 0;
    for (let i = 0; i<data.length; i++){
        console.log(data[i]);
        gain += data[i].quantity * data[i].value;
    }
    let gain_elem = document.getElementById("gain");
    if(gain_elem.hasChildNodes()){
        for(let child of gain_elem.childNodes)
            gain_elem.removeChild(child);
    }
    gain_elem.appendChild(document.createTextNode(gain.toString()));
}


function updateLowest(data){
    if(data.length == 0)
        return
    let low_value = data[0].value;
    //MIN
    for (let i = 1; i<data.length; i++){
        if(data[i].value < low_value)
            low_value = data[i].value;
    }
    let min_raise = Number.parseFloat(
        document
            .getElementById("min-raise")
            .innerText
            .toString());

    let low_elem = document.getElementById("min-bid");

    if(low_elem.hasChildNodes()){
        for(let child of low_elem.childNodes)
            low_elem.removeChild(child);
    }
    low_elem.appendChild(document.createTextNode((low_value + min_raise).toString()));
}

function updateSold(data){
    if (!data || data === [])
        return;
    let sold = 0;
    for (let i = 0; i<data.length; i++){
        sold += data[i].quantity;
    }
    let sold_elem = document.getElementById("sold");
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
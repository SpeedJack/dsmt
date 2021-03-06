let socket = null;
let auction = null;
let winningBids = null;
let lowestBids = null;
const cookie = parseCookie(document.cookie);

function registerHandlers() {
    console.log("registering hanlders");
    $("#bid_form").off("submit").on("submit", makeBidHandler);
    $('.delete-bid-button').off('click').on("click", deleteBidHandler);
}

function makeBidHandler(event){
    event.preventDefault();
    let data = new FormData(event.target);
    data = Object.fromEntries(data.entries());
    data.action = "make";
    $.post("/web/bid", data)
        .done((response) => {
            updateCustomerBidsTable(JSON.parse(response));
            registerHandlers();
            $("#errorMessage").text("Your bid has been sent!");
        })
        .fail( () =>  {
            $("#errorMessage").text("Your bid is not valid!");
        });
}


function deleteBidHandler(event){
    event.preventDefault();
    let params = {
        auctionID: document.getElementById("auction_id").value,
        bidID: $(this).closest("tr").attr('id'),
        action: "delete"
    };
    $.post("/web/bid", params)
        .done((response) => {
            //Response is the list of offers for the current user
            updateCustomerBidsTable(JSON.parse(response));
            registerHandlers();
            $("#errorMessage").text("Your bid has been deleted!");
        })
        .fail( () => {
            $("#errorMessage").text("Your bid cannot be deleted!");
        });
}



function updateLowestBidsTable(){
    let body = document.getElementById("lowest-bids-table-body");
    if(body === null)
        return;
    clearTable("lowest-bids-table-body");
    for(let i=0; i< Object.keys(lowestBids).length; i++){
        let row = createRow();
        let data;
        if(i === 0){
            data = `less or equal to ${Object.keys(lowestBids)[i]}`;
        }
        else{
            data = `between ${Object.keys(lowestBids)[i-1]} and ${Object.keys(lowestBids)[i]}`;
        }
        let k = createCell(data);
        let v = createCell(lowestBids[Object.keys(lowestBids)[i]]);
        row.append(k,v);
        body.appendChild(row);
    }
}

function updateSellerDetails(){

    let sold = 0;
    let gain = 0;
    for(const bid of winningBids){
        sold += bid.quantity;
        gain += (bid.quantity * bid.value);
    }
    const gainElem = document.getElementById("gain");
    if(gainElem !== null){
        gainElem.innerText = gain;
    }
    const soldElem = document.getElementById("sold");
    if(soldElem !== null){
        soldElem.innerText = sold;
    }
}

function updateCustomerBidsTable(bids){
    let body = document.getElementById("offers-bid-table-body-customer");
    clearTable("offers-bid-table-body-customer");
    if(bids.length === 0){
        showMessage(false);
    }
    else{
        hideMessage();
        let winning = winningBids.find((bid) => bid.user === parseInt(cookie["userId"]));
        for(let i=0; i<bids.length; i++){
            let bid = bids[i];
            let row = createRow(bid.id.toString());
            let quantity = createCell(bid.quantity);
            let value = createCell(bid.value);
            let isWinning;
            if(winning !== undefined && bid.id === winning.id){
                isWinning = createCell("true");
            }else{
                isWinning = createCell("false");
            }
            let button = createDeleteButton();
            row.append(quantity,value,isWinning,button);
            body.appendChild(row);
        }
    }

}

function updateCustomerWinningBidsTable(){
    let body = document.getElementById("offers-bid-table-body-customer");
    let winning = winningBids.find(bid => bid.user === parseInt(cookie["userId"]));
    if(!winning)
    {
        winning = {};
        winning["id"] = -1;
    }
    let rows = body.getElementsByTagName("tr");
    for(let i=0; i<rows.length; i++){
        let row = rows[i];
        let tds = row.getElementsByTagName("td");
        if (parseInt(row.getAttribute("id")) === winning.id) {
            tds[2].innerHTML = "true";
        }else{
            tds[2].innerHTML = "false";
        }
    }
}

function updateSellerWinningBidsTable(){
    let body = document.getElementById("offers-bid-table-body-seller");
    clearTable("offers-bid-table-body-seller");
    if(winningBids.length === 0){
        showMessage(true);
    }
    else{
        hideMessage();
        for(let i=0; i<winningBids.length; i++){
            let bid = winningBids[i];
            let row = createRow(bid.id.toString());
            let quantity = createCell(bid.quantity);
            let value = createCell(bid.value);
            row.append(quantity,value);
            body.appendChild(row);
        }
    }
}


function parseCookie(cookie){
    let list = cookie.split(";");
    let obj = {};
    for(let c of list){
        let entry = c.split("=");
        if(entry.length !== 0){
            obj[entry[0]] = entry[1];
        }
    }
    return obj;
}


function showMessage(isSeller){
    //$("#no-bid-message").show();
    //$("#offers_bid_table").hide();
    document.getElementById("no-bid-message").hidden = false;
    document.getElementById("offers_bid_table").hidden = true;

}

function hideMessage(){
    //$("#no-bid-message").hide();
    //$("#offers_bid_table").show();
    document.getElementById("no-bid-message").hidden = true;
    document.getElementById("offers_bid_table").hidden = false;
}

function clearTable(id){
        $(`#${id}`).empty();
}


function createRow(id = null){
    let elem = document.createElement("tr");
    if(id !== null){
        elem.setAttribute("id", id);
    }
    return elem;
}

function createCell(data){
    let elem = document.createElement("td");
    if(data !== null){
        elem.innerText = data;
    }
    return elem//create a cell with given data displayed
}

function createDeleteButton(){
    //<input className="btn btn-danger delete-bid-button" type="button" value="Delete"/>
    let elem = createCell(null);
    let button = document.createElement("input");
    button.className = "btn btn-danger delete-bid-button";
    button.setAttribute("type", "button");
    button.setAttribute("value", "Delete");
    elem.appendChild(button);
    return elem;
}


window.addEventListener('load', (event) => {
    registerHandlers();
    init_socket(socket,
        (event) => {
            const dataString = event.data;
            if(dataString.startsWith("CLOSE")){
                window.location.reload(true);
            }
            else{
                const data = JSON.parse(dataString)
                if(data.hasOwnProperty("winningBids")){
                    winningBids = data.winningBids;
                    if(Number(cookie["userId"]) === auction.agent){
                        updateSellerWinningBidsTable();
                        updateSellerDetails();
                    }
                    else{
                        updateCustomerWinningBidsTable();
                    }
                } else if(data.hasOwnProperty("auction")){
                    auction = data.auction;
                } else if(data.hasOwnProperty("lowestBids")){
                    lowestBids = data.lowestBids;
                    updateLowestBidsTable();
                }
            }
            registerHandlers();
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

    socket = new WebSocket(`ws://localhost:8080/ws/auction/${auction}`);
    socket.onopen = function(e) {
        console.log(`[open] Connection established`);
    };

    socket.onmessage = onmessage;
    socket.onclose = function(event) {
        if (event.wasClean) {
            console.log(`[close] Connection closed cleanly, code=${event.code} reason=${event.reason}`);
        } else {
            console.log('[close] Connection died');
        }

    };

    socket.onerror = function(error) {
        console.log(`[error] ${error.message}`);
    };

}
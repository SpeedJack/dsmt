window.addEventListener('load', (event) => {
    endate_countdown()
});

function endate_countdown() {
    var list = document.getElementsByClassName("end_date");
    var dates = new Array();
    for (var i = 0; i < list.length; i++) {
        dates.push(new Date(Number(list.item(i).innerText)).getTime());
    }

    var x = setInterval(function () {

        var now = new Date().getTime();

        for (var i = 0; i < list.length; i++) {
            elem = list.item(i);
            console.log(elem);
            var countDownDate =  dates[i];
            var distance = countDownDate - now;

            var days = Math.floor(distance / (1000 * 60 * 60 * 24));
            var hours = Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
            var minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
            var seconds = Math.floor((distance % (1000 * 60)) / 1000);

            elem.innerHTML = days + "d " + hours + "h "
                + minutes + "m " + seconds + "s  remaining";
            elem.removeAttribute("hidden")
            if (distance < 0) {
                clearInterval(x);
                elem.innerHTML = "Auction closed";
                document.getElementById("submit_button ").setAttribute("disabled", "true");
            }
        }
    }, 1000);
}
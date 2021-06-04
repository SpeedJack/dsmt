window.addEventListener('load', (event) => {
    endate_countdown()
});

function endate_countdown() {
    var countDownDate = new Date(Number(document.getElementById("end_date").innerText)).getTime();

    var x = setInterval(function () {

        var now = new Date().getTime();

        var distance = countDownDate - now;

        var days = Math.floor(distance / (1000 * 60 * 60 * 24));
        var hours = Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
        var minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
        var seconds = Math.floor((distance % (1000 * 60)) / 1000);

        document.getElementById("end_date").innerHTML = days + "d " + hours + "h "
            + minutes + "m " + seconds + "s  remaining";
        document.getElementById("end_date").removeAttribute("hidden")
        if (distance < 0) {
            clearInterval(x);
            document.getElementById("end_date").innerHTML = "Auction closed";
            document.getElementById("submit_button ").setAttribute("disabled", "true");
        }
    }, 1000);
}
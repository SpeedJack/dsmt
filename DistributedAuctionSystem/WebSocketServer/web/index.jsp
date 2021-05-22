<%--
  Created by IntelliJ IDEA.
  User: nicola
  Date: 20/05/21
  Time: 17:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>$Title$</title>
  </head>
  <body>
  $END$
  </body>
  <script>
    let backend_socket = new WebSocket("ws://localhost:8080/ws/auction_backend/0");
    let socket = new WebSocket("ws://localhost:8080/ws/auction/0");
    socket.onopen = backend_socket.onopen = function(e) {
      alert("[open] Connection established");
    };

    backend_socket.onmessage = socket.onmessage = function(event) {
      console.log(event.data);
      alert(`[message] Data received from server: ${event.data}`);
    };

    backend_socket.onclose = socket.onclose = function(event) {
      if (event.wasClean) {
        alert(`[close] Connection closed cleanly, code=${event.code} reason=${event.reason}`);
      } else {
        // e.g. server process killed or network down
        // event.code is usually 1006 in this case
        alert('[close] Connection died');
      }
    };

    backend_socket.onerror = socket.onerror = function(error) {
      alert(`[error] ${error.message}`);
    };

    let state = {
      winningBids : [
        {
          user : 1,
          auction: 0,
          timestamp: Date.now(),
          value : 32.0,
          quantity: 1,
          id: 1,
        },
        {
          user : 1,
          auction: 0,
          timestamp: Date.now(),
          value : 32.0,
          quantity: 1,
          id: 1,
        },
        {
          user : 1,
          auction: 0,
          timestamp: Date.now(),
          value : 32.0,
          quantity: 1,
          id: 1,
        }
      ]
    }
    alert("Sending to server" + JSON.stringify(state));
    backend_socket.send(JSON.stringify(state));
  </script>
</html>

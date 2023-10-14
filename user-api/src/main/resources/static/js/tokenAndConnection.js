var sock = new SockJS("/ws/chat", null, {withCredentials: true});
var ws = Stomp.over(sock);

async function connectWebSocket() { // Modified to be an async function
  return new Promise((resolve, reject) => {
    ws.connect({}, async function (frame) {
      let userInfo = await instance.get("/auth/getInfo", {});
      userForm = userInfo.data;
      resolve(frame);
    }, reject);
  }).catch(error => {
    console.error("An error occurred: ", error);
  });
}

async function initializeApp() {
  try {

    await connectWebSocket(); // Wait for WebSocket connection to establish
    await findAllRoom(); // Wait for findAllRoom to complete

  } catch (error) {
    console.error(error);
  }

}

initializeApp().catch(error => console.error(error));
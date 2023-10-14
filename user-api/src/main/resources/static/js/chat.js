var token = `eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJBQUFBQUFBQUFBQUFBQUFBQUFBQUFBPT06NktsaGdkZUxhY0JNUUtYUzBQR1FIZGYxSzFkMVdFWjJQZWhwZFhLNi96TT0iLCJqdGkiOiJBQUFBQUFBQUFBQUFBQUFBQUFBQUFBPT06Z0k3MXNnYmRlZGlxOXBqbUVadTNyUT09Iiwicm9sZXMiOlsiUk9MRV9DVVNUT01FUiIsIlJPTEVfU0VMTEVSIl0sImlhdCI6MTY5Njg4MTUzMywiZXhwIjoxNjk2ODg1MTMzfQ.pV9qWwCr9AER8ObsFqHaX0VWbJ7nmmBu3xjLM9FYQ28`;
var sock = new SockJS(
    "/ws/chat");
var ws = Stomp.over(sock);

var currentSubscription;

var userForm = '';
var sender = '';
var roomId = '';
var chatRoom = '';
var chatRooms = '';
var messageForm = '';
var hisMessages = '';

function getChatRoomList(data) {
  return `<a href="#" class="${data.roomId}" id = "room-id">
    <div class="user_row">
      <div class="col-3"><img class="mr-3 rounded-circle" src="./img/profile.png"
          alt="Generic placeholder image"></div>
      <div class="col-9">${data.roomName}
        <br /><small>Hi, I am using Whats app</small>
      </div>
    </div>
  </a>`
}

function getChatBox(chatRoom) {
  return `<div id="user_chat_data" class="user_chat_data">
  <div class="profile_name"><a href="#"><i class="fa fa fa-arrow-left" aria-hidden="true"></i></a>
    &nbsp;&nbsp;&nbsp;&nbsp;<img src="./img/profile.png" class="mr-3 rounded-circle"> &nbsp;&nbsp;
  ${chatRoom.roomName}</div>
  <div class="container-fluid chat_section" id="chat-box">
  </div>
  <div class="type_msg">
    <div class="input_msg_write">
      <input id="chat-outgoing-msg" type="text" class="write_msg" placeholder="Type a message" />
      <button id="chat-outgoing-button" class="msg_send_btn" type="button"><i class="fa fa-paper-plane"
          aria-hidden="true"></i></button>
    </div>
  </div>
</div>`
}

//------------------------------------
function getSendMsgBox(data) {
  return `<div class="sent_msg">
    <p>${data.content}</p>
    <span class="time_date"> 09:01 | August 9</span>
</div>`
}

function getReceiveMsgBox(data) {
  return `<div class="received_withd_msg">
    <p>${data.content}</p>
    <span class="time_date"> 09:01 | August 9</span>
</div>`
}

// 파란박스 만들기(자신)
function initMyMessage(data) {
  let chatBox = document.querySelector("#chat-box");

  let sendBox = document.createElement("div")
  sendBox.className = "outgoing_msg"

  sendBox.innerHTML = getSendMsgBox(data);
  chatBox.append(sendBox)
  document.documentElement.scrollTop = document.body.scrollHeight;
}

// 회색박스 만들기(타인)
function initOtherMessage(data) {
  let chatBox = document.querySelector("#chat-box");

  let receivedBox = document.createElement("div")
  receivedBox.className = "received_msg"

  receivedBox.innerHTML = getReceiveMsgBox(data);
  chatBox.append(receivedBox)
  document.documentElement.scrollTop = document.body.scrollHeight;
}

function initGreetMessage(data) {
  let chatBox = document.querySelector("#chat-box");
  let greetBox = document.createElement("div")
  let greetMessageBox = document.createElement("p")
  greetBox.className = "gray-bar";
  greetMessageBox.textContent = data.sender + "님이 입장하셨습니다."

  greetBox.appendChild(greetMessageBox)
  chatBox.append(greetBox);
}

function addMessage() {
  let msgInput = document.querySelector("#chat-outgoing-msg");

  let message = {
    type: 'TALK', content: msgInput.value, sender: sender, roomId: roomId,

  }
  ws.send("/pub/chat/message", {},
      JSON.stringify({
        type: message.type,
        roomId: message.roomId,
        sender: message.sender,
        content: message.content
      }));
  msgInput.value = '';
}

//-----------------------------------

async function findRoom(roomId) {
  try {

    const response = await axios.get('/chat/room/' + roomId, {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    });
    chatRoom = response.data;
  } catch (error) {
    console.error(error)
  }
}

async function findAllRoom() { // Modified to be an async function
  try {
    const response = await axios.get('/chat/rooms', {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    });
    chatRooms = response.data;

    await addList(this.chatRooms); // Wait for addList to complete

  } catch (error) {
    console.error(error);
  }

}

function addList(datas) {
  let chatListBox = document.querySelector("#chat-list-box")
  for (const chatroom of datas) {
    console.log(chatroom)
    let user = document.createElement("div")
    user.className = "user";
    user.innerHTML = getChatRoomList(chatroom);
    user.addEventListener("click", () => { // 각각의 챗룸에 대해 클릭 이벤트 추가
      callMessages(chatroom.roomId);
    })
    chatListBox.append(user);
  }
}

async function callMessages(requestRoomId) {
  sender = userForm.id;

  roomId = requestRoomId;
  await findRoom(requestRoomId);

  await axios.get("/chat/messages/" + requestRoomId, {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  }).then(response => {
    hisMessages = response.data;

  }).catch(error => {
    console.error("Get Messages Error", error.response.data);
  })
  let delElement = document.getElementById("out-chat-box")
  if (delElement !== null) {
    delElement.remove();
  }
  let secondBox = document.querySelector('#second-box');
  let outChatBox = document.createElement("div");
  outChatBox.id = "out-chat-box"
  outChatBox.innerHTML = getChatBox(chatRoom);
  secondBox.append(outChatBox);

  for (const message of hisMessages) {
    if (message.type == "ENTER") {
      initGreetMessage(message);
    } else {
      if (message.sender == sender) {
        initMyMessage(message);
      } else {
        initOtherMessage(message);
      }
    }
  }
  let chatBox = document.getElementById('chat-box');
  chatBox.scrollTop = chatBox.scrollHeight;

  // Unsubscribe from previous room if any
  if (currentSubscription) {
    currentSubscription.unsubscribe();
  }

  // Subscribe to the new room
  currentSubscription = ws.subscribe("/sub/chat/room/" + roomId,
      function (data) {
        messageForm = JSON.parse(data.body);
        console.log(messageForm);
        if (messageForm.type == "ENTER") {
          initGreetMessage(messageForm);
        } else {
          if (messageForm.sender == sender) {
            initMyMessage(messageForm);
          } else {
            initOtherMessage(messageForm);
          }
        }
        chatBox.scrollTop = chatBox.scrollHeight;
      });

  document.querySelector("#chat-outgoing-button").addEventListener("click",
      () => {
        addMessage()
      })

  document.querySelector("#chat-outgoing-msg").addEventListener("keydown",
      async (e) => {
        var messageValue = $('#chat-outgoing-msg').val();
        if (e.keyCode === 13) {
          if (messageValue !== null && messageValue.trim() !== '') {
            await addMessage()
          }
        }
      })

}

async function connectWebSocket() { // Modified to be an async function
  return new Promise((resolve, reject) => {
    ws.connect({'token': token}, async function (frame) {
      let userInfo = await axios.get("/auth/getInfo", {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });
      userForm = userInfo.data;
      resolve(frame);
    }, function (error) {
      console.error("Websocket connection error : " + error);
      reject(error)
    });
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



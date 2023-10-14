var currentSubscription;
var userForm = '';
var sender = '';
var roomId = '';
var chatRoom = '';
var chatRooms = '';
var messageForm = '';
let Months = ["January", "February", "March", "April", "May", "June", "July",
  "August", "September", "October", "November", "December"];

function getChatRoomList(data) {
  return `<a href="#" class="${data.roomId}" id = "room-id">
    <div class="user_row">
      <div class="col-3"></div>
      <div class="col-9">${data.roomName}
      </div>
    </div>
  </a>`
}

function getChatBox(chatRoom) {
  return `<div id="user_chat_data" class="user_chat_data">
  <div class="profile_name"><a href="#"><i class="fa fa fa-arrow-left" aria-hidden="true"></i></a>
    &nbsp;&nbsp;
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
<div>
    <p>${data.content}</p>
</div>
    <span class="time_date"> ${data.hour}:${data.minute} | ${data.month} ${data.day}</span>
</div>`
}

function getReceiveMsgBox(data) {
  return `<div class="received_withd_msg">
<div class="received_withd_msg_div1">
<p >${data.sender}</p>
</div>
<div class="received_withd_msg_div2">
    <p>${data.content}</p>
</div>
    <span class="time_date"> ${data.hour}:${data.minute} | ${data.month} ${data.day}</span>
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
    type: 'TALK',
    content: msgInput.value,
    sender: sender,
    roomId: roomId

  }
  ws.send("/pub/chat/message", {},
      JSON.stringify({
        type: message.type,
        roomId: message.roomId,
        sender: message.sender,
        content: message.content
      }));
  msgInput.value = '';

  msgInput.focus();
}

//-----------------------------------

async function findRoom(roomId) {
  try {

    const response = await instance.get('/chat/room/' + roomId,
        {withCredentials: true});
    chatRoom = response.data;
  } catch (error) {
    console.error(error)
  }
}

async function findAllRoom(retryCount = 0) { // Modified to be an async function
  try {
    await instance.get('/chat/rooms', {withCredentials: true}).then(
        response => {
          chatRooms = response.data;
        })

    await addList(this.chatRooms); // Wait for addList to complete

  } catch (error) {

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

async function getMessages(requestRoomId, retryCount = 0) {
  try {
    const response = await instance.get("/chat/messages/" + requestRoomId,
        {withCredentials: true});
    return response.data;
  } catch (error) {

  }
}

async function callMessages(requestRoomId, retryCount = 0) {

  sender = userForm.id;
  roomId = requestRoomId;
  await findRoom(requestRoomId);

  hisMessages = await getMessages(requestRoomId);

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
    message.month = Months[(message.month - 1)];
    console.log(message.month);
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
        messageForm.month = Months[(messageForm.month - 1)];
        console.log(messageForm.month);
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

  let lastMessageTime = 0;
  const MIN_INTERVAL = 500;  // 최소 인터벌 (밀리세컨드)
  const DISABLE_TIME = 1000 * 3;

  document.querySelector("#chat-outgoing-msg").addEventListener(
      "keypress" || "click",
      async (e) => {
        var messageField = document.querySelector("#chat-outgoing-msg");
        var messageValue = $('#chat-outgoing-msg').val();
        if (e.keyCode === 13) {
          if (messageValue !== null && messageValue.trim() !== '') {
            let now = Date.now();
            if (now - lastMessageTime < MIN_INTERVAL) {
              alert("5초간 입력이 제한됩니다.");

              // 입력 필드 비활성화
              messageField.disabled = true;

              // 일정 시간 후에 다시 활성화
              setTimeout(() => {
                messageField.disabled = false;
              }, DISABLE_TIME);

              e.preventDefault(); // Enter 키 기본 동작(메시지 전송 등) 막기
              return;
            }
            await addMessage()

            lastMessageTime = now;
          }
        }
      })

}
'use strict';

var usernamePage = document.querySelector('#username-page');
var chatPage = document.querySelector('#chat-page');
var usernameForm = document.querySelector('#usernameForm');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');
var userList = document.querySelector('#userList');

var stompClient = null;
var sender = null;
var nickname = null;

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

function connect(event) {
    nickname = document.querySelector('#name').value.trim();

    if(nickname) {
        usernamePage.classList.add('hidden');
        chatPage.classList.remove('hidden');

        var socket = new SockJS('/websocket');
        stompClient = Stomp.over(socket);

        const headers = {
                       nickname: nickname
                      };
        stompClient.connect(headers, onConnected, onError);
    }
    event.preventDefault();
}


function onConnected() {
    // Subscribe to the Chat Topic
    stompClient.subscribe('/topic/chat', onMessageReceived);

    checkUser();

    connectingElement.classList.add('hidden');
}

function checkUser(){
    const data = {
      nickname: nickname,
    };

    fetch('/chat-user', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(data)
        })
        .then(response => response.json())
        .then(data => {
                sender = data;
                })
        .then(() => {
            console.log("Show messages");
            getLastMessages().then(messages => {
                               messages.forEach(showMessage);
                               messageArea.scrollTop = messageArea.scrollHeight;
                               })
                               .catch(error => {
                                 console.error('Error:', error);
                               });
            getOnlineUsers().then(onlineUsers => {
                                onlineUsers.forEach(showOnlineUser);
                                })
                                .catch(error => {
                                console.error('Error:', error);
                                });
            })
        .catch(error => {
             console.error('Error:', error);
           });
}

function getLastMessages(){
    return fetch('/message?size=5')
        .then(response => response.json())
        .then(data => {
                return data;
                })
        .catch(error => {
             console.error('Error:', error);
           });
}

function getOnlineUsers(){
    return fetch('/online-chat-user')
        .then(response => response.json())
        .then(data => {
                return data;
                })
        .catch(error => {
             console.error('Error:', error);
           });
}

function onError(error) {
    connectingElement.textContent = 'Unable to connect to WebSocket!';
    connectingElement.style.color = 'red';
}


function send(event) {
    var messageContent = messageInput.value.trim();

    if(messageContent && stompClient) {
        var chatMessage = {
            sender: sender,
            text: messageInput.value
        };

        stompClient.send("/app/message", {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
    event.preventDefault();
}


function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);

    showMessage(message);

    getOnlineUsers().then(onlineUsers => {
                                    userList.textContent = '';
                                    onlineUsers.forEach(showOnlineUser);
                                    })
                                    .catch(error => {
                                    console.error('Error:', error);
                                    });

    messageArea.scrollTop = messageArea.scrollHeight;
}

function showMessage(message){
    var messageElement = document.createElement('li');

    messageElement.classList.add('chat-message');

    var avatarElement = document.createElement('i');
    var avatarText = document.createTextNode(message.sender.nickname[0]);
    avatarElement.appendChild(avatarText);
    avatarElement.style['background-color'] = getAvatarColor(message.sender.nickname);

    messageElement.appendChild(avatarElement);

    var usernameElement = document.createElement('span');
    var usernameText = document.createTextNode(message.sender.nickname);
    usernameElement.appendChild(usernameText);
    messageElement.appendChild(usernameElement);

    var dateTimeElement = document.createElement('em');
    const date = new Date(message.createdAt);
    const formattedDateString = date.toLocaleString('ru-RU');
    var dateTimeText = document.createTextNode(" " + formattedDateString);
    dateTimeElement.appendChild(dateTimeText);
    messageElement.appendChild(dateTimeElement);

    var textElement = document.createElement('p');
    var messageText = document.createTextNode(message.text);
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);
    messageArea.appendChild(messageElement);
}

function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }

    var index = Math.abs(hash % colors.length);
    return colors[index];
}

function showOnlineUser(onlineUser){
    var onlineUserElement = document.createElement('span');

    var nicknameText = document.createTextNode(onlineUser.nickname + " ");
    onlineUserElement.appendChild(nicknameText);

    userList.appendChild(onlineUserElement);
}


usernameForm.addEventListener('submit', connect, true)
messageForm.addEventListener('submit', send, true)
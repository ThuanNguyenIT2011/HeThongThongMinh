function getHTMLTemplateMessage(site, obj) {
  if (site === "ai") {
    const now = new Date();

    return `<div class="d-flex justify-content-between">
                        <p class="small mb-1">${obj.name}</p>
                        <p class="small mb-1 text-muted">${now.getHours()}:${now.getMinutes()}</p>
                    </div>
                    <div class="d-flex flex-row justify-content-start">
                        <img src="https://mdbcdn.b-cdn.net/img/Photos/new-templates/bootstrap-chat/ava5-bg.webp"
                            alt="avatar 1" style="width: 45px; height: 100%;">
                        <div>
                            <p class="small p-2 ms-3 mb-3 rounded-sm" style="background-color: #f5f6f7;">${
                              obj.message
                            }</p>
                        </div>
                    </div>`;
  } else if (site === "user") {
    return `<div class="d-flex justify-content-between">
                        <p class="small mb-1 text-muted"></p>
                        <p class="small mb-1">${obj.name}</p>
                    </div>
                    <div class="d-flex flex-row justify-content-end mb-4 pt-1">
                        <div>
                            <p class="small p-2 me-3 mb-3 text-white rounded-sm" style="background-color: #FFD333">${obj.message}</p>
                        </div>
                        <img src="https://mdbcdn.b-cdn.net/img/Photos/new-templates/bootstrap-chat/ava6-bg.webp"
                            alt="avatar 1" style="width: 45px; height: 100%;">
                    </div>`;
  }
}
document.querySelector("#button-addon2").addEventListener("click", function () {
  addNewMessage();
});
document
  .getElementById("chatbox-input")
  .addEventListener("keydown", function (e) {
    if (e.key == "Enter") {
      addNewMessage();
    }
  });
function addNewMessage() {
  const input = document.getElementById("chatbox-input");
  const value = input.value;
  if (value) {
    document.querySelector(".card-body").insertAdjacentHTML(
      "beforeend",
      getHTMLTemplateMessage("user", {
        name: "Johny Bullock",
        message: value,
      })
    );
    document.querySelector(".card-body").scroll(0, 10000);
    input.value = "";
    setTimeout(function () {
      document.querySelector(".card-body").insertAdjacentHTML(
        "beforeend",
        getHTMLTemplateMessage("ai", {
          name: "AI",
          message: "Please wait ... ",
        })
      );
      document.querySelector(".card-body").scroll(0, 10000);
    }, 1000);
  }
}
document.querySelector(".btn-toggleChatBox").onclick = function () {
  const chatBox = document.querySelector(".chat-box");
  chatBox.classList.toggle("show");
  chatBox.classList.toggle("hide", !chatBox.classList.contains("show"));
};
document
  .querySelector(".card-header .fa-times")
  .addEventListener("click", () => {
    document.querySelector(".chat-box").classList.replace("show", "hide");
  });

function getHTMLTemplateMessage(site, obj) {
  if (site === "ai") {
    const now = new Date();

    return `<div class="d-flex justify-content-between">
                        <p class="small ml-1 mb-1 text-dark">${obj.name}</p>
                        <p class="small mb-1 text-muted">${now.getHours()}:${now.getMinutes()}</p>
                    </div>
                    <div class="d-flex align-items-center justify-content-start">
                        <img src="https://mdbcdn.b-cdn.net/img/Photos/new-templates/bootstrap-chat/ava5-bg.webp"
                            alt="avatar 1" style="width: 45px; height: 100%;">
                        <div>
                            <p class="small mb-0 p-2 ms-3 ml-1 rounded-sm text-dark" style="background-color: #f5f6f7;">${
                              obj.message
                            }</p>
                        </div>
                    </div>`;
  } else if (site === "user") {
    return `<div class="d-flex justify-content-between">
                        <p class="small mb-1 text-muted text-dark"></p>
                        <p class="small mb-1 text-dark">${obj.name}</p>
                    </div>
                    <div class="d-flex align-items-center justify-content-end mb-4 pt-1">
                        <div>
                            <p class="small p-2 me-3 mb-0 mr-1 text-white rounded-sm" style="background-color: #FFD333">${obj.message}</p>
                        </div>
                        <img src="https://mdbcdn.b-cdn.net/img/Photos/new-templates/bootstrap-chat/ava6-bg.webp"
                            alt="avatar 1" style="width: 45px; height: 100%;">
                    </div>`;
  }
}
document.querySelector("#button-addon2").addEventListener("click", function () {
  addNewMessage();
});

document.getElementById("chatbox-input").addEventListener("keydown", function (e) {
    if (e.key == "Enter") {
      addNewMessage();
    }
  });
  
  
function addNewMessage() {
  const input = document.getElementById("chatbox-input");
  const inputClient = input.value;
  if (inputClient) {
    document.querySelector(".card-body").insertAdjacentHTML(
      "beforeend",
      getHTMLTemplateMessage("user", {
        name: "Client",
        message: inputClient,
      })
    );
    document.querySelector(".card-body").scroll(0, 10000);
    
    // api
    getDataFromFlask(inputClient)
    	.then(response => {
			document.querySelector(".card-body").
				insertAdjacentHTML("beforeend",getHTMLTemplateMessage("ai", { name: "AI", message: response.data.name,}))
      		document.querySelector(".card-body").scroll(0, 10000);
		})
    input.value = ""
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
  

const urlGetData = "http://127.0.0.1:5000/prediction"
  
  
 
async function getDataFromFlask(content){
	try {
		const responseApi = await fetch(urlGetData,  {
		    method: "POST",
		    cache: "no-cache", 
		    headers: {"Content-Type": "application/json"},
		    body: JSON.stringify({content})
	    })
	    return await responseApi.json()
	   
	} 
	catch(error) {
		console.log(er)
	}
}







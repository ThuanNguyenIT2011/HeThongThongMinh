

let isInputOrderId = false
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
  
  
 function isInteger(str) {
  let n = parseInt(str);
  return !isNaN(n) && Number.isInteger(n);
}

  
  
function addNewMessage() {
  const input = document.getElementById("chatbox-input");
  const inputClient = input.value;
  input.value = ''
  if (inputClient) {
    document.querySelector(".card-body").insertAdjacentHTML(
      "beforeend",
      getHTMLTemplateMessage("user", {
        name: "Client",
        message: inputClient,
      })
    );
    document.querySelector(".card-body").scroll(0, 10000);
    
    if (isInputOrderId) {		
	 	
	 	// check xem co ton tai so trong nay khong. 
	 	if(!isInteger(inputClient)) {
			// thông báo với nguoi dung
			document.querySelector(".card-body").
					insertAdjacentHTML("beforeend",getHTMLTemplateMessage("ai", { name: "AI", message: "Số hóa đơn là số nguyên!"}))
      		document.querySelector(".card-body").scroll(0, 10000);
			return
		}
		 isInputOrderId = false 	 
		 fetch(
			 `http://localhost:9000/Shopme/api/v1/guarantee/${inputClient}`,
			 {
			 method: "GET",
			 cache: "no-cache", 
		     headers: {"Content-Type": "application/json"}
			 }
		 )
		 .then(response => response.json())
		 .then(data => {
	
			 // hienr thị ra thông tin của khách hàng. + địa chỉ 
			 let message = `Thông tin bảo hành<br>${data.customerName}, ${data.customerPhoneNumber}`
			 let guarantees = data.guarantees 
			 let x = ''
			 for(let guarantee of guarantees) {
				 let y = ''
				 if (guarantee.stillUnderGuarantee) {
					y = `còn bảo hành (${guarantee.guaranteeStartTime.slice(0,10)}, ${guarantee.guaranteeEndTime.slice(0,10)})` 
			
				 }
				 else {
					y = `không còn bảo hành (${guarantee.guaranteeStartTime.slice(0,10)}, ${guarantee.guaranteeEndTime.slice(0,10)})` 
				 }
				 
				 x += `<br>${guarantee.productName}, ${y}`
			 }
			 message += x
			document.querySelector(".card-body").
					insertAdjacentHTML("beforeend",getHTMLTemplateMessage("ai", { name: "AI", message: message}))
      		document.querySelector(".card-body").scroll(0, 10000);
      		document.querySelector(".card-body").
					insertAdjacentHTML("beforeend",getHTMLTemplateMessage("ai", { name: "AI", message: 'Bạn có thể bảo hành thiết bị tại: 230/15A Man Thiện, TP.Thủ Đức'}))
      		document.querySelector(".card-body").scroll(0, 10000);
		
			isInputOrderId = false
		 })
		 
		
		
		
		
		return
	} 
    
    // api
    getDataFromFlask(inputClient)
    	.then(response => {
			let message
			if (response.data.id == 0) {
				message = 'Xin chào mình là chatbot KingOfShop, bạn có vấn đề gì về thiết bị điện tử bạn có thể mô tả cho tôi để tôi dự đoán' 
			}
			else {		
				message = 'Tôi dự đoán bạn đang gặp phải vấn đề về ' +  response.data.name + ` (${response.data.type}) `
			}	
			document.querySelector(".card-body").
				insertAdjacentHTML("beforeend",getHTMLTemplateMessage("ai", { name: "AI", message}))
      		document.querySelector(".card-body").scroll(0, 10000);
      		
      		if (response.data.solution != '') {
					solution = `Bạn có thể tham khảo cách giải quyết tại: <a href="${response.data.solution}">link</a>`
					document.querySelector(".card-body").
					insertAdjacentHTML("beforeend",getHTMLTemplateMessage("ai", { name: "AI", message: solution}))
      		document.querySelector(".card-body").scroll(0, 10000);
			}
			
			if (response.data.type === 'hardware')  {
				// hỏi xem người dùng có mua máy ở đây không
				let message = `Thiết bị được mua tại cửa hàng: <a onclick="clickYesWithAI()" style="cursor: pointer"><u>yes</u></a> / <a onclick="clickNoWithAI()" style="cursor: pointer"><u>no</u></a>`
				document.querySelector(".card-body").insertAdjacentHTML("beforeend",getHTMLTemplateMessage("ai", { name: "AI", message}))
      		document.querySelector(".card-body").scroll(0, 10000);
				
			}
		})
  }
}


function clickYesWithAI() {
	// khi nguoi dung chon yes -> sau do nguoi dung se nhap vao ma so phieu mua hang. /
	isInputOrderId = true 
	let message = "Hãy cho tôi biết số hóa đơn khi mua hàng, tôi sẽ giúp bạn kiểm tra xem thiết bị bạn mua có còn bảo hành hay không?"
	document.querySelector(".card-body").insertAdjacentHTML("beforeend",getHTMLTemplateMessage("ai", { name: "AI", message}))
      		document.querySelector(".card-body").scroll(0, 10000);
	
	
}

function clickNoWithAI() {
	// khi nguoi dung chon yes -> sau do nguoi dung se nhap vao ma so phieu mua hang. 
	let message = "Bạn có thể mang thiết bị đến 230/15A Man Thiện TP.Thủ Đức để chúng tôi kiểm tra!"
	document.querySelector(".card-body").insertAdjacentHTML("beforeend",getHTMLTemplateMessage("ai", { name: "AI", message}))
      		document.querySelector(".card-body").scroll(0, 10000);
	
	
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







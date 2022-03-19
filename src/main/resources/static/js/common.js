	function triggerLoginForm(displayStyle){
			const modalBoxWrapper = document.getElementsByClassName('boxed_wrapper')[0];
		    modalBoxWrapper.style.display = displayStyle;
		}
		//thai's code
		 function resetEmail() {
             var email = document.getElementById("reset-email").value;
             if (/^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/.test(email)) {
                 var formData = new FormData();
                 //var formData1 = new FormData();
                 formData.append("email", email);
                 fetch("/ChildrenCare/api-user/user", {
                     method: 'post',
                     body: formData
                 }).then(response => {
                     return response.text();
                 }).then(message => {
                     if (message !== "not exist") {
                         //formData1.append("email",email);
                         fetch("/ChildrenCare/api-login/reset",{
                             method : 'post',
                             body : formData
                         }).then(response => {
                             return response.text(); //tra ket qua tu server -> text
                         }).then(message => {
                             if(message === "Success"){
                                 alert("Success");
                                 var closeButton = document.getElementById("big-button-close");
                                 var closeReset = document.getElementById("close-reset");
                                 closeButton.click();
                                 closeReset.click();
                             }else{
                                 alert("Fail");
                             }
                         })
                     } else{
                         alert("Email is not in database")
                     }

                 })
             } else {
                 alert("Invalid Email");
             }
         }
		
		 function changeLanguage(langCode){
				var queryString = window.location.search;
				var params = new URLSearchParams(queryString);
				if(!params.has("lang")){
					params.append("lang",langCode);
				}else{
					params.set("lang",langCode);
				}
				window.location.href = window.location.pathname +"?"+ params;
			}

        //end
        //nghia's code
        function loginCheck(){
            var email = document.getElementById("email").value;
            var password = document.getElementById("pass").value;
            var formData = new FormData();
            formData.append("email",email);
            formData.append("password",password);
            formData.append("currentpage",currentpage);
            fetch("/ChildrenCare/api-login/signIn",{
                method : "post",
                body : formData
            }).then(respond => {
                return respond.json();
            }).then(mapObject => {
                const message = mapObject.message;
                alert(message);
                if(message === "successfully"){
                	const url = mapObject.url;
                	if(typeof url === 'undefined'){
                		window.location.reload();
                	}else{
                		window.location.href = "/ChildrenCare"+url;
                	}
                	
                }
            })
        }
    var capchaGlobal;
    function generateCapcha(){
        var formData1 = new FormData();
        var email_register = document.getElementById("email_register").value;
        formData1.append("email",email_register);
        fetch("/ChildrenCare/api-feedback/generate-captcha",{
            method:"POST",
            body:formData1
        }).then(respond => {
            alert("The capcha code has been sent to your email");
            return respond.text();
        }).then(capcha => {
            capchaGlobal = capcha;
        })
    }

    function register(){

        var confirmCheck = document.getElementById("confirm").value;
        if(confirmCheck !== capchaGlobal){
            alert("Capcha invalid!");
            return;
        }

        var email_register = document.getElementById("email_register").value;
        var username_register = document.getElementById("username_register").value;
        var fullname_register = document.getElementById("fullname_register").value;
        var address_register = document.getElementById("address_register").value;
        var gender_register = document.getElementById("gender_register").value;
        var notes_register = document.getElementById("notes_register").value;
        var password_register = document.getElementById("password_register").value;
        var phone_register = document.getElementById("phone_register").value;
        var formData = new FormData();
        formData.append("email",email_register);
        formData.append("username",username_register);
        formData.append("fullname",fullname_register);
        formData.append("address",address_register);
        formData.append("gender",gender_register);
        formData.append("notes",notes_register);
        formData.append("password",password_register);
        formData.append("phone",phone_register);

        fetch("/ChildrenCare/api-login/signUp",{
            method : "post",
            body : formData
        }).then(respond => {
            return respond.text();
        }).then(message => {
            alert(message);
            window.location.reload();
        })
    }
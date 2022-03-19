
var selectorRules = {};

function Validator(rules){

    rules.forEach(rule => {
        if(Array.isArray(selectorRules[rule.selector])){
            selectorRules[rule.selector].push(rule.test);
        }else{
            selectorRules[rule.selector] = [rule.test];
        }

        const formElement = document.querySelector(rule.selector);
        const formMessageElement = formElement.parentElement.getElementsByClassName("form-message")[0];
        // when user blur out of input field
        formElement.onblur = function(){
            const formValue = formElement.value;
            const fieldRules = selectorRules[rule.selector];


            var errorMessage;
            for(let i = 0 ; i < fieldRules.length ; i++){
                errorMessage = fieldRules[i](formValue);
                if(errorMessage) break;
            }

            if(errorMessage){
                formMessageElement.innerHTML = errorMessage;
                formElement.classList.add("invalid");
            }else{
                formMessageElement.innerHTML = "";
                formElement.classList.remove("invalid");
            }
        }

        // when user input something
        formElement.oninput = function (){
            formMessageElement.innerHTML = "";
            formElement.classList.remove("invalid");
        }
    })
}

Validator.isRequired = function (selector,length,message){
    return {
        selector : selector,
        test : function(value){
            return value.trim().length > length ? undefined : message || 'This field must contains at lease '+length+' characters';
        }
    }
}

Validator.isMinNumber = function (selector,minValue,message){
    return {
        selector : selector,
        test : function (value){
            if(isNaN(value)){
                return "Must be a number";
            }
            if(parseInt(value) <= minValue){
                return message || "Must be greater than "+minValue;
            }
            return undefined;
        }
    }
}


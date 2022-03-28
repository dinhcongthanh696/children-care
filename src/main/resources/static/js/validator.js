
var rules = {};

function validate(InputElement,selector){
    const inputValue = InputElement.value;
    const selectorRules = rules[selector];
    const formMessageElement = InputElement.parentElement.getElementsByClassName("form-message")[0];
    var errorMessage;
    for(let i = 0 ; i < selectorRules.length ; i++){
        errorMessage = selectorRules[i](inputValue);
        if(errorMessage) break;
    }

    if(errorMessage){
        formMessageElement.innerHTML = errorMessage;
        InputElement.classList.add("invalid");
    }else{
        formMessageElement.innerHTML = "";
        InputElement.classList.remove("invalid");
    }

    return (typeof errorMessage === 'undefined') ? true : false;
}

function Validator(options){
    var selectorRules = options.selectorRules;
    var formElement = document.getElementById(options.formId);

    formElement.onsubmit = event => {
        event.preventDefault();
        var isFormValid = true;

        selectorRules.forEach(selectorRule => {
            const InputElement = formElement.querySelector(selectorRule.selector);
            let  isInputValid = validate(InputElement,selectorRule.selector);
            if(isFormValid && !isInputValid){
                isFormValid = false;
            }
        })

        if(isFormValid){
            if(typeof options.onSubmit === 'function'){
                options.onSubmit();
            }else{
                formElement.submit();
            }
        }
    }

    selectorRules.forEach(selectorRule => {
        if(Array.isArray(rules[selectorRule.selector])){
            rules[selectorRule.selector].push(selectorRule.test);
        }else{
            rules[selectorRule.selector] = [selectorRule.test];
        }

        const InputElement = formElement.querySelector(selectorRule.selector);
        const formMessageElement = InputElement.parentElement.getElementsByClassName("form-message")[0];
        // when user blur out of input field
        InputElement.onblur = function(){
            validate(InputElement,selectorRule.selector);
        }

        // when user input something
        InputElement.oninput = function (){
            formMessageElement.innerHTML = "";
            InputElement.classList.remove("invalid");
        }
    })
}

Validator.isRequired = function (selector,length,message){
    return {
        selector : selector,
        test : function(value){
            return value.trim().length > length ? undefined :
                message || 'This field must contains at lease '+length+' characters';
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


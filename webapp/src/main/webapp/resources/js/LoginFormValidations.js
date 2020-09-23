var email = document.getElementById("registerEmail"),
    password = document.getElementById("registerPassword"),
    confirm_password = document.getElementById("registerPasswordConfirm");

function validateConfirmPassword(){
    if(password.value !== confirm_password.value) {
        confirm_password.setCustomValidity("Passwords Don't Match");
    } else {
        confirm_password.setCustomValidity('');
    }
}

function SetValidations(){

    if(password.classList.contains("is-invalid")){
        password.setCustomValidity("Invalid Password");
    }

    if(email.classList.contains("is-invalid")){
        email.setCustomValidity("Invalid Password");
    }
}

onload = SetValidations;

password.onchange = () => {
    password.setCustomValidity("");
}

email.onchange = () => {
    email.setCustomValidity("");
}

confirm_password.onchange = validateConfirmPassword;
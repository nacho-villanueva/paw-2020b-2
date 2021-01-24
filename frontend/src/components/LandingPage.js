import {Form} from "react-bootstrap";
import {useState} from "react";
import "./../css/homepage.css";

function LandingPage() {
    const title1 = "title1";
    const title2 = "title2";
    const login = "log in!";
    const emailLabel = "Email address";
    const passwordLabel = "Password";
    const rememberMeLabel = "Remember Me";
    const submitLogin = "SUBMIT";
    const invalidEmail = "Please input a correct email";
    const invalidPassword = "Please input a correct password";
    const repeatPasswordLabel = "Please repeat the Password";
    const submitRegister = "SUBMITt";

    const register = "register!";
    const loginLink = "/login";
    const registerLink = "/register";

    const [loginValidated, setLoginValidated] = useState(false);
    const handleLoginSubmit = (event) => {
        const form = event.currentTarget;
        if(form.checkValidity() === false) {
            event.preventDefault();
            event.stopPropagation();
        }

        setLoginValidated(true);
    };

    const [registerValidated, setRegisterValidated] = useState(false);
    const handleRegisterSubmit = (event) => {
        const form = event.currentTarget;
        if(form.checkValidity() === false) {
            event.preventDefault();
            event.stopPropagation();
        }

        setRegisterValidated(true);
    };

    return(
        <div className="card main-card bg-light">
            <div className="card-body">
                <h1 className="text-center text-highlight">{title1}</h1>
                <h4 className="text-center text-highlight">{title2}</h4>
                <hr className="divider my-4"/>
                <ul className="nav nav-pills nav-justified" id="myTab" role="tablist">
                    <li className="nav-item">
                        <a className="nav-item nav-link"
                           id="login-tab" data-toggle="tab" href={loginLink} role="tab"
                           aria-controls="login" aria-selected="false">{login}</a>
                    </li>
                    <li className="nav-item">
                        <a className="nav-link"
                           id="register-tab" data-toggle="tab" href={registerLink}
                           role="tab" aria-controls="register" aria-selected="false">{register}</a>
                    </li>
                </ul>

                <hr className="divider my-4"/>

                <div className="tab-content">
                    <div id="login" className="tab-pane fade in">
                        <Form className="form-signin" noValidate validated={loginValidated} onSubmit={handleLoginSubmit}>
                            <fieldset className="form-group">
                                <label for="loginEmail" className="bmd-label-static">{emailLabel}</label>
                                <input name="loginEmail" type="email" className="form-control" id="loginEmail" placeholder={emailLabel}  />
                            </fieldset>
                            <fieldset className="form-group">
                                <label for="loginPassword" class="bmd-label-static">{passwordLabel}</label>
                                <input name="loginPassword" type="password" class="form-control" id="loginPassword" placeholder={passwordLabel} />
                            </fieldset>

                            <div className="checkbox">
                                <label>
                                    <input name="rememberme" type="checkbox"/>{rememberMeLabel}
                                </label>
                            </div>

                            <div className="row justify-content-center">
                                <input type="submit" class="row btn btn-lg action-btn" value={submitLogin}/>
                            </div>
                        </Form>
                    </div>



                    <div id="register" className="tab-pane fade show active">
                        <Form noValidate validated={registerValidated} className="form-signin" onSubmit={handleRegisterSubmit}>
                            <fieldset className="form-group">
                                <label for="registerEmail" className="bmd-label-static">{emailLabel}</label>
                                    {emailLabel}
                                <input type="email" path="email" cssClass="form-control" cssErrorClass="form-control is-invalid" id="registerEmail" required="required" placeholder={emailLabel} />
                                <Form.Control.Feedback type="invalid">{invalidEmail}</Form.Control.Feedback>
                            </fieldset>

                            <fieldset className="form-group">
                                <label for="registerPassword" className="bmd-label-static">{passwordLabel}</label>
                                    {passwordLabel}
                                <input type="password" path="passwordField.password" cssClass="form-control" cssErrorClass="form-control is-invalid" id="registerPassword" required="required" placeholder={passwordLabel} />
                                <Form.Control.Feedback type="invalid">{invalidPassword}</Form.Control.Feedback>
                            </fieldset>
                            <fieldset className="form-group">
                                <label for="registerPasswordConfirm" className="bmd-label-static">{repeatPasswordLabel}</label>
                                    {repeatPasswordLabel}
                                <input path="passwordField.confirmPassword" type="password" cssClass="form-control" cssErrorClass="form-control is-invalid" required="required" id="registerPasswordConfirm" placeholder={repeatPasswordLabel} />
                                <Form.Control.Feedback type="invalid">{invalidPassword}</Form.Control.Feedback>
                            </fieldset>
                            <div className="row justify-content-center">
                                <input type="submit" className="row btn btn-lg action-btn" value={submitRegister} />
                            </div>
                        </Form>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default LandingPage;
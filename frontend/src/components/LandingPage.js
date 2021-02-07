import NavBar from "./NavBar.js";
import {Form} from "react-bootstrap";
import "./Style/LandingPage.css";
import {useState} from "react";
import {useHistory} from "react-router-dom";

import {authenticate} from "../redux/actions";
import {useDispatch} from "react-redux";
import {registerUser, login} from "../api/Auth";

function LandingPage() {
    //strings
    const title1 = "Welcome to";
    const title2 = "MedTransfer";
    const loginButton = "log in!";
    const emailLabel = "Email address";
    const passwordLabel = "Password";
    const rememberMeLabel = "Remember Me";
    const submitLogin = "Log In";
    const invalidEmail = "Please input a correct email";
    const invalidPassword = "Please input a correct password";
    const repeatPasswordLabel = "Please repeat the Password";
    const submitRegister = "Register";

    const register = "register!";
    const loginLink = "/login";
    const registerLink = "/register";

    const history = useHistory();

    const dispatch = useDispatch();

    //////////////////////////////////////////////////////////
    //form validation

    const [loginValidated, setLoginValidated] = useState(false);
    const handleLoginSubmit = (event) => {
        event.preventDefault();
        const form = event.currentTarget;

        const inputs = {
            email: form[0],
            password: form[1]
        }

        if(form.checkValidity() === false) {
            event.stopPropagation();
        }else{
            //          ideal/future code
            //let data = {email: event.target[0], password: event.target[1]};
            //let return = Wrapper.Authenticate(data);
            //dispatch(authenticate(return.token, return.role));
            // dispatch(authenticate("T3ST0k3N", "Medic"));
            login(inputs.email.value, inputs.password.value);
        }

        setLoginValidated(true);
    };

    const [registerValidated, setRegisterValidated] = useState(false);
    const handleRegisterSubmit = (event) => {
        event.preventDefault();

        const form = event.currentTarget;
        let formInputs = event.target;
        //the first values in the object formInputs are the fields from the form, then its useless stuff
        const inputs = {email: formInputs[0],
                        password: formInputs[1],
                        passwordConfirm: formInputs[2]}

        /*
        for(var i in inputs){
            console.log(i, inputs[i].value);
        }
        */

        if(form.checkValidity() === false || inputs.password.value !== inputs.passwordConfirm.value) {
            event.stopPropagation();
        }else{
            registerUser(inputs.email.value, inputs.password.value, inputs.passwordConfirm.value);
        }

        setRegisterValidated(true);
    };

    //////////////////////////////////////////////////////////
    //code to change between the login and register tabs

    const [loginTab, setLoginTab] = useState("tab-pane fade in show active");
    const [registerTab, setRegisterTab] = useState("tab-pane fade in");
    const [activeLoginTab, setActiveLoginTab] = useState("active");
    const [activeRegisterTab, setActiveRegisterTab] = useState("");

    const changeToLogin = (event) => {
        setLoginTab("tab-pane fade in show active");
        setRegisterTab("tab-pane fade in");
        setActiveLoginTab("active");
        setActiveRegisterTab("");
    };
    const changeToRegister = (event) => {
        setRegisterTab("tab-pane fade in show active");
        setLoginTab("tab-pane fade in");
        setActiveRegisterTab("active");
        setActiveLoginTab("");
    };

    return(
        <div className={"landingContainer"}>
            <NavBar />
            <div className="card main-card bg-light">
                <div className="card-body">
                    <h1 className="text-center text-highlight">{title1}</h1>
                    <h4 className="text-center text-highlight">{title2}</h4>
                    <hr className="divider my-4"/>
                    <ul className="nav nav-pills nav-justified" id="myTab" role="tablist">
                        <li className="nav-item">
                            <a className={"nav-item nav-link " + activeLoginTab} href={"/#"}
                               id="login-tab" data-toggle="tab" role="tab" onClick={changeToLogin}
                               aria-controls="login" aria-selected={activeLoginTab === 'active' ? "true" : "false" }>{loginButton}</a>
                        </li>
                        <li className="nav-item">
                            <a className={"nav-link " + activeRegisterTab} href={"/#"}
                               id="register-tab" data-toggle="tab" onClick={changeToRegister}
                               role="tab" aria-controls="register" aria-selected={activeRegisterTab === 'active'? "true" : "false"}>{register}</a>
                        </li>
                    </ul>

                    <hr className="divider my-4"/>

                    <div className="tab-content">
                        <div id="login" className={loginTab}>
                            <Form className="form-signin" noValidate validated={loginValidated} onSubmit={handleLoginSubmit}>
                                <Form.Group controlId="loginEmail">
                                    <Form.Label className="bmd-label-static">{emailLabel}</Form.Label>
                                    <Form.Control required type="email" placeholder="Enter email" name="email"/>
                                    <Form.Control.Feedback type="invalid">{invalidEmail}</Form.Control.Feedback>
                                </Form.Group>
                                <Form.Group controlId="loginPassword">
                                    <Form.Label className="bmd-label-static">{passwordLabel}</Form.Label>
                                    <Form.Control required type="password" placeholder="Password" name="password"/>
                                    <Form.Control.Feedback type="invalid">{invalidPassword}</Form.Control.Feedback>
                                </Form.Group>
                                <Form.Group>
                                    <Form.Check label={rememberMeLabel} name="rememberme"/>
                                </Form.Group>


                                <div className="row justify-content-center">
                                    <input type="submit" className="row btn btn-lg action-btn" value={submitLogin}/>
                                </div>
                            </Form>
                        </div>



                        <div id="register" className={registerTab}>
                            <Form noValidate validated={registerValidated} className="form-signin" onSubmit={handleRegisterSubmit}>
                                <Form.Group controlId="registerEmail">
                                    <Form.Label className="bmd-label-static">{emailLabel}</Form.Label>
                                    <Form.Control required type="email" placeholder="Enter email" name="email"/>
                                    <Form.Control.Feedback type="invalid">{invalidEmail}</Form.Control.Feedback>
                                </Form.Group>
                                <Form.Group controlId="registerPassword">
                                    <Form.Label className="bmd-label-static">{passwordLabel}</Form.Label>
                                    <Form.Control required type="password" placeholder="Password" name="password"/>
                                    <Form.Control.Feedback type="invalid">{invalidPassword}</Form.Control.Feedback>
                                </Form.Group>
                                <Form.Group controlId="registerPasswordConfirm">
                                    <Form.Label className="bmd-label-static">{repeatPasswordLabel}</Form.Label>
                                    <Form.Control required type="password" placeholder="Repeat Password" name="passwordConfirm"/>
                                    <Form.Control.Feedback type="invalid">{invalidPassword}</Form.Control.Feedback>
                                </Form.Group>

                                <div className="row justify-content-center">
                                    <input type="submit" className="row btn btn-lg action-btn" value={submitRegister} />
                                </div>
                            </Form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default LandingPage;
import NavBar from "./NavBar.js";
import {Alert, Button, Form} from "react-bootstrap";
import "./Style/LandingPage.css";
import {useState} from "react";
import {useHistory} from "react-router-dom";
import { Trans, useTranslation } from 'react-i18next'

import {authenticate} from "../redux/actions";
import {useDispatch} from "react-redux";
import {registerUser, login} from "../api/Auth";
import { ERROR_CODES } from "../constants/ErrorCodes"
import InvalidFeedback from "./InvalidFeedback";

function LandingPage() {

    // password length values
    const passwordMin = 8;
    const passwordMax = 100;
    
    const loginLink = "/login";
    const registerLink = "/register";

    const { t } = useTranslation();

    const history = useHistory();

    const dispatch = useDispatch();

    //////////////////////////////////////////////////////////
    //form validation

    const [errors, setErrors] = useState([]);
    const [statusCode, setStatusCode] = useState(0);

    const [loginValidated, setLoginValidated] = useState(false);
    const handleLoginSubmit = (event) => {
        event.preventDefault();
        const form = event.currentTarget;

        const inputs = {
            email: form[0],
            password: form[1],
            rememberMe: form[2],
        }

        if(form.checkValidity() === false) {
            event.stopPropagation();
        }else{
            login(inputs.email.value, inputs.password.value, inputs.rememberMe.checked, setStatusCode, setErrors);
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

        if(form.checkValidity() === false) {
            event.stopPropagation();
        }else if(inputs.password.value !== inputs.passwordConfirm.value){
            event.stopPropagation();
            setStatusCode(400);
            setErrors([{property: "passwordConfirm",code:"invalid"}])
        }else{
            registerUser(inputs.email.value, inputs.password.value, setStatusCode, setErrors);
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
                    <Trans t={t} i18nKey="home.title">
                        <h1 className="text-center text-highlight">Welcome to</h1><h4 className="text-center text-highlight">MedTransfer</h4>
                    </Trans>
                    <hr className="divider my-4"/>
                    <ul className="nav nav-pills nav-justified" id="myTab" role="tablist">
                        <li className="nav-item">
                            <a className={"nav-item nav-link " + activeLoginTab} href={"/#"}
                               id="login-tab" data-toggle="tab" role="tab" onClick={changeToLogin}
                               aria-controls="login" aria-selected={activeLoginTab === 'active' ? "true" : "false" }>
                                   <Trans t={t} i18nKey="home.tabs.login.title"/>
                            </a>
                        </li>
                        <li className="nav-item">
                            <a className={"nav-link " + activeRegisterTab} href={"/#"}
                               id="register-tab" data-toggle="tab" onClick={changeToRegister}
                               role="tab" aria-controls="register" aria-selected={activeRegisterTab === 'active'? "true" : "false"}>
                                   <Trans t={t} i18nKey="home.tabs.register.title"/>
                            </a>
                        </li>
                    </ul>

                    <hr className="divider my-4"/>

                    <div className="tab-content">
                        <div id="login" className={loginTab}>
                            <Form className="form-signin" noValidate validated={loginValidated} onSubmit={handleLoginSubmit}>
                                <Form.Group controlId="loginEmail">
                                    <Form.Label className="bmd-label-static"><Trans t={t} i18nKey="home.tabs.login.form.email.label"/></Form.Label>
                                    <Form.Control required type="email" placeholder={t("home.tabs.login.form.email.placeholder")} name="email"/>
                                    <Form.Control.Feedback type="invalid"><Trans t={t} i18nKey="home.tabs.login.form.email.errors.invalid"/></Form.Control.Feedback>
                                    <InvalidFeedback
                                        condition={statusCode===400 && errors.filter(e => {return e.code === ERROR_CODES.MISSING_FIELD && e.property === "email"}).length>0} 
                                        message={t("home.tabs.login.form.email.errors.missing-field")}
                                    />
                                    <InvalidFeedback
                                        condition={statusCode===400 && errors.filter(e => {return e.code === ERROR_CODES.INVALID && e.property === "email"}).length>0} 
                                        message={t("home.tabs.login.form.email.errors.invalid",{ min: passwordMin, max: passwordMax})}
                                    />
                                </Form.Group>
                                <Form.Group controlId="loginPassword">
                                    <Form.Label className="bmd-label-static"><Trans t={t} i18nKey="home.tabs.login.form.password.label"/></Form.Label>
                                    <Form.Control required type="password" placeholder={t("home.tabs.login.form.password.placeholder")} name="password" minLength={passwordMin} maxLength={passwordMax}/>
                                    <Form.Control.Feedback type="invalid"><Trans t={t} i18nKey="home.tabs.login.form.password.errors.invalid" values={{ min: passwordMin, max: passwordMax}}/></Form.Control.Feedback>
                                    <InvalidFeedback
                                        condition={statusCode===400 && errors.filter(e => {return e.code === ERROR_CODES.MISSING_FIELD && e.property === "password"}).length>0} 
                                        message={t("home.tabs.login.form.password.errors.missing-field")}
                                    />
                                    <InvalidFeedback
                                        condition={statusCode===400 && errors.filter(e => {return e.code === ERROR_CODES.INVALID && e.property === "password"}).length>0} 
                                        message={t("home.tabs.login.form.password.errors.invalid",{ min: passwordMin, max: passwordMax})}
                                    />
                                </Form.Group>
                                <Form.Group>
                                    <Form.Check label={t("home.tabs.login.form.remember-me.label")} name="rememberme"/>
                                </Form.Group>

                                <Alert show={statusCode === 401 && "true"} variant="warning">
                                    <div className="d-flex justify-content-between">
                                    <span className="my-2">
                                        <Trans t={t} i18nKey="home.tabs.login.form.errors.unauthorized.message" />
                                    </span>
                                    <Button
                                        variant="outline-warning"
                                        onClick={() => {setStatusCode(0)}}
                                    >
                                        <Trans t={t} i18nKey="home.tabs.login.form.errors.unauthorized.close-alert" />
                                    </Button>
                                    </div>
                                </Alert>


                                <div className="row justify-content-center">
                                    <input type="submit" className="row btn btn-lg action-btn" value={t("home.tabs.login.form.submit.value")}/>
                                </div>
                            </Form>
                        </div>



                        <div id="register" className={registerTab}>
                            <Form noValidate validated={registerValidated} className="form-signin" onSubmit={handleRegisterSubmit}>
                                <Form.Group controlId="registerEmail">
                                    <Form.Label className="bmd-label-static"><Trans t={t} i18nKey="home.tabs.register.form.email.label"/></Form.Label>
                                    <Form.Control required type="email" placeholder={t("home.tabs.register.form.email.placeholder")} name="email"/>
                                    <Form.Control.Feedback type="invalid"><Trans t={t} i18nKey="home.tabs.register.form.email.errors.invalid"/></Form.Control.Feedback>
                                    <InvalidFeedback
                                        condition={statusCode===400 && errors.filter(e => {return e.code === ERROR_CODES.ALREADY_EXISTS && e.property === "email"}).length>0} 
                                        message={t("home.tabs.register.form.email.errors.already-exists")}
                                    />
                                    <InvalidFeedback
                                        condition={statusCode===400 && errors.filter(e => {return e.code === ERROR_CODES.MISSING_FIELD && e.property === "email"}).length>0} 
                                        message={t("home.tabs.register.form.email.errors.missing-field")}
                                    />
                                    <InvalidFeedback
                                        condition={statusCode===400 && errors.filter(e => {return e.code === ERROR_CODES.INVALID && e.property === "email"}).length>0} 
                                        message={t("home.tabs.register.form.email.errors.invalid",{ min: passwordMin, max: passwordMax})}
                                    />
                                </Form.Group>
                                <Form.Group controlId="registerPassword">
                                    <Form.Label className="bmd-label-static"><Trans t={t} i18nKey="home.tabs.register.form.password.label"/></Form.Label>
                                    <Form.Control required type="password" placeholder={t("home.tabs.register.form.password.placeholder")} name="password" minLength={passwordMin} maxLength={passwordMax}/>
                                    <Form.Control.Feedback type="invalid"><Trans t={t} i18nKey="home.tabs.register.form.password.errors.invalid" values={{ min: passwordMin, max: passwordMax}}/></Form.Control.Feedback>
                                    <InvalidFeedback
                                        condition={statusCode===400 && errors.filter(e => {return e.code === ERROR_CODES.MISSING_FIELD && e.property === "password"}).length>0} 
                                        message={t("home.tabs.register.form.password.errors.missing-field")}
                                    />
                                    <InvalidFeedback
                                        condition={statusCode===400 && errors.filter(e => {return e.code === ERROR_CODES.INVALID && e.property === "password"}).length>0} 
                                        message={t("home.tabs.register.form.password.errors.invalid",{ min: passwordMin, max: passwordMax})}
                                    />
                                </Form.Group>
                                <Form.Group controlId="registerPasswordConfirm">
                                    <Form.Label className="bmd-label-static"><Trans t={t} i18nKey="home.tabs.register.form.repeat-password.label"/></Form.Label>
                                    <Form.Control required type="password" placeholder={t("home.tabs.register.form.repeat-password.placeholder")} name="passwordConfirm"/>
                                    <Form.Control.Feedback type="invalid"><Trans t={t} i18nKey="home.tabs.register.form.repeat-password.errors.invalid"></Trans></Form.Control.Feedback>
                                    <InvalidFeedback
                                        condition={statusCode===400 && errors.filter(e => {return e.code === ERROR_CODES.INVALID && e.property === "passwordConfirm"}).length>0} 
                                        message={t("home.tabs.register.form.repeat-password.errors.invalid")}
                                    />
                                </Form.Group>

                                <div className="row justify-content-center">
                                    <input type="submit" className="row btn btn-lg action-btn" value={t("home.tabs.register.form.submit.value")} />
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
import NavBar from "./NavBar.js";
import {Alert, Button, Form} from "react-bootstrap";
import "./Style/LandingPage.css";
import {useEffect, useState} from "react";
import {useHistory, useLocation} from "react-router-dom";
import { Trans, useTranslation } from 'react-i18next'

import {useDispatch} from "react-redux";
import {registerUser, login, validateToken} from "../api/Auth";
import {ERROR_CODES, HTTP_CODES} from "../constants/ErrorCodes"
import InvalidFeedback from "./InvalidFeedback";
import Loader from "react-loader-spinner";
import ErrorFeedback from "./inputs/ErrorFeedback";

function useQuery() {
    return new URLSearchParams(useLocation().search);
}

function LandingPage(props) {

    const [tokenValidated, setTokenValidated] = useState(0)
    const query = useQuery();

    useEffect(() => {
        let checkedToken = false
        if(props.tokenValidation === true && !checkedToken) {
            let token = query.get("token")
            console.log(token)
            validateToken(token).then((r) => {console.log(r); setTokenValidated(r)})
            return () => {checkedToken = true}
        }
    }, [])



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
    const [isLoading, setLoading] = useState(false);
    const [invalidLogin, setInvalidLogin] = useState(false);

    const [loginValidated, setLoginValidated] = useState(false);
    const onLoginSuccess = () => {
        setLoginValidated(true);
        setLoading(false)
    }

    const onLoginFail = () => {
        setInvalidLogin(true)
        setLoginValidated(false);
        setLoading(false)
        setIsRegistered(false)
    }


    const handleLoginSubmit = (event) => {
        event.preventDefault();
        const form = event.currentTarget;

        const inputs = {
            email: form[0].value,
            password: form[1].value,
            rememberMe: form[2].checked,
        }

        if(form.checkValidity() === false) {
            event.stopPropagation();
            setInvalidLogin(true)
        }else{
            login(inputs.email, inputs.password, inputs.rememberMe, onLoginSuccess, onLoginFail);
            setLoading(true);
        }
    };

    const [isRegistered, setIsRegistered] = useState(false);
    const onRegisterSuccess = () => {
        setRegisterValidated(true);
        setLoading(false)
        changeToLogin()
        setIsRegistered(true)
    }

    const [registerErrors, setRegisterErrors] = useState({email:false, password:false, repeat: false});
    const onRegisterFail = (errors) => {
        setRegisterErrors({email: errors.email, password: errors.password, repeat: false})
        setRegisterValidated(false);
        setLoading(false)
    }

    const [registerValidated, setRegisterValidated] = useState(false);
    const handleRegisterSubmit = (event) => {
        event.preventDefault();

        const form = event.currentTarget;
        let formInputs = event.target;
        //the first values in the object formInputs are the fields from the form, then its useless stuff
        const inputs = {email: formInputs[0].value,
            password: formInputs[1].value,
            passwordConfirm: formInputs[2].value
        }

        if(form.checkValidity() === false || inputs.password !== inputs.passwordConfirm) {
            event.stopPropagation();

            let e = {email:false, password:false, repeat: false};

            if(inputs.email === "")
                e.email = ERROR_CODES.INVALID
            if(passwordMin > inputs.password.length || inputs.password.length > passwordMax)
                e.password = ERROR_CODES.INVALID
            if(inputs.password !== inputs.passwordConfirm)
                e.repeat = ERROR_CODES.INVALID

            setRegisterErrors(e)
        }else{
            registerUser(inputs.email, inputs.password, onRegisterSuccess, onRegisterFail);
            setLoading(true);
        }
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
                            <a className={"nav-item nav-link " + activeLoginTab}
                               id="login-tab" data-toggle="tab" role="tab" onClick={changeToLogin}
                               aria-controls="login" aria-selected={activeLoginTab === 'active' ? "true" : "false" }>
                                   <Trans t={t} i18nKey="home.tabs.login.title"/>
                            </a>
                        </li>
                        <li className="nav-item">
                            <a className={"nav-item nav-link " + activeRegisterTab} 
                               id="register-tab" data-toggle="tab" onClick={changeToRegister}
                               role="tab" aria-controls="register" aria-selected={activeRegisterTab === 'active'? "true" : "false"}>
                                   <Trans t={t} i18nKey="home.tabs.register.title"/>
                            </a>
                        </li>
                    </ul>

                    <hr className="divider my-4"/>

                    <div className="tab-content">
                        <div id="login" className={loginTab}>
                            <Alert show={invalidLogin} variant={'danger'}><Trans t={t} i18nKey="home.tabs.login.form.errors.unauthorized.message" /></Alert> {/*TODO*/}
                            <Alert show={isRegistered} variant={'success'}><Trans t={t} i18nKey="home.tabs.register.form.success" /></Alert>
                            <Alert show={tokenValidated === HTTP_CODES.ACCEPTED} variant={'success'}><Trans t={t} i18nKey="home.tabs.login.validation.success" /></Alert>
                            <Alert show={tokenValidated === HTTP_CODES.NOT_FOUND} variant={'warning'}><Trans t={t} i18nKey="home.tabs.login.validation.error" /></Alert>
                            <Form className="form-signin" noValidate validated={loginValidated} onSubmit={handleLoginSubmit}>
                                <Form.Group controlId="loginEmail">
                                    <Form.Label className="bmd-label-static"><Trans t={t} i18nKey="home.tabs.login.form.email.label"/></Form.Label>
                                    <Form.Control required type="email" placeholder={t("home.tabs.login.form.email.placeholder")} name="email" isInvalid={registerErrors.email}/>
                                </Form.Group>
                                <Form.Group controlId="loginPassword">
                                    <Form.Label className="bmd-label-static"><Trans t={t} i18nKey="home.tabs.login.form.password.label"/></Form.Label>
                                    <Form.Control required type="password" placeholder={t("home.tabs.login.form.password.placeholder")} name="password" minLength={passwordMin} maxLength={passwordMax}/>
                                </Form.Group>
                                <Form.Group controlId="loginRememberMe">
                                    <Form.Check className={"muted"} label={t("home.tabs.login.form.remember-me.label")} name="rememberme"/>
                                </Form.Group>

                                <div className="row justify-content-center">
                                    {!isLoading &&
                                    <input type="submit" className="row btn btn-lg action-btn" value={t("home.tabs.login.form.submit.value")}/>}
                                    {isLoading &&
                                    <Loader
                                        className="row btn btn-lg action-btn loader-button"
                                        type="ThreeDots"
                                        color="#FFFFFF"
                                        height={"25"}
                                    />}
                                </div>
                            </Form>
                        </div>


                        <div id="register" className={registerTab}>
                            <Form noValidate validated={registerValidated} className="form-signin" onSubmit={handleRegisterSubmit}>
                                <Form.Group controlId="registerEmail">
                                    <Form.Label className="bmd-label-static"><Trans t={t} i18nKey="home.tabs.register.form.email.label"/></Form.Label>
                                    <Form.Control required type="email" placeholder={t("home.tabs.register.form.email.placeholder")} name="email"/>
                                    <ErrorFeedback isInvalid={registerErrors.email === ERROR_CODES.INVALID}><Trans t={t} i18nKey="home.tabs.register.form.email.errors.invalid"/></ErrorFeedback>
                                    <ErrorFeedback isInvalid={registerErrors.email === ERROR_CODES.ALREADY_EXISTS}><Trans t={t} i18nKey="home.tabs.register.form.email.errors.already-exists"/></ErrorFeedback>
                                </Form.Group>
                                <Form.Group controlId="registerPassword">
                                    <Form.Label className="bmd-label-static"><Trans t={t} i18nKey="home.tabs.register.form.password.label"/></Form.Label>
                                    <Form.Control required type="password" placeholder={t("home.tabs.register.form.password.placeholder")} name="password" minLength={passwordMin} maxLength={passwordMax}/>
                                    <ErrorFeedback isInvalid={registerErrors.password === ERROR_CODES.INVALID}><Trans t={t} i18nKey="home.tabs.register.form.password.errors.invalid" values={{ min: passwordMin, max: passwordMax}}/></ErrorFeedback>
                                </Form.Group>
                                <Form.Group controlId="registerPasswordConfirm">
                                    <Form.Label className="bmd-label-static"><Trans t={t} i18nKey="home.tabs.register.form.repeat-password.label"/></Form.Label>
                                    <Form.Control required type="password" placeholder={t("home.tabs.register.form.repeat-password.placeholder")} name="passwordConfirm"/>
                                    <Form.Control.Feedback type="invalid"><Trans t={t} i18nKey="home.tabs.register.form.repeat-password.errors.invalid"/></Form.Control.Feedback>
                                    <ErrorFeedback isInvalid={registerErrors.repeat === ERROR_CODES.INVALID}><Trans t={t} i18nKey="home.tabs.register.form.repeat-password.errors.invalid"/></ErrorFeedback>
                                </Form.Group>

                                <div className="row justify-content-center">
                                    {!isLoading && <input type="submit" className="row btn btn-lg action-btn" value={t("home.tabs.register.form.submit.value")} />}
                                    {isLoading &&
                                    <Loader
                                        className="row btn btn-lg action-btn loader-button"
                                        type="ThreeDots"
                                        color="#FFFFFF"
                                        height={"25"}
                                    />}
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
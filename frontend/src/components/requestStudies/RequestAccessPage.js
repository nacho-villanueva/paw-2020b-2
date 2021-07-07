import {Alert, Button, Card, Form} from "react-bootstrap";
import "./Style/RequestAccessPage.css"
import {Trans, useTranslation} from "react-i18next";
import {useEffect, useState} from "react";
import ErrorFeedback from "../inputs/ErrorFeedback";
import {Typeahead} from "react-bootstrap-typeahead";
import Loader from "react-loader-spinner";
import {getAllStudyTypes} from "../../api/CustomFields";
import {RequestAccessToOrder} from "../../api/OrderRequest";
import {ERROR_CODES, HTTP_CODES} from "../../constants/ErrorCodes";

const RequestAccessPage = () => {

    const {t} = useTranslation()

    const [success, setSuccess] = useState(false)
    const [repeated, setRepeated] = useState(false)
    const [loading, setLoading] = useState(false)
    const [errors, setErrors] = useState({email: false, study: false})

    const [studyTypesOptions, setStudyTypesOptions] = useState([])

    useEffect(() => {
        let loaded = false
        if(!loaded) {
            getAllStudyTypes(setStudyTypesOptions)
        }
        return () => {loaded = true}
    }, [])

    const handleSubmit = (event) => {


        event.preventDefault();
        event.stopPropagation();

        setSuccess(false)
        setRepeated(false)

        let formInputs = event.target;

        let input = {
            email: formInputs[0].value,
            medicStudy: formInputs[1].value
        }

        let e = {email: false, study: false};

        let ms = studyTypesOptions.filter((x) => x.name === input.medicStudy)
        if(ms.length !== 1)
            e.study = ERROR_CODES.INVALID;

        let re = /\S+@\S+\.\S+/;
        if (input.email === "" || !re.test(input.email))
            e.email = ERROR_CODES.INVALID;
        setErrors(e)

        if(!e.email && !e.study){
            setLoading(true)
            RequestAccessToOrder(input.email, ms[0]).then((r) => {
                if(r === HTTP_CODES.OK){
                    setSuccess(true);
                } else if (r === HTTP_CODES.NOT_MODIFIED) {
                    setRepeated(true)
                }
                else {
                    setErrors(r);
                }
                setLoading(false)
            })
        }
    }

    return (
        <div className={"accessContentContainer"}>
            <Card className={"accessCard"}>
                <h4 className={"cardTitle"}><Trans t={t} i18nKey={"request-order.title"}/> </h4>
                <hr className={"divider"}/>
                <Alert show={success} variant={"success"}><Trans t={t} i18nKey={"request-order.alerts.success"}/></Alert>
                <Alert show={repeated} variant={"primary"}><Trans t={t} i18nKey={"request-order.alerts.exists"}/></Alert>
                <Form className={""} noValidate onSubmit={handleSubmit}>
                    <Form.Group className="form-group col mt-1" controlId="clinicName">
                        <Form.Label className="bmd-label-static"><Trans t={t} i18nKey={"request-order.email.label"}/></Form.Label>
                            <Form.Control
                                type="text" className={"clinicName"}
                                name="lastName" placeholder={t("request-order.email.label")}
                            />
                        <ErrorFeedback isInvalid={errors.email === ERROR_CODES.INVALID}><Trans t={t} i18nKey={"request-order.email.error.invalid"}/></ErrorFeedback>
                        <ErrorFeedback isInvalid={errors.email === ERROR_CODES.OTHER}><Trans t={t} i18nKey={"request-order.email.error.non-exists"}/></ErrorFeedback>
                    </Form.Group>

                    <Form.Group className="form-group col" controlId="clinicStudyTypes">
                        <Form.Label className="bmd-label-static"><Trans t={t} i18nKey={"request-order.study.label"}/></Form.Label>
                        <Typeahead
                            options={Array.from(studyTypesOptions,(x) => x.name)}
                            highlightOnlyResult={true}
                            placeholder={t("request-order.study.label")}
                            id={"studyTypes"}
                        />
                        <ErrorFeedback isInvalid={errors.study === ERROR_CODES.INVALID}><Trans t={t} i18nKey={"request-order.study.error"}/></ErrorFeedback>
                    </Form.Group>

                    {!loading &&
                    <Button className= "create-btn registrationSubmitButton"
                            type="submit" name="accessSubmit"><Trans t={t} i18nKey={"request-order.send-button"}/></Button>}

                    {loading &&
                    <Loader
                        className= "create-btn registrationSubmitButton loading-button"
                        type="ThreeDots"
                        color="#FFFFFF"
                        height={"25"}/>}
                </Form>
            </Card>
        </div>
    )

}

export default RequestAccessPage;
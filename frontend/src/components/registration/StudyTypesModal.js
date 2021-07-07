import {Button, Form, Modal} from "react-bootstrap";
import {useState} from "react";
import {createStudyType} from "../../api/CustomFields";
import {Trans, useTranslation} from "react-i18next";

const StudyTypesModal = (props) => {

    const {t} = useTranslation()

    const [field, setField] = useState("");

    const onSuccess = () => {
        setField("");
        props.callbackSuccess(field);
        props.setShow(false);
    }

    const onFail = () => {
        setField("");
        props.callbackFail(field);
        setField("");
        props.setShow(false);
    }


    const handleClose = () => {setField(""); props.setShow(false);}
    const handleSubmit = () => {createStudyType(field, onSuccess, onFail);};

    return <Modal show={props.show} onHide={handleClose}>
        <Modal.Header closeButton>
            <Modal.Title><Trans t={t} i18nKey={"registration.modals.study-types.title"}/></Modal.Title>
        </Modal.Header>
        <Modal.Body>
            <Form onSubmit={(e)=> {e.preventDefault(); e.stopPropagation(); handleSubmit(); }}>
                <Form.Group className="form-group col mt-1" controlId="medicalField">
                    <Form.Label className="bmd-label-static"><Trans t={t} i18nKey={"registration.modals.study-types.label"}/></Form.Label>
                    <Form.Control
                        type="text" className={"registrationInput"}
                        name="medicalField" placeholder={t("registration.modals.study-types.placeholder")}
                        value={field} onChange={(e)=> {setField(e.target.value)}}
                    />
                </Form.Group>
            </Form>
        </Modal.Body>
        <Modal.Footer>
            <Button variant="secondary" onClick={handleClose}>
                <Trans t={t} i18nKey={"registration.modals.return-button"}/>
            </Button>
            <Button variant="primary" onClick={handleSubmit}>
                <Trans t={t} i18nKey={"registration.modals.submit-button"}/>
            </Button>
        </Modal.Footer>
    </Modal>
}

export default StudyTypesModal;

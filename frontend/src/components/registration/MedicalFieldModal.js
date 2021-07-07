import {Button, Form, Modal} from "react-bootstrap";
import {useState} from "react";
import {createMedicalField} from "../../api/CustomFields";
import {Trans, useTranslation} from "react-i18next";

const MedicalFieldModal = (props) => {

    const {t} = useTranslation()

    const [field, setField] = useState("");

    const onSuccess = () => {
        setField("");
        props.callbackSuccess(field);
        props.setShow(false);
    }

    const onFail = () => {
        props.callbackFail(field);
        setField("");
        props.setShow(false);
    }


    const handleClose = () => {setField(""); props.setShow(false);}
    const handleSubmit = () => {createMedicalField(field, onSuccess, onFail);};

    return <Modal show={props.show} onHide={handleClose}>
        <Modal.Header closeButton>
            <Modal.Title><Trans t={t} i18nKey={"registration.modals.medical-fields.title"}/></Modal.Title>
        </Modal.Header>
        <Modal.Body>
            <Form onSubmit={(e)=> {e.preventDefault(); e.stopPropagation(); handleSubmit(); }}>
                <Form.Group className="form-group col mt-1" controlId="medicalField">
                    <Form.Label className="bmd-label-static"><Trans t={t} i18nKey={"registration.modals.medical-fields.label"}/></Form.Label>
                    <Form.Control
                        type="text" className={"registrationInput"}
                        name="medicalField" placeholder={t("registration.modals.medical-fields.placeholder")}
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

export default MedicalFieldModal;

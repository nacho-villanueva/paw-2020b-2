import {Button, Form, Modal} from "react-bootstrap";
import {useState} from "react";
import {createInsurancePlan} from "../../api/CustomFields";

const InsurancePlanModal = (props) => {

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
    const handleSubmit = () => {createInsurancePlan(field, onSuccess, onFail);};

    return <Modal show={props.show} onHide={handleClose}>
        <Modal.Header closeButton>
            <Modal.Title>Add Insurance Plan</Modal.Title>
        </Modal.Header>
        <Modal.Body>
            <Form onSubmit={(e)=> {e.preventDefault(); e.stopPropagation(); handleSubmit(); }}>
                <Form.Group className="form-group col mt-1" controlId="medicalField">
                    <Form.Label className="bmd-label-static">Insurance Plan</Form.Label>
                    <Form.Control
                        type="text" className={"registrationInput"}
                        name="medicalField" placeholder={"Insert Insurance Plan"}
                        value={field} onChange={(e)=> {setField(e.target.value)}}
                    />
                </Form.Group>
            </Form>
        </Modal.Body>
        <Modal.Footer>
            <Button variant="secondary" onClick={handleClose}>
                Return
            </Button>
            <Button variant="primary" onClick={handleSubmit}>
                Create
            </Button>
        </Modal.Footer>
    </Modal>
}

export default InsurancePlanModal;

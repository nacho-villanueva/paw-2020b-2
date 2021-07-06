import { Modal, Form, Button } from "react-bootstrap";

export function UploadResults(props){
    const show = props.show;
    const setShow = props.setShow;
    const handleUploadResults = props.handleUploadResults;
    const uploadValidated = props.uploadValidated;
    const orderInfo = props.orderInfo;

    const handleClose = () => {
        setShow(false);
    };

    return(
        <Modal show={show} onHide={handleClose}>
            <Modal.Header closeButton>
                <Modal.Title>Upload Results</Modal.Title>
            </Modal.Header>
            <Form noValidate validated={uploadValidated} onSubmit={handleUploadResults}>
                <Modal.Body>
                    <hr/>
                    <div className="row w-100 justify-content-center mx-2">
                        <div className="col type">
                            <p className="type-title">Order Number</p>
                            {orderInfo.id}
                        </div>
                        <div className="col type">
                            <p className="type-title">Patient</p>
                            {orderInfo.patientName}
                        </div>
                        <div class="w-100"></div>
                        <div className="col type">
                            <p className="type-title">Patient insurance plan</p>
                            {/*orderInfo.medicPlan.name*/}
                        </div>
                        <div className="col type">
                            <p className="type-title">Patient insurance number</p>
                            {/*orderInfo.medicPlan.number*/}
                        </div>
                    </div>
                    <hr/>
                    <Form.Group>
                        <Form.File label="Choose the files to upload"
                            name="fileUpload" accept="image/png, image/jpeg"
                            multiple={true} required={true}
                        />
                        <Form.Text muted>
                            Only pick results in an image format (jpeg, png, etc.)
                        </Form.Text>
                    </Form.Group>
                    <hr/>
                    <div className="row">
                        <div className="col">
                            <Form.Group>
                                <Form.Label className="bmd-label-static">Responsible Medic</Form.Label>
                                <Form.Control
                                    name="responsibleMedicName"
                                    placeholder="Dr. Jane Doe"
                                    required={true}
                                />
                                <Form.Control.Feedback type="invalid">Only use letters or numbers</Form.Control.Feedback>
                            </Form.Group>
                        </div>
                        <div className="col">
                            <Form.Group>
                                <Form.Label className="bmd-label-static">Responsible Medic's Licence Number</Form.Label>
                                <Form.Control
                                    name="responsibleMedicLicenceNumber"
                                    placeholder="0000000001"
                                    pattern="[a-zA-Z0-9]+"
                                    required={true}
                                />
                            </Form.Group>
                        </div>
                    </div>
                    <hr/>
                    <Form.Group>
                        <Form.File label="Upload signature" required={true}
                            name="signatureUpload" accept="image/png, image/jpeg"/>
                        <Form.Text muted>
                            Upload a picture of the responsible's signature
                        </Form.Text>
                    </Form.Group>

                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleClose}>Cancel</Button>
                    <Button variant="primary" type="submit">Upload Results</Button>
                </Modal.Footer>
            </Form>

        </Modal>
    );
}
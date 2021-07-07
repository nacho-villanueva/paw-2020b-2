import { Modal, Form, Button } from "react-bootstrap";
import ErrorFeedback from "../inputs/ErrorFeedback";

import { Trans, useTranslation } from 'react-i18next'


function UploadResults(props){
    const {t} = useTranslation();

    const show = props.show;
    const setShow = props.setShow;
    const handleUploadResults = props.handleUploadResults;
    const uploadValidated = props.uploadValidated;
    const orderInfo = props.orderInfo;
    const uploadErrors = props.uploadErrors;

    const handleClose = () => {
        setShow(false);
    };

    return(
        <Modal show={show} onHide={handleClose}>
            <Modal.Header closeButton>
                <Modal.Title>
                    <Trans t={t} i18nKey="view-order.upload.title"/>
                </Modal.Title>
            </Modal.Header>
            <Form noValidate validated={uploadValidated} onSubmit={handleUploadResults}>
                <Modal.Body>
                    <hr/>
                    <div className="row w-100 justify-content-center mx-2">
                        <div className="col type">
                            <p className="type-title"><Trans t={t} i18nKey="view-order.upload.order-number"/></p>
                            {orderInfo.id}
                        </div>
                        <div className="col type">
                            <p className="type-title"><Trans t={t} i18nKey="view-order.upload.patient"/></p>
                            {orderInfo.patientName}
                        </div>
                        <div class="w-100"></div>
                        <div className="col type">
                            <p className="type-title"><Trans t={t} i18nKey="view-order.upload.insurance-plan"/></p>
                            {/*orderInfo.medicPlan.name*/}
                        </div>
                        <div className="col type">
                            <p className="type-title"><Trans t={t} i18nKey="view-order.upload.insurance-number"/></p>
                            {/*orderInfo.medicPlan.number*/}
                        </div>
                    </div>
                    <hr/>
                    <Form.Group>
                        <Form.File label={t("view-order.upload.file-upload.label")}
                            name="fileUpload" accept="image/png, image/jpeg"
                            multiple={true} required={true}
                        />
                        <Form.Text muted>
                            {t("view-order.upload.file-upload.text")}
                        </Form.Text>
                        <ErrorFeedback isInvalid={uploadErrors.fileUpload}><Trans t={t} i18nKey="view-order.upload.file-upload.error"/></ErrorFeedback>
                    </Form.Group>
                    <hr/>
                    <div className="row">
                        <div className="col">
                            <Form.Group>
                                <Form.Label className="bmd-label-static"><Trans t={t} i18nKey="view-order.upload.responsible-name.label"/></Form.Label>
                                <Form.Control
                                    name="responsibleMedicName"
                                    placeholder="Dr. Jane Doe"
                                    required={true}
                                />
                                <ErrorFeedback isInvalid={uploadErrors.responsibleMedicName}><Trans t={t} i18nKey="view-order.upload.responsible-name.error"/></ErrorFeedback>
                            </Form.Group>
                        </div>
                        <div className="col">
                            <Form.Group>
                                <Form.Label className="bmd-label-static"><Trans t={t} i18nKey="view-order.upload.responsible-number.label"/></Form.Label>
                                <Form.Control
                                    name="responsibleMedicLicenceNumber"
                                    placeholder="0000000001"
                                    pattern="[a-zA-Z0-9]+"
                                    required={true}
                                />
                                <ErrorFeedback isInvalid={uploadErrors.responsibleMedicLicenceNumber}><Trans t={t} i18nKey="view-order.upload.responsible-number.error"/></ErrorFeedback>
                            </Form.Group>
                        </div>
                    </div>
                    <hr/>
                    <Form.Group>
                        <Form.File label={t("view-order.upload.responsible-id.label")} required={true}
                            name="signatureUpload" accept="image/png, image/jpeg"/>
                        <Form.Text muted>
                            {t("view-order.upload.responsible-id.text")}
                        </Form.Text>
                        <ErrorFeedback isInvalid={uploadErrors.signatureUpload}><Trans t={t} i18nKey="view-order.upload.responsible-id.error"/></ErrorFeedback>
                    </Form.Group>

                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleClose}>{t("view-order.upload.actions.cancel")}</Button>
                    <Button variant="primary" type="submit">{t("view-order.upload.actions.submit")}</Button>
                </Modal.Footer>
            </Form>

        </Modal>
    );
}

export default UploadResults;
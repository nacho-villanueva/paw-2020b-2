import { useTranslation, Trans} from "react-i18next/*";
import {useState} from "react";

import {Typeahead, AsyncTypeahead} from "react-bootstrap-typeahead";

import { Modal, Button, ErrorFeedback} from "react-bootstrap";

import {ERROR_CODES} from "../../constants/ErrorCodes";

import {ShareOrderWithMedic} from "../../api/Orders";




function ShareWithMedic(props){
    const {t} = useTranslation();

    const show = props.show;
    const setShow = props.setShow;
    const orderId = props.orderId;

    const [medicsList, setMedicsList] = useState([]);

    const [modifiedValues, setModifiedValues] = useState({medicName: ''})
    const [validate, setValidate] = useState(false);
    const [errors, setErrors] = useState(0);

    const handleShare = (event) => {
        event.preventDefault();
        let medicName = null;
        if(modifiedValues.medicName !== null && modifiedValues.medicName.length > 0){
            let medicId = -1;
            for(let m of medicsList){
                if(m.name === medicName){
                    medicId = m.userId;
                }
            }
            if(medicId === -1){
                setErrors(ERROR_CODES.INVALID);
            }else{
                ShareOrderWithMedic(orderId, medicId)
                    .then((r) => {
                        if(r !== true){
                            setErrors(r);
                        }else{
                            setSuccess(true);
                        }
                    })
            }
        }

    }


    const handleClose= () => { setShow(false); };

    return(
        <Modal show={show} onHide={handleClose}>
            <Modal.Header closeButton>
                <Modal.Title>
                    Share with medic
                </Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Typeahead
                    options={Array.from(medicsList,(x) => x.name)}
                    highlightOnlyResult={true}
                    placeholder={t("view-order.share-with-medic.label")}
                    id={"medicName"}
                    onInputChange={(val)=> {setModifiedValues({...modifiedValues, medicName: val})}}
                    onChange={(val)=> {setModifiedValues({...modifiedValues, medicName: val[0]})}}
                    required
                />
                <ErrorFeedback isInvalid={errors === ERROR_CODES.INVALID}><Trans t={t} i18nKey="view-order.share-with-medic.error"/></ErrorFeedback>

            </Modal.Body>
            <Modal.Footer>
                    <Button variant="secondary" onClick={handleClose}>{t("view-order.share-with-medic.cancel")}</Button>
                    <Button variant="primary" onClick={(e) => {handleShare(e)}}>{t("view-order.share-with-medic.submit")}</Button>
            </Modal.Footer>
        </Modal>
    );
}
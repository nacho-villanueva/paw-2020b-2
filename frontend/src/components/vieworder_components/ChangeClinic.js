import {Modal, Button} from "react-bootstrap";
import {ClinicsFilters} from "../search_clinic/ClinicsFilters";
import {ClinicsResults} from "../search_clinic/ClinicsResults";

export function ChangeClinic(props){
    const show = props.show;
    const setShow = props.setShow;
    const orderInfo = props.orderInfo;

    const handleClose = () => {
        setShow(false);
    }

    const handleFiltersSubmit = (event) => {
        console.log(event);
    }

    return(
        <Modal show={show} onHide={handleClose}>
            <Modal.Header closeButton>
                <Modal.Title>Change Clinic</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <div className="row-custom justify-content-center mt-2">
                    <ClinicsResults/>
                    <ClinicsFilters
                        handleFiltersSubmit={handleFiltersSubmit}
                    />
                </div>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={handleClose}>Cancel</Button>
                <Button variant="primary" type="submit">Change Clinic</Button>
            </Modal.Footer>
        </Modal>
    );
}
import {Form, Button} from "react-bootstrap";
import {Roles} from "../../constants/Roles";

export function MyOrdersFilters(props){
    let filtersValidated = props.filtersValidated;
    let handleFiltersSubmit = props.handleFiltersSubmit;
    let searchFilters = props.searchFilters;
    let studyTypesList = props.studyTypesList;
    let clinicsList = props.clinicsList;
    let role = props.role

    return(
        <Form noValidate validated={filtersValidated} onSubmit={handleFiltersSubmit}>
            <Form.Group>
                <Form.Label className="bmd-label-static">Study type</Form.Label>
                <Form.Control
                    as="select"
                    name="studyTypeSelect"
                    placeholder="Any study type"
                    defaultValue={searchFilters.studyType}
                >
                    <option value={-1}>Any study type</option>
                    {studyTypesList.map((item, index) => (
                        <option value={item.id}
                            key={"study-"+index+"-"+item.id}
                            defaultValue={item.id === searchFilters.studyType}
                        >{item.name}</option>
                    ))}
                </Form.Control>
            </Form.Group>
            <Form.Group>
                <Form.Label className="bmd-label-static">Clinic</Form.Label>
                <Form.Control
                    as="select"
                    name="clinicSelect"
                    placeholder="Any clinic"
                    defaultValue={searchFilters.clinicIDs}
                >
                    <option value={-1}>Any clinic</option>
                    {clinicsList.map((item, index) => (
                        <option value={item.id}
                            key={"clinic-"+index+"-"+item.id}
                            defaultValue={item.id === searchFilters.clinicIDs}
                        >{item.name}</option>
                    ))}
                </Form.Control>
            </Form.Group>
            { role === Roles.CLINIC || role === Roles.MEDIC ?
                <Form.Group>
                    <Form.Label className="bmd-label-floating">Patient's email</Form.Label>
                    <Form.Control
                        type="email"
                        name="patientEmailInput"
                        defaultValue={searchFilters.patientEmail}
                    ></Form.Control>
                </Form.Group>
                :
                <></>
            }
            <Form.Group>
                <Form.Label className="bmd-label-static">From</Form.Label>
                <Form.Control
                    type="date" className="form-control date-input"
                    name="from-datePick"
                    defaultValue={searchFilters.fromTime}
                />
            </Form.Group>
            <Form.Group>
                <Form.Label className="bmd-label-static">To</Form.Label>
                <Form.Control
                    type="date" className="form-control date-input"
                    name="to-datePick"
                    defaultValue={searchFilters.toTime}
                />
            </Form.Group>
            <Button
                className="search-btn mx-auto"
                type="submit"
            >Search</Button>
        </Form>
    );
}
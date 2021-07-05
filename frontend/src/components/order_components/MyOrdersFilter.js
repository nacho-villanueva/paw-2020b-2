import {Form, Button} from "react-bootstrap";
import {Roles} from "../../constants/Roles";

export function MyOrdersFilters(props){
    let filtersValidated = props.filtersValidated;
    let handleFiltersSubmit = props.handleFiltersSubmit;
    let searchFilters = props.searchFilters;
    let studyTypesList = props.studyTypesList;
    let clinicsList = props.clinicsList;
    let role = props.role
    let medicsList = props.medicsList;
    let resetSearchFilters = props.resetSearchFilters;

    return(
        <Form noValidate validated={filtersValidated} onSubmit={handleFiltersSubmit}>
            <Form.Group>
                <Form.Label className="bmd-label-static">Study type</Form.Label>
                <Form.Control
                    as="select" className="select-picker"
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

            { role !== Roles.CLINIC ?
                <Form.Group>
                    <Form.Label className="bmd-label-static">Clinic</Form.Label>
                    <Form.Control
                        as="select" className="select-picker"
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
                : <></>
            }

            { role !== Roles.MEDIC ?
                <Form.Group>
                    <Form.Label className="bmd-label-static">Medic</Form.Label>
                    <Form.Control
                        as="select" className="select-picker"
                        name="medicSelect"
                        placeholder="Any medic"
                        defaultValue={searchFilters.medicIDs}
                    >
                        <option value={-1}>Any medic</option>
                        {medicsList.map((item, index) => (
                            <option value={item.id}
                                key={"medic-"+index+"-"+item.id}
                                defaultValue={item.id === searchFilters.clinicIDs}
                            >{item.name}</option>
                        ))}
                    </Form.Control>
                </Form.Group>
                : <></>
            }

            { role !== Roles.PATIENT ?
                <Form.Group>
                    <Form.Label className="bmd-label-static">Patient's email</Form.Label>
                    <Form.Control
                        type="email" className="select-input"
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
                    type="date" className="form-control date-input select-date"
                    name="from-datePick"
                    defaultValue={searchFilters.fromTime}
                />
            </Form.Group>
            <Form.Group>
                <Form.Label className="bmd-label-static">To</Form.Label>
                <Form.Control
                    type="date" className="form-control date-input select-date"
                    name="to-datePick"
                    defaultValue={searchFilters.toTime}
                />
            </Form.Group>
            <div className="row justify-content-center mt-5">
                <Button
                    className="search-btn mx-auto"
                    type="submit"
                >Search</Button>
            </div>
            <div className="row justify-content-center">
                <Button
                    className="reset-btn row btn-outline-secondary"
                    type="reset" onClick={() => {resetSearchFilters();}}
                >Reset Filters</Button>
            </div>
        </Form>
    );
}
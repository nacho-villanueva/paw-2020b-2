import {Form, Button} from "react-bootstrap";

export function MyOrdersFilters(props){
    let filtersValidated = props.filtersValidated;
    let handleFiltersSubmit = props.handleFiltersSubmit;
    let searchFilters = props.searchFilters;
    let studyTypesList = props.studyTypesList;
    let clinicsList = props.clinicsList;

    return(
        <Form noValidate validated={filtersValidated} onSubmit={handleFiltersSubmit}>
            <Form.Group>
                <Form.Label className="bmd-label-static">Study type</Form.Label>
                <Form.Control
                    as="select"
                    name="studyType"
                    placeholder="Any study type"
                >
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
                    name="clinic"
                    placeholder="Any clinic"
                >
                    {clinicsList.map((item, index) => (
                        <option value={item.id}
                            key={"clinic-"+index+"-"+item.id}
                            defaultValue={item.id === searchFilters.clinicIDs}
                        >{item.name}</option>
                    ))}
                </Form.Control>
            </Form.Group>
            <Form.Group>
                <Form.Label className="bmd-label-floating">Patient's email</Form.Label>
                <Form.Control
                    type="email"
                    name="patientEmail"
                    defaultValue={searchFilters.patientEmail}
                ></Form.Control>
            </Form.Group>
            <Form.Group>
                <Form.Label className="bmd-label-static">From</Form.Label>
                <Form.Control
                    type="date" className="form-control date-input"
                    name="from-date"
                    defaultValue={searchFilters.fromTime}
                />
            </Form.Group>
            <Form.Group>
                <Form.Label className="bmd-label-static">To</Form.Label>
                <Form.Control
                    type="date" className="form-control date-input"
                    name="to-date"
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
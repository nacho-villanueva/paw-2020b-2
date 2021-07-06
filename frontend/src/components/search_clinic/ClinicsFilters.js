import { Form, Button, Collapse, Table } from "react-bootstrap";
import {useState} from "react";
import { Trans, useTranslation } from 'react-i18next'


import {Day} from "./Day";

export function ClinicsFilters(props){
    const [insurancePlans, setInsurancePlans] = useState([]);
    const [studyTypesList, setStudyTypesList] = useState([]);

    const { t } = useTranslation();

    const daysOfTheWeek = [
        {name:"Sunday", id: 0},
        {name:"Monday", id: 1},
        {name:"Tuesday", id: 2},
        {name:"Wednesday", id: 3},
        {name:"Thursday", id: 4},
        {name:"Friday", id: 5},
        {name:"Saturday", id: 6}
    ];

    //what we have to send out
    const sendableFilters = {
        clinicName: '',
        insurancePlan: '',
        studyType: '',
        days: new Array(7),
        fromTime: new Array(7),
        toTime: new Array(7)
    };

    //used in the FORM
    const defaultInputs = {
        clinicName: '',
        insurancePlan: '',
        studyType: '',
        schedule: [
            {day:0, checked: false, fromTime: '', toTime: ''},
            {day:1, checked: false, fromTime: '', toTime: ''},
            {day:2, checked: false, fromTime: '', toTime: ''},
            {day:3, checked: false, fromTime: '', toTime: ''},
            {day:4, checked: false, fromTime: '', toTime: ''},
            {day:5, checked: false, fromTime: '', toTime: ''},
            {day:6, checked: false, fromTime: '', toTime: ''}
        ]
    };
    const [filters, setFilters] = useState(defaultInputs);

    const [filtersValidation, setFiltersValidation] = useState(false);
    const [showSchedule, setShowSchedule] = useState(false);

    const handleFiltersSubmit = props.handleFiltersSubmit;

    return(
        <div className="card cl-filters bg-light float-left">
            <div className="card-body">
                <div className="row">
                    <div className="col">
                        <p className="card-title h4">Filters</p>
                    </div>
                </div>
                <hr/>
                <Form noValidate validated={filtersValidation} onSubmit={handleFiltersSubmit}>
                    <Form.Group>
                        <Form.Label className="bmd-label-static">
                            <Trans t={t} i18nKey="advanced-search-clinics.form.clinic-name.label" />
                        </Form.Label>
                        <Form.Control
                            type="text" className="select-input"
                            name="clinicName"
                            defaultValue={filters.clinicName}
                        />
                    </Form.Group>
                    <Form.Group>
                        <Form.Label className="bmd-label-static">
                            <Trans t={t} i18nKey="advanced-search-clinics.form.insurance-plan.label" />
                        </Form.Label>
                        <Form.Control
                            className="select-picker"
                            as="select" name="insurancePlanSelect"
                            defaultValue={filters.insurancePlan}
                        >
                            <option value={-1}>Any insurance plan</option>
                            {
                                insurancePlans.map((item) => (
                                    <option
                                        value={item.id}
                                        defaultValue={item.id === filters.insurancePlan}
                                    >{item.name}</option>
                                ))
                            }
                        </Form.Control>
                    </Form.Group>
                    <Form.Group>
                        <Form.Label className="bmd-label-static">
                            <Trans t={t} i18nKey="advanced-search-clinics.form.study-type.label" />
                        </Form.Label>
                        <Form.Control
                            className="select-picker"
                            as="select" name="studyPlanSelect"
                            defaultValue={filters.studyType}
                        >
                            <option value={-1}>Any study type</option>
                            {
                                studyTypesList.map((item) => (
                                    <option
                                        value={item.id}
                                        defaultValue={item.id === filters.studyType}
                                    >{item.name}</option>
                                ))
                            }
                        </Form.Control>
                    </Form.Group>

                    <div className="row mx-1">
                        <Button
                            className="clinic-btn mx-auto"
                            variant="primary"
                            onClick={() => {setShowSchedule(!showSchedule);}}
                        >
                            {t("advanced-search-clinics.form.available-hours.buttons.show")}
                            {showSchedule===false? <i className="fas fa-chevron-down ml-2"/> : <i className="fas fa-chevron-up ml-2"/>}
                        </Button>
                        <Button
                            className="clinic-btn search-btn mx-auto"
                            type="submit" name="clinicSearchSubmit"
                            value="advanced-search-clinics.form.submit.value"
                        >
                            <Trans t={t} i18nKey="advanced-search-clinics.form.submit.value" />
                        </Button>
                    </div>


                    <Collapse in={showSchedule}>
                        <div className="w-100">
                            <Table
                                striped bordered hover
                                className="custom-table"
                            >
                                <thead>
                                    <tr>
                                        <th><Trans t={t} i18nKey="advanced-search-clinics.form.available-hours.columns.day" /></th>
                                        <th><Trans t={t} i18nKey="advanced-search-clinics.form.available-hours.columns.available" /></th>
                                        <th><Trans t={t} i18nKey="advanced-search-clinics.form.available-hours.columns.from-time" /></th>
                                        <th><Trans t={t} i18nKey="advanced-search-clinics.form.available-hours.columns.to-time" /></th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {daysOfTheWeek.map((item) => (
                                        <Day item={item} filters={filters}/>
                                    ))}
                                </tbody>
                            </Table>
                        </div>
                    </Collapse>
                </Form>
            </div>
        </div>
    );
}
import { Form, Button, Collapse, Table } from "react-bootstrap";
import {useState} from "react";
import { Trans, useTranslation } from 'react-i18next'

import "../Style/SearchClinics.css";
import {Day} from "./Day";

export function ClinicsFilters(props){
    const inputs = props.inputs;
    const setInputs = props.setInputs;

    const handleFiltersSubmit = props.handleFiltersSubmit;
    const insurancePlans = props.insurancePlans;
    const studyTypesList = props.studyTypesList;
    const filtersValidation = props.filterValidated;
    const daysOfTheWeek = props.daysOfTheWeek;

    const { t } = useTranslation();
    const [showSchedule, setShowSchedule] = useState(false);


    return(
        <div className="card cl-filters bg-light float-left">
            <div className="card-body">
                <div className="row">
                    <div className="col">
                        <p className="card-title h4">
                        <Trans t={t} i18nKey="advanced-search-clinics.title-filters" />
                        </p>
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
                            defaultValue={inputs.clinicName}
                            onChange={(val) => {setInputs({...inputs, clinicName: val.target.value})}}
                        />
                    </Form.Group>
                    <Form.Group>
                        <Form.Label className="bmd-label-static">
                            <Trans t={t} i18nKey="advanced-search-clinics.form.insurance-plan.label" />
                        </Form.Label>
                        <Form.Control
                            className="select-picker"
                            as="select" name="insurancePlanSelect"
                            placeholder={t("advanced-search-clinics.form.insurance-plan.any")}
                            defaultValue={inputs.insurancePlan}
                            onChange={(val) => {setInputs({...inputs, insurancePlan: val.target.value})}}
                        >
                            <option value={-1}>
                                {t("advanced-search-clinics.form.insurance-plan.any")}
                            </option>
                            {
                                insurancePlans.map((item,idx) => (
                                    <option
                                        value={item.id} key={"inpl-"+idx}
                                        defaultValue={item.id === inputs.insurancePlan}
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
                            defaultValue={inputs.studyType}
                            placeholder={t("advanced-search-clinics.form.study-type.any")}
                            onChange={(val) => {setInputs({...inputs, studyType: val.target.value})}}
                        >
                            <option value={-1}>
                                {t("advanced-search-clinics.form.study-type.any")}
                            </option>
                            {
                                studyTypesList.map((item,idx) => (
                                    <option
                                        value={item.id} key={"stty-"+idx}
                                        defaultValue={item.id === inputs.studyType}
                                    >{item.name}</option>
                                ))
                            }
                        </Form.Control>
                    </Form.Group>
                    <div className="row justify-content-center mb-3">
                        <Button
                            className="cl-search-btn mx-auto"
                            onClick={(e)=> {
                                e.preventDefault();
                                handleFiltersSubmit();
                            }}
                        >
                            <Trans t={t} i18nKey="advanced-search-clinics.form.submit.value" />
                        </Button>
                    </div>

                    <div className="row mx-1">
                        <Button
                            className="cl-schedule-btn mx-auto"
                            variant="secondary"
                            onClick={() => {setShowSchedule(!showSchedule);}}
                        >
                            {t("advanced-search-clinics.form.available-hours.buttons.show")}
                            {showSchedule===false? <i className="fas fa-chevron-down ml-2"/> : <i className="fas fa-chevron-up ml-2"/>}
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
                                    {daysOfTheWeek.map((item,idx) => (
                                        <Day item={item} filters={inputs} key={"di-"+idx} setFilters={setInputs}/>
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
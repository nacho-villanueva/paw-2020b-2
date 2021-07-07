import {Form, Button} from "react-bootstrap";
import {Roles} from "../../constants/Roles";
import { Trans, useTranslation } from 'react-i18next'

export function MyOrdersFilters(props){
    let filtersValidated = props.filtersValidated;
    let handleFiltersSubmit = props.handleFiltersSubmit;
    let searchFilters = props.searchFilters;
    let studyTypesList = props.studyTypesList;
    let clinicsList = props.clinicsList;
    let role = props.role
    let medicsList = props.medicsList;
    let resetSearchFilters = props.resetSearchFilters;

    const { t } = useTranslation();

    return(
        <Form noValidate validated={filtersValidated} onSubmit={handleFiltersSubmit}>
            <Form.Group>
                <Form.Label className="bmd-label-static"><Trans t={t} i18nKey="my-orders.filters.study-type.label"/></Form.Label>
                <Form.Control
                    as="select" className="select-picker"
                    name="studyTypeSelect"
                    placeholder={t("my-orders.filters.study-type.placeholder")}
                    defaultValue={searchFilters.studyType}
                >
                    <option value={-1}>{t("my-orders.filters.study-type.placeholder")}</option>
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
                    <Form.Label className="bmd-label-static"><Trans t={t} i18nKey="my-orders.filters.clinic.label"/></Form.Label>
                    <Form.Control
                        as="select" className="select-picker"
                        name="clinicSelect"
                        placeholder={t("my-orders.filters.clinic.placeholder")}
                        defaultValue={searchFilters.clinicIDs}
                    >
                        <option value={-1}>{t("my-orders.filters.clinic.placeholder")}</option>
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
                    <Form.Label className="bmd-label-static"><Trans t={t} i18nKey="my-orders.filters.medic.label"/></Form.Label>
                    <Form.Control
                        as="select" className="select-picker"
                        name="medicSelect"
                        placeholder={t("my-orders.filters.medic.placeholder")}
                        defaultValue={searchFilters.medicIDs}
                    >
                        <option value={-1}>{t("my-orders.filters.medic.placeholder")}</option>
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
                    <Form.Label className="bmd-label-static"><Trans t={t} i18nKey="my-orders.filters.patient-email.label"/></Form.Label>
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
                <Form.Label className="bmd-label-static"><Trans t={t} i18nKey="my-orders.filters.from-time.label"/></Form.Label>
                <Form.Control
                    type="date" className="form-control date-input select-date"
                    name="from-datePick"
                    defaultValue={searchFilters.fromTime}
                />
            </Form.Group>
            <Form.Group>
                <Form.Label className="bmd-label-static"><Trans t={t} i18nKey="my-orders.filters.to-time.label"/></Form.Label>
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
                ><Trans t={t} i18nKey="my-orders.filters.buttons.search"/></Button>
            </div>
            <div className="row justify-content-center">
                <Button
                    className="reset-btn row btn-outline-secondary"
                    type="reset" onClick={() => {resetSearchFilters();}}
                ><Trans t={t} i18nKey="my-orders.filters.buttons.reset"/></Button>
            </div>
        </Form>
    );
}
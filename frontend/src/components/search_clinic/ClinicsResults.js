import { useState } from "react";
import { Form, Button, Table } from "react-bootstrap";
import { Trans, useTranslation } from 'react-i18next'

import { PaginationComponent } from "../order_components/PaginationComponent";

export function ClinicsResults(props){
    const results = props.results;
    const currentPage = props.currentPage;
    const totalClinicPages = props.totalClinicPages;
    const changePageAndFetch = props.changePageAndFetch;
    const selectedClinic = props.selectedClinic;
    const chooseClinic = props.chooseClinic;

    const [update, setUpdate] = useState(0);

    const {t} = useTranslation();


    const NoClinicResultsFound = () => {
        return(
            <section>
                <div className="align-items-end result-not">
                    <h1 className="text-center mt-5 py-5">No Results Found</h1>
                </div>
            </section>
        );
    }

    const ClinicInfo = (props) => {
        const id = props.item.userId;
        return(
            <div className="tab-pane tab-result" key={"cl-info-"+id}>
                <h3>{props.item.name}</h3>
                <Table className="table table-borderless">
                    <tbody>
                        <tr>
                            <td><Trans t={t} i18nKey="advanced-search-clinics.clinic-info.email"/></td>
                            <td className="output">{props.item.email}</td>
                        </tr>
                        <tr>
                            <td><Trans t={t} i18nKey="advanced-search-clinics.clinic-info.telephone"/></td>
                            <td className="output">{props.item.telephone}</td>
                        </tr>
                        <tr>
                            <td><Trans t={t} i18nKey="advanced-search-clinics.clinic-info.open-hours"/></td>
                            <td>
                                {props.item.hours.map((piano) => (
                                    <div key={"oh_"+id+"_"+piano.day}>
                                        <span>{t('days.day-'+piano.day)}</span>&nbsp;&nbsp;&nbsp;
                                        <span>{piano.openTime + " - " + piano.closeTime}</span>
                                    </div>
                                ))}
                            </td>
                        </tr>
                        <tr>
                            <td><Trans t={t} i18nKey="advanced-search-clinics.clinic-info.insurance"/></td>
                            <td className="output">
                                {props.item.acceptedPlans.map((pico) => (
                                    <span
                                        key={"plan_"+ id +"_"+pico.id}
                                        className="badge-sm badge-pill badge-secondary mr-1 d-inline-block"
                                    >{pico.plan}</span>

                                ))}
                            </td>
                        </tr>
                        <tr>
                            <td><Trans t={t} i18nKey="advanced-search-clinics.clinic-info.studies"/></td>
                            <td className="output">
                                {props.item.medicalStudies.map((study) => (
                                    <p key={"study_"+ id +"_"+study.name}>{study.name}</p>
                                ))}
                            </td>
                        </tr>
                    </tbody>
                </Table>
            </div>
        );
    }

    const Item = (props) => {
        return(
            <li className="nav-item" key={props.clinic.name}>
                <Button
                    id={"cl-item-"+props.clinic.userId}
                    onClick={() => {chooseClinic(props.clinic);}}
                    className="list-group-item list-group-item-action"
                    data-toggle="tab" role="tab"
                >
                    <div className="justify-content-between">
                        <h5 className="mb-1">{props.clinic.name}</h5>
                    </div>
                </Button>
            </li>
        );
    }

    const ClinicResultsViewer = () => {
        return(
            <div className="row">
                <div className="d-flex flex-row">
                    <div id="cl-results" className="list-group res-section">
                        <ul className="nav flex-column" id="myTab" role="tablist">
                            {
                                results.map((item) => (
                                    <Item key={item.userId} clinic={item}/>
                                ))
                            }
                        </ul>
                        {totalClinicPages >= 2 ?
                            <PaginationComponent
                                active={currentPage}
                                total={totalClinicPages}
                                changePageAndFetch={changePageAndFetch}
                                update={update}
                            />
                            :
                            <></>
                        }
                    </div>
                    <div id="cl-data" className="cl-data-section">
                        <h5 class="text-muted">
                            <Trans t={t} i18nKey="advanced-search-clinics.form.results.selected" />
                        </h5>
                        <div className="">
                            {selectedClinic === null ?
                                <h4><Trans t={t} i18nKey="advanced-search-clinics.form.results.no-selected" /></h4>
                                :
                                <ClinicInfo key={"selected_" + selectedClinic.userId} item={selectedClinic}/>}
                        </div>
                    </div>
                </div>
            </div>
        );
    }

    return(
        <div className="card cl-results bg-light float-right">
            <div className="card-body" key={"results-key-"+update}>
                <div className="row">
                    <div className="col">
                        <p className="card-title h4">ResultsHHHHH</p>
                    </div>
                </div>
                <hr/>
                {results.length === 0 ?
                    <NoClinicResultsFound/>
                    :
                    <ClinicResultsViewer/>
                }
            </div>
        </div>
    );
}
import {Button, Card, Col, Pagination, Row} from "react-bootstrap";
import "./Style/DisplayAccessRequests.css"
import {useEffect, useState} from "react";
import {AcceptAccessRequest, DenyAccessRequest, GetShareRequests} from "../../api/OrderRequest";
import Loader from "react-loader-spinner";
import {GetUserByURL, GetUserInfo} from "../../api/UserInfo";
import {Trans, useTranslation} from "react-i18next";

const RequestItem = (props) => {

    const [isLoading, setIsLoading] = useState(false)

    const {t} = useTranslation()

    function handleAccept(){
        setIsLoading(true)
        props.acceptRequest(props.value).then(()=>{setIsLoading(false)})
    }



    return (
        <Card className={"itemCard"}>
            <Row>
                <Col className={"labelCol"}>
                    <p className={"studyLabel"}><Trans t={t} i18nKey={"display-requests.study"}/> {props.value.studyType.name}</p>
                    <div className={"break"} />
                    <p className={"medicLabel"}><Trans t={t} i18nKey={"display-requests.medic"}/> {props.value.medic.name}</p>
                </Col>
                <Col className={"buttonsCol"}>
                    {!isLoading && (
                        <div>
                        <Button onClick={handleAccept} variant={"primary"}><Trans t={t} i18nKey={"display-requests.accept"}/> </Button>
                        <Button onClick={() => {props.denyRequest(props.value.medic, this.props.value.studyType)}} variant={"secondary"}><Trans t={t} i18nKey={"display-requests.deny"}/> </Button>
                        </div>)}
                    {isLoading &&
                    <Loader
                        type="ThreeDots"
                        color={"var(--primary)"}
                        height={"15"}/>}
                </Col>
            </Row>
        </Card>
    )
}


const DisplayAccessRequestsPage = () => {

    const {t} = useTranslation()

    const [requests, setRequests] = useState([])
    const [pageCount, setPageCount] = useState(1)
    const [currentPage, setCurrentPage] = useState(1)
    const [loading, setLoading] = useState(false)

    const per_page = 5;

    useEffect(() => {
        let loaded = false
        if(!loaded) {
            setLoading(true);
            GetShareRequests(1, per_page).then((r)=>{setLoading(false); setRequests(r.value); setPageCount(r.pageCount)})
        }
        return () => {loaded = true}
    }, [])

    const updatePage = (page) => {
        setCurrentPage(page);
        setRequests([])
        setLoading(true)
        GetShareRequests(page, per_page).then((r)=>{setLoading(false); setRequests(r.value); setPageCount(r.pageCount)})
    }

    async function handleAccept (value) {
        let medicUser = await GetUserByURL(value.medic.user);
        await AcceptAccessRequest(medicUser.id, value.studyType.id);
        await GetShareRequests(currentPage, per_page).then((r)=>{setRequests(r.value); setPageCount(r.pageCount)})
        return true;
    }
    async function handleDeny (value) {
        await DenyAccessRequest(value.medic.id, value.studyType.id);
        await GetShareRequests(currentPage, per_page).then((r)=>{setRequests(r.value); setPageCount(r.pageCount)})
        return true;
    }

    let pages = []
    for(let i = 0; i < pageCount; i++)
        pages.push(<Pagination.Item key={i} active={currentPage === i+1} onClick={() => {updatePage(i+1)}}>{i+1}</Pagination.Item>)

    return (
    <div className={"accessContentContainer"}>
        <Card className={"accessCard"}>
            <h3 className={"noRequests"}><Trans t={t} i18nKey={"display-requests.title"}/> </h3>
            <hr className={"divider"}/>
            {!loading && pageCount === 0 && <h5 className={"noRequests"}><Trans t={t} i18nKey={"display-requests.empty"}/> </h5>}

            {!loading && <div className={"requestsList"}>
                {requests.map((v) => (
                    <RequestItem key={v.studyType.name + v.medic.name} acceptRequest={handleAccept} denyRequest={handleDeny} value={v} />
                ))}
            </div>}

            {loading &&
            <Loader
                className= "loader"
                type="ThreeDots"
                color={"var(--primary)"}
                height={"25"}/>
            }

            {!loading && pageCount > 0 && <Row className={"paginationRow"}>
                <div className={"paginationContainer"}>
                    <p className={"paginationLabel"}><Trans t={t} i18nKey={"display-requests.pages"}/> </p>
                    <Pagination className={"pagination"}>
                        {pages}
                    </Pagination>
                </div>
            </Row>}

        </Card>
    </div>)
}

export default DisplayAccessRequestsPage;
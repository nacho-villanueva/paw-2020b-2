import {Pagination} from "react-bootstrap";

export function PaginationComponent(props){
    let active = props.current;
    let changePageAndFetch = props.changePageAndFetch;
    let total = props.total

        return(
            <div className="row justify-content-center">
                {
                    total === 1 ?
                    <></>
                    :
                    <Pagination>
                        {active -1 > 0 ? <Pagination.Prev onClick={() => changePageAndFetch(active - 1)}/> : <Pagination.Prev disabled/>}
                        <Pagination.Item active>{active}</Pagination.Item>
                        {active + 1 <= total ?  <Pagination.Next onClick={() => changePageAndFetch(active + 1)}/> : <Pagination.Next disabled/>}
                    </Pagination>
                }
            </div>
        );
}
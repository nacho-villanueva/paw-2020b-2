
const ErrorFeedback = (props) => {
    return  <div key={props.isInvalid} style={{width:"100%", marginTop: ".25rem", fontSize:"80%", color:"#f44336"}}>{props.isInvalid === true && props.children}</div>
}

export default ErrorFeedback;

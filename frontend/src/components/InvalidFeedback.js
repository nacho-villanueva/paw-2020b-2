const InvalidFeedback = (props) => {

    const showCondition = props.condition;
    const message = props.message;

    if(!showCondition) return null;

    return(<div className="invalid-feedback" style={{display: "block"}}>{message}</div>);
}

export default InvalidFeedback;
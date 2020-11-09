function addOption(mySelectId,newOptionValue) {

    if(newOptionValue !== ""){
        let newOption = sanitize(newOptionValue);
        $(mySelectId).append('<option value="'+newOption+'" selected="">'+newOption+'</option>');
    }
}
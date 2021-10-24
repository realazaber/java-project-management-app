function sendEmail(params) {
    var tmpParams = {
        from_name:document.getElementById("fromName").value,
        email:document.getElementById("email").value,
        message:document.getElementById("msg").value,
    };

    emailjs.send('service_npenbn3', 'template_z1eb21g',tmpParams)
    .then(function(res){
        console.log("Success");
    })
}
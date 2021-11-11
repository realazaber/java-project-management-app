import React from 'react';
import LightSpeed from 'react-reveal/LightSpeed';

function Contact() {
    return(
        <>
            <svg className="wave" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1440 320"><path fill="#0099ff" fillOpacity="1" d="M0,288L48,272C96,256,192,224,288,197.3C384,171,480,149,576,165.3C672,181,768,235,864,250.7C960,267,1056,245,1152,250.7C1248,256,1344,288,1392,304L1440,320L1440,320L1392,320C1344,320,1248,320,1152,320C1056,320,960,320,864,320C768,320,672,320,576,320C480,320,384,320,288,320C192,320,96,320,48,320L0,320Z"></path></svg>
            
                <div id="contact">
                <LightSpeed>        
                    <form action="https://formspree.io/f/mqkwogwn" method="post">
                    <h2>Contact me</h2>
                    <h3>If you want to send me a message regarding this project please fill in this form.</h3>
                    <input type="text" name="name:" placeholder="Your name" required />
                    <br />
                    <input type="email" name="email" placeholder="Your email" required />
                    <textarea name="Message" cols="25" rows="10" placeholder="Your message" required></textarea>
                    <br />
                    <button type="submit">Submit</button>
                    </form>
                </LightSpeed>   
                </div>
            
            <svg className="wave upsideDown" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1440 320"><path fill="#0099ff" fillOpacity="1" d="M0,128L60,154.7C120,181,240,235,360,229.3C480,224,600,160,720,128C840,96,960,96,1080,90.7C1200,85,1320,75,1380,69.3L1440,64L1440,0L1380,0C1320,0,1200,0,1080,0C960,0,840,0,720,0C600,0,480,0,360,0C240,0,120,0,60,0L0,0Z"></path></svg>
        </>
    );
}

export default Contact;
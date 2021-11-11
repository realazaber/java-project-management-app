import React from 'react';

function Gallery() {
    return(
        <>
              <svg className="wave galleryWave" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1440 320"><path fill="#0099ff" fillOpacity="1" d="M0,288L48,272C96,256,192,224,288,197.3C384,171,480,149,576,165.3C672,181,768,235,864,250.7C960,267,1056,245,1152,250.7C1248,256,1344,288,1392,304L1440,320L1440,320L1392,320C1344,320,1248,320,1152,320C1056,320,960,320,864,320C768,320,672,320,576,320C480,320,384,320,288,320C192,320,96,320,48,320L0,320Z"></path></svg>

            <div id="Gallery">
            Gallery
            <br />
            <div id="galleryView">
                
                <a href="images/screenshots/Register.png">
                    <img className="galleryImage" src="images/screenshots/Register.png" alt="Register" />
                </a>
                
                <a href="images/screenshots/Multiple projects.png">
                    <img className="galleryImage" src="images/screenshots/Multiple projects.png" alt="Multiple Projects" />
                </a>

                <a href="images/screenshots/Edit Task.png">
                    <img className="galleryImage" src="images/screenshots/Edit Task.png" alt="Edit task" />
                </a>

                <a href="images/screenshots/ProfileEdit.png">
                    <img className="galleryImage" src="images/screenshots/ProfileEdit.png" alt="Edit Profile" />
                </a>

                <a href="images/screenshots/Delete confirmation.png">
                    <img className="galleryImage" src="images/screenshots/Delete confirmation.png" alt="Delete" />
                </a>

                <video className="galleryImage" controls src="videos/maindemo.mp4"></video>
            </div>
            </div>
        </>
    );
}

export default Gallery;
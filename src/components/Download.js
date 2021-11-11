import React from 'react';
import Roll from 'react-reveal/Roll';

function Download() {
    return(
        <>
            <svg className="wave" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1440 320"><path fill="#0099ff" fillOpacity="1" d="M0,64L48,101.3C96,139,192,213,288,229.3C384,245,480,203,576,208C672,213,768,267,864,240C960,213,1056,107,1152,80C1248,53,1344,107,1392,133.3L1440,160L1440,0L1392,0C1344,0,1248,0,1152,0C1056,0,960,0,864,0C768,0,672,0,576,0C480,0,384,0,288,0C192,0,96,0,48,0L0,0Z"></path></svg>
            <Roll>
                <div id="Download">
                    <div>
                        <h3>Where can i get this?</h3>
                        <h4>This is available on <a href="https://github.com/therealcoolpup/java-project-management-app" target="_blank" rel="noopener noreferrer">my github</a>.</h4>
                        <h4>It is currently in open beta.</h4>
                    </div>
                </div>
            </Roll>
        </>
    );
}

export default Download;
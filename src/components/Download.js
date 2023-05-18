import React from "react";
import Fade from "react-reveal/Fade";

function Download() {
  return (
    <section>
      <div className="container">
        <Fade>
          <div id="Download">
            <div>
              <h3>Where can i get this?</h3>
              <h4>
                This is available on{" "}
                <a
                  href="https://github.com/therealcoolpup/java-project-management-app"
                  target="_blank"
                  rel="noopener noreferrer"
                >
                  my github
                </a>
                .
              </h4>
              <h4>It is currently in open beta.</h4>
            </div>
          </div>
        </Fade>
      </div>
    </section>
  );
}

export default Download;

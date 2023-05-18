import React from "react";
import Screenshots from "./screenshots.json";
import Slide from "react-reveal/Slide";

function Gallery() {
  return (
    <section id="Gallery">
      <div className="container">
        Gallery
        <br />
        <Slide bottom cascade>
          <div id="galleryView">
            {Screenshots.map((Screenshot, index) => {
              return (
                <a href={Screenshot.img}>
                  <img
                    className="galleryImage"
                    src={Screenshot.img}
                    alt={Screenshot.description}
                  />
                </a>
              );
            })}

            <video
              className="galleryImage"
              controls
              src="videos/maindemo.mp4"
            ></video>
          </div>
        </Slide>
      </div>
    </section>
  );
}

export default Gallery;

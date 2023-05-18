import React from "react";
import Fade from "react-reveal/Fade";

function Contact() {
  return (
    <section id="contact">
      <div className="container">
        <Fade up>
          <form action="https://formspree.io/f/mqkwogwn" method="post">
            <h2>Contact me</h2>
            <h3>
              If you want to send me a message regarding this project please
              fill in this form.
            </h3>
            <input type="text" name="name:" placeholder="Your name" required />
            <br />
            <input
              type="email"
              name="email"
              placeholder="Your email"
              required
            />
            <textarea
              name="Message"
              cols="25"
              rows="10"
              placeholder="Your message"
              required
            ></textarea>
            <br />
            <button type="submit">Submit</button>
          </form>
        </Fade>
      </div>
    </section>
  );
}

export default Contact;

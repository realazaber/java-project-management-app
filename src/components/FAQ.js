import React from 'react';
import Bounce from 'react-reveal/Bounce';

function FAQ() {
    return(
      
        <div id="FAQ">
        
        <Bounce left>
        <div className="left">
          <h3>What is it?:</h3>
          <h4>This is a simple Java application that you can use to keep track of your projects.</h4>
          <h4>You can even have columns in your projects, put tasks in those columns and make checklists in those tasks.</h4>
        </div>
        </Bounce>
        
        <Bounce right>
        <div className="right">
          <h3>Is it secure?</h3>
          <h4>All data is stored on your local machine. You can even look at the code for yourself.</h4>
        </div>
        </Bounce>
    
        <Bounce left>
        <div className="left">
          <h3>What else can I do?:</h3>
          <h4>You can create your own custom profile and more features are always being added.</h4>
        </div>
        </Bounce>
        
      </div>
    );
}

export default FAQ;
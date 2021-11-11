import React from 'react';

import Heading from './components/Heading';
import Nav from './components/Nav';
import FAQ from './components/FAQ';
import Gallery from './components/Gallery/Gallery';
import Download from './components/Download';
import Contact from './components/Contact';

function App() {
  return (
    <>
      <Heading />
      <Nav />
      <FAQ />
      <Gallery />
      <Download />
      <Contact />

    </>
  );
}

export default App;

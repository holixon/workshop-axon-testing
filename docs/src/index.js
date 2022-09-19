import Reveal from 'reveal.js';
// STYLING
import 'reveal.js/css/reveal.css';
import 'reveal.js/lib/css/zenburn.css';
import './styles/theme.scss';

// library used by RevealJS to download dependency plugins (here: markdown and highlighting)
require('headjs/dist/1.0.0/head.min.js');
// required by the RevealJS markdown plugins
window.Reveal = Reveal;

// If the query includes 'print-pdf', include the PDF print sheet
if (window.location.search.match(/print-pdf/gi)) {
  var link = document.createElement('link');
  link.rel = 'stylesheet';
  link.type = 'text/css';
  link.href = 'node_modules/reveal.js/css/print/pdf.css';
  document.getElementsByTagName('head')[0].appendChild(link);
  var linkMore = document.createElement('link');
  linkMore.rel = 'stylesheet';
  linkMore.type = 'text/css';
  linkMore.href = 'styles/pdf.css';
  document.getElementsByTagName('head')[0].appendChild(linkMore);
}

// PLUGINS
// includes the plugins in the build, at the url expected by RevealJS
require('file-loader?name=plugin/markdown/[name].[ext]!reveal.js/plugin/markdown/marked.js');
require('file-loader?name=plugin/markdown/[name].[ext]!reveal.js/plugin/markdown/markdown.js');
require('file-loader?name=plugin/notes/[name].[ext]!reveal.js/plugin/notes/notes.js');
require('file-loader?name=plugin/notes/[name].[ext]!reveal.js/plugin/notes/notes.html');
require('file-loader?name=plugin/zoom-js/[name].[ext]!reveal.js/plugin/zoom-js/zoom.js');
require('file-loader?name=plugin/highlight/[name].[ext]!reveal.js/plugin/highlight/highlight.js');

// loads the markdown content and starts the slideshow
window.document.getElementById('source').innerHTML = require('./slides.md');
// Configure Reveal
// Full list of configuration options available here:
// https://github.com/hakimel/reveal.js#configuration
Reveal.initialize({

  // The "normal" size of the presentation, aspect ratio will be preserved
  // when the presentation is scaled to fit different resolutions. Can be
  // specified using percentage units.

  width: 1280,
  height: 800,

  // Display controls in the bottom right corner
  controls: true,
  // Display a presentation progress bar
  progress: true,
  // Display the page number of the current slide
  slideNumber: false,
  // Push each slide change to the browser history
  history: true,
  // Enable keyboard shortcuts for navigation
  keyboard: true,

  // Vertical centering of slides
  center: true,

  // Enable the slide overview mode
  overview: true,

  // Enables touch navigation on devices with touch input
  touch: true,

  // Loop the presentation
  loop: false,

  // Change the presentation direction to be RTL
  rtl: false,

  // Turns fragments on and off globally
  fragments: true,

  // Flags if the presentation is running in an embedded mode,
  // i.e. contained within a limited portion of the screen
  embedded: false,

  // Number of milliseconds between automatically proceeding to the
  // next slide, disabled when set to 0, this value can be overwritten
  // by using a data-autoslide attribute on your slides
  autoSlide: 0,

  // Stop auto-sliding after user input
  autoSlideStoppable: true,


  // Factor of the display size that should remain empty around the content
  margin: 0.05,

  // Bounds for smallest/largest possible scale to apply to content
  minScale: 0.5,
  maxScale: 400.0,

  theme: Reveal.getQueryHash().theme, // available themes are in /css/theme

  // Transition speed
  transitionSpeed: 'default', // default/fast/slow

  transition: 'slide', // none/fade/slide/convex/concave/zoomnone

  hideAddressBar: true,

  dependencies: [
    // interpret Markdown in <section> elements
    { src: 'plugin/markdown/marked.js', condition: function () { return !!document.querySelector('[data-markdown]'); } },
    { src: 'plugin/markdown/markdown.js', condition: function () { return !!document.querySelector('[data-markdown]'); } },
    // syntax highlight for <code> elements
    { src: 'plugin/highlight/highlight.js', callback: function () { hljs.initHighlightingOnLoad(); } },
    { src: 'plugin/zoom-js/zoom.js', callback: function () { return !!document.body.classList; } },
    { src: 'plugin/notes/notes.js', callback: function () { return !!document.body.classList; } },
  ],
});

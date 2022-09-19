import Reveal from 'reveal.js';
// STYLING
import 'reveal.js/dist/reveal.css';
import 'reveal.js/plugin/highlight/zenburn.css';
import './styles/theme.scss';

// PLUGINS
import RevealMarkdown from 'reveal.js/plugin/markdown/markdown.js'
import RevealNotes from 'reveal.js/plugin/notes/notes.js'
import RevealZoom from 'reveal.js/plugin/zoom/zoom.js'
import RevealSearch from 'reveal.js/plugin/search/search.js'
import RevealHighlight from 'reveal.js/plugin/highlight/highlight.js'

// required by the RevealJS markdown plugins
window.Reveal = Reveal;

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

  theme: Reveal.theme, // available themes are in /css/theme

  // Transition speed
  transitionSpeed: 'default', // default/fast/slow

  transition: 'slide', // none/fade/slide/convex/concave/zoomnone

  hideAddressBar: true,

  plugins: [
    RevealMarkdown,
    RevealNotes,
    RevealZoom,
    RevealHighlight,
    RevealSearch
  ]
});

# Slides

These slides are made with reveal.js + markdown. Check `src/slides.md` for the content.

## Build 

`mvn clean install -Pdocs` 

The resulting static generated index.html is located in `dist` folder.

## Start 

`mvn clean install -Pdocs -Pstart`

This will run the build and watch on files and automatically open the browser to serve http://localhost:9000/.

## Make PDF

`mvn clean install -Pdocs -Pmake-pdf`

Will create a PDF handout in `dist` (currently not having a nice layout).



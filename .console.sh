function mvn.reveal.build() {
    mvn clean install -Pdocs
}

function mvn.reveal.start() {
  mvn clean install -Pdocs -Pstart
}

function mvn.reveal.pdf() {
  mvn clean install -Pdocs -Pmake-pdf
}

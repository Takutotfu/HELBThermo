#!/bin/bash
mvn clean package
java -jar --module-path /usr/share/openjfx/lib --add-modules=javafx.base,javafx.controls,javafx.fxml,javafx.graphics,javafx.media,javafx.swing,javafx.web target/HELBThermo-1.0-SNAPSHOT.jar


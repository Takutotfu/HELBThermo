#!/bin/bash

mvn clean package

if [ $? -eq 0 ]; then
	java -jar --module-path /usr/share/openjfx/lib --add-modules=javafx.base,javafx.controls,javafx.fxml,javafx.graphics,javafx.media,javafx.swing,javafx.web target/HELBThermo-1.0-SNAPSHOT.jar
else
	echo "Error build"
fi

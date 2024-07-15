@echo off

if not exist target\HELBThermo-1.0-SNAPSHOT.jar (
	mvn clean package
)

java -jar --module-path "C:\Program Files\Java\javafx-sdk\lib" --add-modules=javafx.base,javafx.controls,javafx.fxml,javafx.graphics,javafx.media,javafx.swing,javafx.web target\HELBThermo-1.0-SNAPSHOT.jar


pause
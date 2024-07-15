@echo off

set PATH_TO_FX = "C:\path\to\javafx\lib"

if not exist target\HELBThermo-1.0-SNAPSHOT.jar (
	mvn clean package
)

java -jar --module-path "%PATH_TO_FX%" --add-modules=javafx.base,javafx.controls,javafx.fxml,javafx.graphics,javafx.media,javafx.swing,javafx.web target\HELBThermo-1.0-SNAPSHOT.jar

pause

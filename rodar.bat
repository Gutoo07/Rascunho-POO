rmdir /S /Q ".\bin\"
mkdir bin
@REM javac -d ./bin -cp .;./lib/jtds-1.3.1.jar ./src/*.java
javac -d ./bin -cp .;./lib/jtds-1.3.1.jar ./src/model/*.java ./src/control/*.java ./src/dao/*.java ./src/view/*.java
java -cp .;./bin;./lib/jtds-1.3.1.jar view.Main

@REM javac -cp ".\lib\*" -d .\bin .\src\*.java && java -cp ".\lib;.\bin" --add-modules javafx.controls Main

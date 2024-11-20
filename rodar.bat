rmdir /S /Q ".\bin\"
mkdir bin
javac -d ./bin -cp .;./lib/jtds-1.3.1.jar ./src/*.java
java -cp .;./bin;./lib/jtds-1.3.1.jar Main

@REM javac -cp ".\lib\*" -d .\bin .\src\*.java && java -cp ".\lib;.\bin" --add-modules javafx.controls Main

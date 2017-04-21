cls
del *.class
echo "Please Execute this batch with Administrator Priviledge."
xcopy "lib\*.jar" "c:\program files\java\jdk1.8.0_91\jre\lib\ext" /e && ^
javac -J-Duser.language=en -J-Duser.country=US -classpath "lib/*;." -verbose %1.java && ^
java %1

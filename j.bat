cls
PATH=C:\WINDOWS\system32;C:\WINDOWS;C:\WINDOWS\system32\WBEM;c:\Program Files\Microsoft SQL Server\90\Tools\binn\;C:\Program Files\Microsoft SQL Server\80\Tools\Binn\;C:\Program Files\Microsoft SQL Server\90\DTS\Binn\;c:\program files\java\jdk1.8.0_91\bin
del *.class
echo "Please Execute this batch with Administrator Priviledge."
xcopy "lib\*.jar" "c:\program files\java\jdk1.8.0_91\jre\lib\ext" /e && ^
javac -J-Duser.language=en -J-Duser.country=US -classpath "lib/*;." -verbose %1.java && ^
java %1

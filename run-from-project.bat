set BASIC_FOLDER=%~dp0


cd /d %~dp0\swing-gui\target\usage-graph.jar

"%JAVA_HOME%\bin\java" ^
-cp ^
%BASIC_FOLDER%\swing-gui-source-analyse-plugin-script\target\swing-gui-source-analyse-plugin-script-1.0-SNAPSHOT.jar;^
%BASIC_FOLDER%\analyse-script\target\analyse-script-1.0-SNAPSHOT.jar;^
swing-gui\target\usage-graph.jar;^
 -Xmx768m "ru.ezhov.graph.App"

pause

cd /d %~dp0

"%JAVA_HOME%\bin\java" ^
-cp usage-graph.jar;^
lib\collections-generic-4.01-1.0.0.jar;^
lib\colt-1.2.0-1.0.0.jar;^
lib\concurrent-1.3.4-1.0.0.jar;^
lib\j3d-core-1.3.1-1.0.0.jar;^
lib\jung-3d-2.0.1-1.0.0.jar;^
lib\jung-3d-demos-2.0.1-1.0.0.jar;^
lib\jung-algorithms-2.0.1-1.0.0.jar;^
lib\jung-api-2.0.1-1.0.0.jar;^
lib\jung-graph-impl-2.0.1-1.0.0.jar;^
lib\jung-io-2.0.1-1.0.0.jar;^
lib\jung-jai-2.0.1-1.0.0.jar;^
lib\jung-jai-samples-2.0.1-1.0.0.jar;^
lib\jung-samples-2.0.1-1.0.0.jar;^
lib\jung-visualization-2.0.1-1.0.0.jar;^
lib\stax-api-1.0.1-1.0.0.jar;^
lib\vecmath-1.3.1-1.0.0.jar;^
lib\wstx-asl-3.2.6-1.0.0.jar;^
 -Xmx768m "ru.ezhov.graph.App"

pause

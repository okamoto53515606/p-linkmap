native2ascii -encoding SJIS SearchParameter.java build\SearchParameter.java
native2ascii -encoding SJIS Saver.java build\Saver.java
native2ascii -encoding SJIS Painter.java build\Painter.java
native2ascii -encoding SJIS Member.java build\Member.java
native2ascii -encoding SJIS Icon.java build\Icon.java
native2ascii -encoding SJIS ErrorFrame.java build\ErrorFrame.java
native2ascii -encoding SJIS ConnectionThread.java build\ConnectionThread.java
native2ascii -encoding SJIS Button2.java build\Button2.java
native2ascii -encoding SJIS Changer.java build\Changer.java
native2ascii -encoding SJIS Config.java build\Config.java
native2ascii -encoding SJIS PLMApplet.java build\PLMApplet.java



cd build
SET CLASSPATH=%CLASSPATH%;.;


javac -target 1.1 -encoding SJIS PLMApplet.java

rem jar cf plm.2.0.16.jar *.class images sounds
rem jar tvf plm.2.0.16.jar

jar cf plm.2.0.16.jar *.class





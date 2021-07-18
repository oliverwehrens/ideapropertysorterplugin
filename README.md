Extension of the Property Sorter IntelliJ IDEA plugin by Oliver Wehrens,  
that is used internally by Doodle. Forked off @oliverwehrens.  

Additional features
-------------------
* Automatic sorting of properties  
* Proper handling of ResourceBundle Plugin (inserts "\ " instead of just " ")  
* Sort Properties action can now sort multiple files. User selects one or more properties files in Intellij and then 
selects Code > Sort Properties.
  * The result of sorting the properties files is displayed in a table in a dialog window. There is one row displayed
    per file.

To Do
------
Remove duplicates/unused  


How to build
------------
Basically I followed this guide: http://confluence.jetbrains.com/display/IDEADEV/Getting+Started+with+Plugin+Development   
Long story short: install IntelliJ Community edition and get the sources.  
Create a Plugin Project and make sure you have a Java6 SDK as well as the IntelliJ SDK (the source code).
Hack away, Build -> Make project, Build -> Prepare Plugin for Deployment.

How to build with gradle
------------------------
Use task `buildPlugin` to build:

```shell script
./gradlew buildPlugin
```

See: [Getting Started with Gradle](https://plugins.jetbrains.com/docs/intellij/gradle-prerequisites.html) for details.



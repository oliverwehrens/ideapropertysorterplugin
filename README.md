Extension of the Property Sorter IntelliJ IDEA plugin by Oliver Wehrens,  
that is used internally by Doodle.  
Forked off https://github.com/oliverwehrens/ideapropertysorterplugin.  

== Additional features ==
From the original:   
	Automatic sorting of properties  
	Proper handling of ResourceBundle Plugin (inserts "\ " instead of just " ")  
	Todo: turn the Code -> Sort Properties action into sort all properties or/and remove duplicates/unused  


== How to build == 
Basically I followed this guide: http://confluence.jetbrains.com/display/IDEADEV/Getting+Started+with+Plugin+Development   
Long story short: install IntelliJ Community edition and get the sources.  
Create a Plugin Project and make sure you have a Java6 SDK as well as the IntelliJ SDK (the source code).
Hack away, Build -> Make project, Build -> Prepare Plugin for Deployment.


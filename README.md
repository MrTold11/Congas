# Congas

## About
Congas = console games - open-sourced cross-platform terminal games launcher written on Java. Simple and flexible input output system allows anyone to create their own games of any genre (that would fit into console) and upload them to the store. Congas depends on [JLine 3](https://github.com/jline/jline3) and [Jansi](https://github.com/fusesource/jansi) for interactive I/O (using raw console mode and ANSI escape sequences).

Currently it's developing by Khabarovsk Mathematical Lyceum students (Georgy Korobkov, Mikhail Spivakov and Vladislav Tkalin) for IT Cube's Hack-a-Weeks competition.

## Functional
Congas client allows you to download and run games in terminal. Currently it only supports anthologies - collentions of games generalized by genre or style. For now it also has debug mode for additional information. 

Note: client input doesn't support ctrl, alt, shift, F1-F12 and arrow keys, please use 'wasd' and other keys instead.

Server side and settings is currently unavailable, but it is still in TODO list for the nearest furute.

## Supported platforms
Congas supports platforms maintained by JLine3:
* FreeBSD
* Linux
* OS X
* Solaris
* Windows

## Usage
You can download latest client .jar file from [Releases](https://github.com/MrTold11/Congas/releases). Run it with `java [options] -jar <filename>.jar`. Options are not necessary, but allows to control Java heap size allocation and [much more](https://docs.oracle.com/javase/7/docs/technotes/tools/solaris/java.html#BGBJAAEH).

Running Congas server is similar with running a client, but additionally you have to open 26642 port in order to allow internet users connect to it.

## Long-term TODO
* Add localisation system and Russian language
* Move artifacts build from Inteiij IDEA to Gradle
* Add store with downloads and settings for console preferences

## More
* [Wiki](https://github.com/MrTold11/Congas/wiki)

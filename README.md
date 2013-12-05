Embedded Game Data Editor Application
===================================================

## Summary

This is a standalone Spring based Java application that embeds Jetty and uses SpringMVC as its web tier.
Java Service Wrapper is used to enable it can be run as a Windows Service or UNIX Daemon.

It provides a quick way to designed game data.

## Running

The default package goal will build a jar in the target directory with
dependency jars copied into editor/tools/lib.

    mvn package
    windows: editor/tools/bin/run.bat
    linux:   ./editor/tools/bin/run.sh 

Then point your browser at http://localhost:8080.

#!/bin/sh
. /etc/profile
export JAVA_HOME=/usr/local/jdk/
DIR=`dirname $0`
$DIR/run.sh stop


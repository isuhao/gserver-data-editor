#!/bin/sh
SVN=/opt/CollabNet_Subversion/bin/svn
DIR=`dirname $0`/../
LOG=`dirname $0`/../../
export LANG=en_US.UTF-8
$SVN cleanup $DIR >> $LOG/svnlog.txt 2>> $LOG/svnerr.txt
$SVN up $DIR >> $LOG/svnlog.txt 2>> $LOG/svnerr.txt

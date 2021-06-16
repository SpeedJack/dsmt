#!/bin/bash
set -e
ERL=erl
EXECUTORNAME=exec
EXECUTORCOUNT=3
DISPATCHERNAME=disp1
INIT=start
WORKDIR="$(realpath "$(dirname ".")")"

while getopts n:e:d:p:i flag
do
	case "${flag}" in
		n) EXECUTORCOUNT=${OPTARG};;
		e) EXECUTORNAME=${OPTARG};;
		d) DISPATCHERNAME=${OPTARG};;
		p) WORKDIR=${OPTARG};;
		i) INIT=init;;
	esac
done

echo -n '* Starting dispatcher...'
$ERL -sname $DISPATCHERNAME@localhost -noshell -detached -s das startup $INIT $WORKDIR frist
echo 'done!'
echo '* Waiting 5 secs...'
sleep 5
echo -n '* Starting executors...'
for i in $(seq 1 $EXECUTORCOUNT)
do
	$ERL -sname $EXECUTORNAME$i@localhost -noshell -detached -s das startup $INIT $WORKDIR $DISPATCHERNAME@localhost
	echo -n " $i..."
done
echo 'done!'
echo '* All nodes started! Use the stop script to shutdown.'

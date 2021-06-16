#!/bin/bash

erl -sname disp1@localhost -noshell -detached -s das startup init $(realpath .) frist
echo 'disp started. waiting 10 secs...'
sleep 10
echo 'starting execs'
erl -sname exec1@localhost -noshell -detached -s das startup init $(realpath .) disp1@localhost
erl -sname exec2@localhost -noshell -detached -s das startup init $(realpath .) disp1@localhost
erl -sname exec3@localhost -noshell -detached -s das startup init $(realpath .) disp1@localhost
echo 'done! :)'

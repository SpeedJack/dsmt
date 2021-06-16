#!/bin/bash
set -e

echo -n '* Stopping nodes...'
pkill beam.smp
echo 'done!'

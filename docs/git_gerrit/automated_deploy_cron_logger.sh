#!/bin/sh
cd /home/oscara/myoscar/
./automated_deploy_myoscar_client.sh>cron_logs/client_`date +%F_%T`.log 2>&1
./automated_deploy_myoscar_server.sh>cron_logs/server_`date +%F_%T`.log 2>&1

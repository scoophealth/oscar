#!/bin/sh
git daemon --syslog --export-all --base-path=/var/gerrit/review_site/git --detach

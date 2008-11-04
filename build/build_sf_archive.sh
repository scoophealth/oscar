#!/bin/sh
##########
# This script should build a SF deployment archive

ant clean
ant

pushd ..
tar cvfz build/tmp/oscar.tar.gz database install build/tmp/*.war
popd

echo ----------
echo Run the following to upload the archive
echo - sftp <sf_user>@frs.sourceforge.net
echo - cd uploads
echo - put tmp/oscar.tar.gz
echo ----------

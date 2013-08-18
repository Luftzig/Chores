#!/bin/bash

TMP_FILE=/tmp/parse.tmp
if [ -e /tmp/parse.tmp ]; then
  echo "Cleaning up from previous install failure"
  rm -f /tmp/parse.tmp
fi
echo "Fetching latest version ..."
curl --progress-bar https://www.parse.com/downloads/cloud_code/parse -o /tmp/parse.tmp
if [ ! -d ${HOME}/bin ]; then
  echo "Making ${HOME}/bin"
  mkdir -p /usr/local/bin
fi
echo "Installing ..."
mv /tmp/parse.tmp ${HOME}/bin/parse
chmod 755 ${HOME}/bin/parse

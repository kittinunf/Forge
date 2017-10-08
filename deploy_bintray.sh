#!/bin/bash

if [[ "$TRAVIS_BRANCH" == */release-* ]]; then

  echo "We're on release branch, deploying at $TRAVIS_BRANCH"

  modules=("forge")
  for i in "${modules[@]}"
  do
    echo ">> Deploying $i ..."
    ./gradlew :$i:clean :$i:build :$i:bintrayUpload -PbintrayUser=$BINTRAY_USER -PbintrayKey=$BINTRAY_KEY -PdryRun=false
    echo ">> Done deploying for $i"
  done

fi

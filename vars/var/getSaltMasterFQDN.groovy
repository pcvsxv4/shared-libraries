#!/usr/bin/env groovy
//
// Returns the correct salt master hostname given the environment.
//
String call(String env = 'dev') {
  if (env == 'production') {
    'orchestration.peapod.com'
  } else {
      "orchestration.node.${env}.peapod.com"
    }
  }

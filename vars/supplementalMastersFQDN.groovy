#!/usr/bin/env groovy
//
// Returns the supplemental salt master hostname(s) given the environment.
//
def call(String env = 'dev') {
  if (env == 'production') {
    def arr = []
    return arr
  }
  else if (env == 'nacl') {
    def arr = ["orchestration1.node.${env}.peapod.com"]
    return arr
  }
  else if (env == 'dev') {
    def arr = []
    return arr
  }
  else if (env == 'qa') {
    def arr = []
    return arr
  }
  else if (env == 'stage') {
    def arr = []
    return arr
  }
  else if (env == 'common') {
    def arr = []
    return arr
  }
  else if (env == 'prod') {
    def arr = []
    return arr
  }
  else if (env == 'prodsec') {
    def arr = []
    return arr
  }
  else {
    println ("Please enter the valid environemnt")
  }
}

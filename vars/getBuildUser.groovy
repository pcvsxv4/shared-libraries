#!/usr/bin/env groovy

// Returns the current build user's Id and UserName.
// For example,
//
//   node { stage('Get User') { def (userId, userName) = getBuildUser() } }
//
def call() {
  def cause = currentBuild.rawBuild.getCause(hudson.model.Cause.UserIdCause.class)
  [cause.getUserId(), cause.getUserName()]
}

#!/usr/bin/env groovy

// Quite simply, this displays all of the shell env vars available to the job console.
// For example,
//
//   node { stage('Env') { debugEnvironment() } }
//
def call() {
  echo sh(script: 'env|sort', returnStdout: true)
}

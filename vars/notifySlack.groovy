#!/usr/bin/env groovy
//
// Sends a slack notification formatted based on build status.
//
def call(message, buildStatus = 'STARTED', room = 'tasks') {
  // Build status of null means success.
  buildStatus = buildStatus ?: 'SUCCESS'

  def colors = [
    STARTED: '#D4DADF',
    SUCCESS: 'good',
    TESTING: '#808080',
    UNSTABLE: 'warning',
  ]
  def color = colors.get(buildStatus ?: 'SUCCESS', 'danger')
  slackSend(color: color, message: "${buildStatus}: ${message}", channel: room)
}

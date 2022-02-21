#!/usr/bin/env groovy
/*
 * Triggers a build job. Parameters:
 *   `jenkinsUrl`: The base url of the jenkins server where the job is defined.
 *   `path`: The path to the job to run (e.g. job/saltstack/job/apply-master).
 *   `parameters`: A Map containing the parameters to pass to the build job.
 *   `credentials`: The id of the credentials to use when invoking the remote build.
 *
 * Requirements:
 *  HTTP-Request plugin (https://github.com/jenkinsci/http-request-plugin)
 */

import groovy.json.JsonSlurperClassic

def call(Map args) {
  String path = args['path']
  String credentials = args['credentials']
  String jenkinsUrl = args['jenkinsUrl'] ?: env.JENKINS_URL
  Map parameters = args['parameters'] ?: [:]

  def crumbResponse = httpRequest(
    url:            "${jenkinsUrl}/crumbIssuer/api/json",
    acceptType:     'APPLICATION_JSON',
    authentication: credentials,
  )

  def crumbData = new JsonSlurperClassic().parseText(crumbResponse.content)
  String crumb = crumbData['crumb']
  String queryString = parameters.collect { it }.join('&')
  String action = (queryString == '') ? 'build' : "buildWithParameters?${queryString}"

  def trigger = httpRequest(
    url:            "${jenkinsUrl}/${path}/${action}",
    httpMode:       'POST',
    authentication: credentials,
    customHeaders: [[name: 'Jenkins-Crumb', value: crumb]],
    validResponseCodes: '201',
  )
}

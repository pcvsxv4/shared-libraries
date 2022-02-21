#!/usr/bin/env groovy
//
// Returns a string containing the salt-run command to be invoked based on the supplied arguments.
//

String call(Map args = [:]) {
  String env = args['env'] ?: 'dev'
  String master = args['master'] ?: getSaltMasterFQDN(env)
  String function = args['function'] ?: 'manage.down'
  String options = args['options'] ?: ''
  int timeout = args['timeout'] ?: 300

  return "ssh -tt -oStrictHostKeyChecking=no jenkinsbuild@${master} sudo salt-run --timeout ${timeout} --state-output terse ${function} ${options}"
}

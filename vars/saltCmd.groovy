#!/usr/bin/env groovy
//
// Returns a string containing the salt command to be invoked based on the supplied arguments.
//

String call(Map args = [:]) {
  String env = args['env'] ?: 'dev'
  String master = args['master'] ?: getSaltMasterFQDN(env)
  String target = args['target'] ?: "-L '${master}'"
  String function = args['function'] ?: 'test.ping'
  String options = args['options'] ?: ''
  int timeout = args['timeout'] ?: 300

  if (target.contains("'")) {
    target = escapeSingleQuotes(target)
  }

  return "ssh -tt -oStrictHostKeyChecking=no jenkinsbuild@${master} sudo salt --timeout ${timeout} --state-output terse ${target} ${function} ${options}"
}

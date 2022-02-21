String call(Map args = [:]) {
  String env = args['env'] ?: 'nacl'
  String master = args['master'] ?: getSaltMasterFQDN(env)
  String target = args['target'] ?: supplementalMastersFQDN(env)

  if (target.contains("'")) {
    target = escapeSingleQuotes(target)
  }

  return "ssh -tt -oStrictHostKeyChecking=no jenkinsbuild@${master} sudo rsync -Pa /etc/salt/pki/master/minions* ${target}:/etc/salt/pki/master/ --exclude orchestration* --delete"
}

String call(Map args = [:]) {
  String env = args['env'] ?: 'nacl'
  String master = args['master'] ?: getSaltMasterFQDN(env)
  String[] target = args['target'] ?: supplementalMastersFQDN(env)

  if (target.contains("'")) {
    target = escapeSingleQuotes(target)
  }
  for (supMaster in target)
  {
  return "ssh -tt -oStrictHostKeyChecking=no jenkinsbuild@${master} sudo sh -c 'rsync -Pa /etc/salt/* ${supmaster}:/etc/salt/ --exclude 'pki' --delete && rsync -Pa /srv/salt/* ${supMaster}:/srv/salt/ --delete'"
  }
}

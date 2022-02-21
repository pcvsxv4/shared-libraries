#!/usr/bin/env groovy
/*
 *
 * Exposes the fields defined for a given vault path as environment variables with the field name
 * uppercased.
 *
 * For example,
 *
 *   withVaultAppRole(
 *     url: 'https://vault.service.dev.peapod.com:8200',
 *     appRoleName: 'peapod-qa',
 *     secretsFromPath: 'secret/peapod-qa/robot',
 *     credentialsId: 'vault-token',
 *   ) {
 *       sh '...'  // some command that uses the secrets contained within secret/peapod-qa/robot
 *   }
 *
 * IMPORTANT: Currently it is possible to expose secrets by echo'ing the corresponding environment
 *            variables. Avoid doing so.
 *
 * FIXME: Unit tests are currently not implemented.
 */
import groovy.json.JsonOutput
import groovy.json.JsonSlurperClassic


String getAppRoleId(String role) {
  echo '-----> fetching role-id'

  def response = httpRequest(
    url: "${env.VAULT_ADDR}/v1/auth/approle/role/${role}/role-id",
    customHeaders: [[name: 'X-Vault-Token', value: env.VAULT_TOKEN]],
    consoleLogResponseBody: false,
    quiet: true,
  )

  new JsonSlurperClassic().parseText(response.content)?.data?.role_id
}

String getAppRoleSecretId(String role) {
  echo '-----> fetching secret-id'

  def response = httpRequest(
    url: "${env.VAULT_ADDR}/v1/auth/approle/role/${role}/secret-id",
    httpMode: 'POST',
    customHeaders: [[name: 'X-Vault-Token', value: env.VAULT_TOKEN]],
    consoleLogResponseBody: false,
    quiet: true,
  )

  new JsonSlurperClassic().parseText(response.content)?.data?.secret_id
}

String loginAppRole(Map data = [:]) {
  echo '-----> login approle'

  def response = httpRequest(
    url: "${env.VAULT_ADDR}/v1/auth/approle/login",
    httpMode: 'POST',
    requestBody: JsonOutput.toJson(data),
    consoleLogResponseBody: false,
    quiet: true,
  )

  new JsonSlurperClassic().parseText(response.content)?.auth?.client_token
}

def getAppRoleSecret(String path, String token) {
  echo '-----> fetching secrets'
  def response = httpRequest(
    url: "${env.VAULT_ADDR}/v1/${path}",
    customHeaders: [[name: 'X-Vault-Token', value: token]],
    consoleLogResponseBody: false,
    quiet: true,
  )

  new JsonSlurperClassic().parseText(response.content)?.data
}


def call(Map parameters = [:], body = null) {
  withCredentials([string(credentialsId: parameters.credentialsId, variable: 'VAULT_TOKEN')]) {
    withEnv(["VAULT_ADDR=${parameters.url}"]) {
      echo "################################################################################\n" +
           "### Fetching approle secrets from vault service @ ${parameters.url}\n" +
           "################################################################################\n"

      String roleId = getAppRoleId(parameters.appRoleName)
      String secretId = getAppRoleSecretId(parameters.appRoleName)
      String appRoleToken = loginAppRole(role_id: roleId, secret_id: secretId)
      def secrets = getAppRoleSecret(parameters.secretsFromPath, appRoleToken)

      withEnv(secrets.collect { "${it.key.toUpperCase()}=${it.value}" }) {
        body()
      }
    }
  }
}

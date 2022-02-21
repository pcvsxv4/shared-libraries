#!/usr/bin/env groovy
/*
 * Fails jobs if any of the supplied environment variables are not set; often the
 * case when a pipeline build has never run (provided that they define paramters).
 *
 * For example,
 *
 *   failOnFirstRun(['ENVIRONMENT'])
 *
 * IMPORTANT: You should call this method after the `properties[(parameters[(...)])]`
 *            declaration, otherwise parameters will not be created.
 */
def call(parameterNames) {
  for (name in parameterNames) {
    if (!env[name]) {
      error("The ${name} env var is missing. First run? Please build again.")
    }
  }
}

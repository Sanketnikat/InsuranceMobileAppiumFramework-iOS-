# Security policy

## Scope

This is a mobile-test framework. Test runs can collect customer data in page
source, screenshots, videos, reports, Appium/Xcode logs, and device
configuration. Treat all of those as sensitive.

## Rules for contributors

- Never commit real customer mobile numbers, dates of birth, OTPs, passwords,
  tokens, device identifiers, or signing material.
- Use non-production test accounts and one-time OTPs supplied at run time (for
  example, a CI secret or a local JVM property), never a value in `testng.xml`.
- Keep device-specific values and signing configuration in ignored local files;
  commit only sanitized example configuration.
- Do not upload `reports/`, `screenshots/`, `videos/`, `target/allure-results`,
  Appium logs, or Xcode logs to tickets or pull requests without redaction.
- Review all dependency-update pull requests and keep GitHub Actions references
  current. Dependabot and the CodeQL workflow provide the baseline checks.

## Current remediation priority

`src/test/resources/config.properties` is currently versioned and contains
machine-specific configuration. Move those values to an ignored local
configuration mechanism before sharing the repository outside the trusted team,
then remove the values from Git history if the repository was already shared.

## Reporting a vulnerability

Do not open a public issue containing sensitive details. Report it privately to
the repository owner with the affected file or component, impact, and safe
reproduction steps.

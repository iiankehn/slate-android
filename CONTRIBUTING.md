# Contributing to Slate R1

Thank you for helping improve Slate. Contributions should preserve R1's focused, private, local-first product scope.

## Start with an issue

- Use the [bug report form](https://github.com/iiankehn/slate-android/issues/new?template=bug_report.yml) for reproducible defects.
- Use the [feature request form](https://github.com/iiankehn/slate-android/issues/new?template=feature_request.yml) for R1 product proposals.
- Search open and closed issues before filing a duplicate.
- Do not include private writing, credentials, signing data, or security exploit details in public issues.

Security vulnerabilities follow [SECURITY.md](SECURITY.md).

## Product boundary

Slate R1 and Slate R2 are separate products, not sequential releases. Changes in this repository should fit the focused R1 notes/document experience. Full desktop-class word processing, Googlebook-specific expansion, and broad ARM64/x86_64 product work belong to the separate R2 line.

## Development setup

Required baseline:

- JDK 17
- Gradle 8.13
- Android SDK 36
- An Android 12+ device or emulator for relevant manual checks

Run the same core verification as CI:

```bash
gradle --no-daemon testDebugUnitTest lintDebug assembleDebug
```

## Change expectations

- Keep the device copy authoritative and writing functional offline.
- Do not add telemetry, advertising, crash-reporting, account, or engagement SDKs.
- Avoid broad Android permissions; use scoped platform APIs.
- Add or update tests for document models, persistence encoding, and format behavior.
- Document user-visible behavior and fidelity limitations.
- Keep Material 3 UI flat, adaptive, keyboard-safe, and consistent with the Midnight/CORE-blue identity.
- Preserve compact Back navigation and expanded two-pane behavior.
- Treat imported files and update metadata as untrusted input with explicit limits and validation.

## Pull requests

A pull request should include:

- a clear problem statement and the chosen behavior;
- linked issues when applicable;
- tests or an explanation of why existing coverage is sufficient;
- screenshots or recordings for meaningful UI changes, with private content removed;
- documentation updates for user-visible features or limitations;
- confirmation that unit tests, lint, and debug assembly pass.

Keep commits focused. Never commit release keystores, passwords, tokens, local SDK paths, generated APKs, or private test documents.

## Release changes

Only GitHub Actions should receive signing secrets. The monthly release workflow verifies, shrinks, signs, checksums, and stages R1 artifacts. See [docs/RELEASING.md](docs/RELEASING.md).

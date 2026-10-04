# Security policy

## Supported version

The latest published unified Slate release receives security fixes. Older APKs should be updated through **Check for updates** or the [latest GitHub release](https://github.com/iiankehn/slate-android/releases/latest).

## Report a vulnerability

Do not open a public issue for a vulnerability that could expose documents, bypass package validation, compromise update integrity, or disclose signing information. Use GitHub's private vulnerability reporting for `iiankehn/slate-android` when available; otherwise contact the repository owner privately through their published GitHub contact channel.

Include affected versions, Android version, impact, reproduction steps, and the smallest safe proof of concept. Do not include real user documents, credentials, keystores, or signing passwords.

## Security boundaries

- Documents are local app-private data, not end-to-end encrypted cloud data.
- `.slx`, `.slxf`, DOCX, Markdown, TXT, and image inputs are untrusted.
- Importers enforce size, count, path, and checksum limits where applicable.
- The updater accepts only declared assets from the official repository, verifies SHA-256, and relies on Android signature checks before replacement.
- There is no automatic background update or telemetry path.

## Disclosure

Please allow reasonable time to investigate and publish a fix before public disclosure. Confirmed reports will be credited when the reporter wants attribution and disclosure is safe.

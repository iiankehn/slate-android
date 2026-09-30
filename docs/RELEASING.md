# Slate R1 releases

Slate R1 uses monthly GitHub-managed releases. The scheduled workflow runs on the first day of each month at 15:00 UTC. It verifies the project, builds an optimized APK, signs it, generates a SHA-256 checksum and `slate-r1-update.json`, and creates or refreshes a draft release named `Slate R1 — YYYY-MM`.

## Required GitHub Actions secrets

- `SLATE_R1_KEYSTORE_BASE64`: the release keystore encoded as Base64
- `SLATE_R1_KEY_ALIAS`: signing-key alias
- `SLATE_R1_KEYSTORE_PASSWORD`: keystore password
- `SLATE_R1_KEY_PASSWORD`: signing-key password

The keystore and passwords must never be committed to Git.

## Manual release

Run **R1 Monthly Release** from the Actions tab. Leave `publish` disabled to create a draft for review, or enable it to publish the release after a successful build. The scheduled run always creates a draft.

The user-facing product version remains `R1`. The workflow supplies an internal monotonically increasing Android version code based on the release workflow run number. This allows a newly signed APK to update an existing Slate installation without exposing build numbers as part of the product name.

## Native updater

The editor menu includes **Check for updates**. Slate contacts GitHub only after the user selects that command. It reads the latest published release, compares the internal version code, downloads the listed APK, verifies its SHA-256 checksum, and hands the verified file to Android's package installer.

Android may ask the user to allow Slate as an installation source and always presents the system installation confirmation. The package name, signing certificate, and higher internal version code allow the APK to replace the installed copy while preserving app data. Draft releases are intentionally excluded from the public update channel.

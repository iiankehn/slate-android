# Slate R1 releases

Slate R1 uses monthly GitHub-managed releases. The scheduled workflow runs on the first day of each month at 15:00 UTC. It verifies the project, builds an optimized APK, signs it, generates a SHA-256 checksum, and creates or refreshes a draft release named `Slate R1 — YYYY-MM`.

## Required GitHub Actions secrets

- `SLATE_R1_KEYSTORE_BASE64`: the release keystore encoded as Base64
- `SLATE_R1_KEY_ALIAS`: signing-key alias
- `SLATE_R1_KEYSTORE_PASSWORD`: keystore password
- `SLATE_R1_KEY_PASSWORD`: signing-key password

The keystore and passwords must never be committed to Git.

## Manual release

Run **R1 Monthly Release** from the Actions tab. Leave `publish` disabled to create a draft for review, or enable it to publish the release after a successful build. The scheduled run always creates a draft.

The user-facing product version remains `R1`. The workflow supplies an internal `YYYYMM` Android version code because Android requires monotonically increasing integer version codes.

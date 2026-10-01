# Slate R1 releases

Slate R1 uses a monthly GitHub-managed release line. The public product designation remains `R1`; the calendar month identifies the release channel artifact rather than a new product generation.

The official first release is [`r1-2026-09`](https://github.com/iiankehn/slate-android/releases/tag/r1-2026-09).

## Automated workflow

`.github/workflows/r1-monthly-release.yml` runs on the first day of each month at 15:00 UTC and can also be started manually. It:

1. checks out the selected `main` commit;
2. configures JDK 17 and Gradle 8.13;
3. restores the signing keystore from repository secrets;
4. runs `testDebugUnitTest`, `lintDebug`, and `assembleRelease`;
5. copies the optimized signed APK into the release payload;
6. generates the APK SHA-256 checksum and `slate-r1-update.json`;
7. creates or refreshes the monthly GitHub release.

Scheduled runs remain drafts for review. Manual runs accept a `publish` input.

## Required GitHub Actions secrets

- `SLATE_R1_KEYSTORE_BASE64`: release keystore encoded as Base64
- `SLATE_R1_KEY_ALIAS`: signing-key alias
- `SLATE_R1_KEYSTORE_PASSWORD`: keystore password
- `SLATE_R1_KEY_PASSWORD`: signing-key password

Signing material must never be committed, printed, attached to issues, or added to release notes.

## Version identity

- Application ID: `com.iiankehn.slate`
- User-facing version name: `R1`
- Release tag: `r1-YYYY-MM`
- Release title: `Slate R1 — YYYY-MM`
- Internal version code: `1,000,000 + GITHUB_RUN_NUMBER`

The hidden monotonically increasing version code allows a newly signed APK to update an installed official copy while preserving data. R1 and R2 remain separate products; this scheme does not make R2 an upgrade from R1.

## Release assets

Every official R1 release must contain:

- `Slate-R1-YYYY-MM.apk`
- `Slate-R1-YYYY-MM.apk.sha256`
- `slate-r1-update.json`

The manifest contains `versionCode`, `versionName`, stable tag-based `apkUrl`, and the APK SHA-256 value.

## Review and publication checklist

- Confirm Android CI passed on the exact source commit.
- Keep the generated release as a draft while reviewing the APK.
- Install over the previous official build and check document preservation.
- Exercise creation/editing, keyboard visibility, compact Back navigation, import/export, and update checking.
- Confirm the checksum file matches the release APK.
- Confirm the updater manifest points to the stable tag URL and contains the same checksum.
- Add complete, user-facing release notes including limitations and reporting links.
- Publish the draft and verify `/releases/latest` resolves to it.
- Confirm the public APK and manifest URLs are downloadable without repository authentication.

## Native updater behavior

The editor menu's **Check for updates** action reads only the latest published release. Draft releases are intentionally excluded. After verifying the manifest and APK checksum, Slate hands the file to Android's package installer. Android may request installation-source permission and always presents final confirmation.

If package identity, signing certificate, or version ordering does not match, Android rejects the in-place update. Never work around that protection by changing the application ID or publishing an unsigned replacement.

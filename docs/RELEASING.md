# Slate releases

Slate uses one monthly GitHub-managed release line. A calendar version identifies the release, and the source commit identifies the exact build.

## Identity

- Package: `com.iiankehn.slate`
- Version name: `YYYY.MM.REVISION+COMMIT` (for example `2026.11.1+abc1234`)
- Version code: `YYYYMM × 1000 + REVISION` (for example `202611001`)
- Tag: `slate-YYYY-MM`
- APK: `Slate-YYYY-MM.apk`
- Update manifest: `slate-update.json`, plus an identical `slate-r1-update.json` for installed R1 builds

Versions follow the calendar. The revision is `1` for a month's first build and increases by one each time that month's release is rebuilt; it restarts at `1` the next month. Android requires a hidden, strictly increasing version code to accept an in-place update, so the same year, month, and revision are packed into one integer. It always rises month to month and within a month, and stays above every earlier scheme (R1 published `1000004`; the October 2026 unified build used `1000002`). The workflow refuses to build a code that is not greater than the latest published release's.

Keeping the existing package and signing identity allows Slate Notes users to install the unified app as an update. Do not change either without an explicit migration plan.

## Automation

`.github/workflows/slate-monthly-release.yml` runs on its monthly schedule or by manual dispatch. It:

1. derives the calendar version and commit suffix;
2. builds and tests the signed release APK;
3. verifies the APK signature and expected package;
4. inspects any native libraries for ARM64 and x86_64 coverage;
5. generates the SHA-256 update manifest;
6. publishes the APK and manifest to a GitHub release.

The workflow maps the established `SLATE_R1_*` repository secrets into generic `SLATE_*` build variables. This preserves the production signing key while the product name changes; secret renaming can happen separately without rotating identity.

## Required secrets

- `SLATE_R1_KEYSTORE_BASE64`
- `SLATE_R1_KEYSTORE_PASSWORD`
- `SLATE_R1_KEY_ALIAS`
- `SLATE_R1_KEY_PASSWORD`

`SLATE_R1_CERT_SHA256` is optional but recommended. When configured, the release workflow compares the APK's signing certificate with the pinned production certificate fingerprint and stops before uploading anything if they differ.

Signing files and passwords must never enter source control, logs, issue attachments, or release notes.

## Pre-release checklist

- CI tests, lint, and release assembly pass.
- Existing Notes data upgrades successfully without clearing app data.
- A previous signed build accepts the new APK as an in-place update.
- Note and Forge documents reopen after restart.
- `.slx` and `.slxf` exports round-trip.
- Import safety limits reject oversized or malformed input safely.
- Back navigation, software keyboard insets, compact layout, and expanded layout are manually smoke-tested on available hardware.
- The generated manifest URL, version code, byte size, and SHA-256 match the APK.
- Release notes describe user-visible behavior and known limitations.

## Re-running in the same month

A run for a month whose release already exists moves that month's tag to the new commit and replaces its assets. If the release is already published, the workflow stops unless it was started manually with `publish=true`, so a scheduled or draft run can never pull a published release back to draft.

Each month's notes come from `docs/releases/YYYY-MM.md`; add that file before the month's run. `RELEASE_NOTES.md` is only a fallback.

## Rollback

Never publish a lower version code or an APK signed with another key. If a release is faulty, fix forward with a higher version code. Keep the bad release available only when needed for forensic comparison and clearly mark it as affected.

# Privacy and security baseline

Slate R1 is private by construction.

## Data handling

- Documents, metadata, formatting ranges, and recovery checkpoints are stored in an app-private Room database.
- The device copy is authoritative. Writing does not require a server or account.
- Recovery history is capped at 30 checkpoints per document.
- Android cloud backup and device transfer are disabled to avoid creating an unclear remote copy of private writing.
- Uninstalling Slate or clearing its app storage removes app-private documents. Users should export important work first.
- User writing leaves app-private storage only through an explicit import, export, share, print, or user-selected image action.

## Permissions and platform access

- No contacts, location, microphone, camera, advertising identifier, analytics, or broad storage permission.
- Imports, exports, and images use Android's scoped document APIs and temporary or persisted URI grants.
- Screenshot and recent-app preview capture are blocked while the Slate activity is visible.
- Automatic backup is disabled in both the application manifest and data-extraction rules.

## Network behavior

Slate has network permission solely for the user-initiated native updater.

- No background polling, telemetry, diagnostics upload, analytics, ads, experiments, or sponsored content.
- Selecting **Check for updates** contacts GitHub's API and official `iiankehn/slate-android` release URLs.
- Cleartext traffic is disabled.
- Downloaded update files are cached in a private update directory and shared only with Android's package installer through a narrowly scoped `FileProvider` grant.

## Untrusted input controls

- General document imports are limited to 25 MB.
- DOCX decoding is limited to 2,000 archive entries and 20 MB of document XML.
- Imports are parsed off the UI thread and committed only after successful validation.
- Update JSON is limited to 64 KB.
- Update APKs are limited to 128 MB and must use an official-repository HTTPS URL.
- Slate verifies the published SHA-256 value before installation; Android separately verifies the application signing certificate.

## Release-chain protections

- Release keystores and passwords remain in GitHub Actions secrets and are never committed.
- Release builds run unit tests and Android lint before optimized APK assembly.
- Each release includes the APK, its SHA-256 checksum file, and the updater manifest.
- Internal Android version codes increase monotonically while the user-facing product remains `R1`.

See [SECURITY.md](../SECURITY.md) for responsible vulnerability reporting.

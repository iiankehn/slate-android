# Privacy

Slate is private by construction and local-first by default.

## Stored on the device

Documents, metadata, folders, tags, favorites, archive/trash state, editor content, and recovery checkpoints live in the app-private Room database. Imported image references use Android content URIs selected by the user.

Android automatic backup and device transfer are disabled. Uninstalling Slate removes its app-private data, so users should export important work before uninstalling.

## Network behavior

Slate contains no advertising, analytics, telemetry, crash-reporting SDK, account system, cloud sync, or background update worker. The only built-in network action is **Check for updates**, which the user initiates manually. It contacts the official GitHub release source and downloads an APK only when accepted.

## User-directed sharing

Import, export, print, and share use Android system surfaces. The destination application or provider has its own privacy policy. Slate does not upload those files itself.

## Permissions

Slate avoids broad storage access. Android's Storage Access Framework grants access only to items selected by the user. The updater uses a narrowly scoped content provider for its verified APK. Android may require permission to install packages from Slate before an update can continue.

## Reporting issues

Do not attach private writing, personal information, credentials, signing data, or sensitive documents to public issues. Reduce a document to the smallest non-sensitive sample that reproduces the problem. See [SECURITY.md](../SECURITY.md) for confidential vulnerability reporting.

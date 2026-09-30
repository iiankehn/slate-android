# Slate by CORE

Slate is a private, local-first writing app for Android. It is designed to be as immediate as a notes app and structured enough for complete documents.

## Slate R1 status

Slate R1 includes:

- a native Kotlin and Jetpack Compose application shell;
- compact phone navigation and an adaptive two-pane tablet/laptop layout;
- a Room-backed document library with a versioned rich-text model;
- selection-aware bold, italic, underline, and heading formatting;
- bulleted lists, checklists, and editor undo/redo;
- links, quotes, embedded images, simple tables, find, and keyboard shortcuts;
- working rename, duplicate, pin, archive, and delete document actions;
- folders, tags, favorites, full-library search, recoverable Trash, and version history;
- plain-text, Markdown, and DOCX interchange plus PDF export, printing, sharing, and file associations;
- immediate recovery checkpoints and debounced automatic saving;
- the Midnight and CORE Glass visual direction using CORE blue `#0072BC`;
- no network, advertising, location, contacts, or analytics permissions;
- Android 12 (API 31) as the minimum supported release;
- GitHub Actions verification for every change and a monthly R1 release workflow;
- unit tests and lint/build gates.

Documents now survive app restarts. Each edit is written to a bounded recovery journal immediately, while the main document snapshot is saved after a short idle delay. On launch, Slate restores a journal entry when it is newer than the main document record.

## Build

The project is pinned to JDK 17, Gradle 8.13, and Android Gradle Plugin 8.13.2. GitHub Actions can do the heavy build work:

Every push to `main` is verified by GitHub Actions. On the first day of each month, the R1 release workflow builds an optimized signed APK, generates a SHA-256 checksum, and creates or refreshes a draft GitHub release. See `docs/RELEASING.md` for the required repository secrets and manual publishing flow.

For a local build with a compatible Android SDK:

```bash
gradle testDebugUnitTest lintDebug assembleDebug
```

## Product principles

1. Writing must never depend on a connection or account.
2. The device copy is authoritative.
3. Autosave and recovery are core features, not polish.
4. Common formats belong at the boundary; Slate's internal document model stays independent.
5. No telemetry, diagnostics, advertising identifiers, behavioral analytics, or experiments.
6. Tablet and keyboard behavior is designed alongside phone behavior.

See `docs/` for the product brief, architecture, privacy baseline, and roadmap.

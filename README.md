# Slate by CORE

Slate is a private, local-first Android writing app that adapts from quick notes to complete word-processing documents. One installation combines the focused Slate Notes workflow with Slate Forge's ribbon workspace, page-layout engine, and large-screen support.

## Get Slate

- [Download the latest signed APK](https://github.com/iiankehn/slate-android/releases/latest)
- Android 12 or newer
- ARM64 and x86_64 Android devices
- Package: `com.iiankehn.slate`

Slate requires no account and includes no ads, analytics, telemetry, cloud sync, or background update polling.

## One adaptive workspace

Every new file starts in **Adaptive** mode:

- ordinary text and rich formatting stay a lightweight **Note**;
- page setup, sections, headers, footers, structured tables, or forced page breaks promote it to **Forge**;
- document length alone never changes its type;
- once a document requires Forge features, it remains Forge so its layout cannot be lost.

The same Forge-derived interface is used for both experiences. The library labels each document as Note or Forge, while search, favorites, folders, tags, archive, trash, and recovery remain unified.

## Highlights

- Material 3 Expressive-inspired workspace with native system light/dark themes and a theme-aware document canvas
- Collapsible phone ribbon with persistent compact/expanded preference; expanded ribbon layouts on larger screens
- Rich text, lists, checklists, links, images, tables, find, undo, redo, and keyboard shortcuts
- Sections, page setup, headers and footers, page breaks, and paginated layout
- Local Room database with autosave, recovery checkpoints, and version history
- Import from `.slx`, `.slxf`, TXT, Markdown, and DOCX
- Export to `.slx`, `.slxf`, TXT, Markdown, DOCX, and PDF; system share and print support
- Manual native updater with SHA-256 verification and Android signature enforcement
- CI-built ARM64 and x86_64-compatible APKs

## File formats

| Format | Purpose |
| --- | --- |
| `.slx` | Portable rich-note document for Note-compatible content |
| `.slxf` | Lossless Slate document for complete Forge page and layout features |

Slate chooses the appropriate experience from document structure when importing. Exporting to `.slx` can simplify Forge-only layout, but never changes the original `.slxf` document.

## Upgrading and migration

Existing Slate Notes installations upgrade in place because Slate keeps the package ID and signing identity. The Room schema migrates existing documents into the unified library.

The former standalone Slate Forge application used a different Android package. Export its documents as `.slxf` or `.slx`, then import them into Slate. Its repository remains available as historical source until migration is complete.

## Build and verify

GitHub Actions is the canonical build environment. For local development:

```bash
./gradlew test lint assembleDebug
```

Release signing material belongs only in GitHub Actions secrets. See [Release process](docs/RELEASING.md) for the monthly build flow.

## Documentation

- [Product definition](docs/PRODUCT.md)
- [Feature reference](docs/FEATURES.md)
- [User guide](docs/USER_GUIDE.md)
- [Architecture](docs/ARCHITECTURE.md)
- [Slate formats](docs/SLATE_FORMATS.md)
- [Roadmap](docs/ROADMAP.md)
- [Privacy](docs/PRIVACY.md)
- [Release process](docs/RELEASING.md)
- [Release notes](RELEASE_NOTES.md)

## Report an issue

Use the repository's [issue forms](https://github.com/iiankehn/slate-android/issues/new/choose). Include the Slate version, Android version, device/form factor, document format, and exact reproduction steps. Remove private writing, personal data, credentials, signing material, and sensitive documents before attaching anything.

Security-sensitive reports should follow [SECURITY.md](SECURITY.md).

## Product principles

- Local-first by default
- One coherent app, not parallel product forks
- Notes remain fast; advanced layout appears when needed
- Open interchange at the edges, lossless Slate format at the center
- No silent network activity
- Accessible across phone, tablet, keyboard, mouse, and touch

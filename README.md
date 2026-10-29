# Slate by CORE

Slate is a private, local-first Android word processor for formatted documents, page layout and printing.

**Development direction:** October 2026 is the final release with the Notes experience. Subsequent builds use one paged word-processing workspace. Notes source code and file compatibility are retained, and existing notes remain accessible as documents. The latest published October APK may still include Notes.

## Get Slate

- [Download the latest signed APK](https://github.com/iiankehn/slate-android/releases/latest)
- Android 12 or newer
- ARM64 and x86_64 Android devices
- Package: `com.iiankehn.slate`

Slate requires no account and includes no ads, analytics, telemetry, cloud sync, or background update polling.

## One word-processing workspace

New documents use Forge’s page-layout workspace. Existing documents and notes share the same library, editor, autosave and recovery tools. Note/Forge classification remains available internally for compatibility, without requiring users to choose between two experiences.

## Highlights

- Material 3 Expressive-inspired workspace with native system light/dark themes and a theme-aware document canvas
- Collapsible phone ribbon with persistent compact/expanded preference; expanded ribbon layouts on larger screens
- Rich text, lists, checklists, links, images, tables, find, undo, redo, and keyboard shortcuts
- Sections, page setup, headers and footers, page breaks, and paginated layout
- Local Room database with autosave, recovery checkpoints, and version history
- Password-encrypted whole-library backup and previewed restore with conflict handling
- Import from `.slx`, `.slxf`, TXT, Markdown, and DOCX
- Export to `.slx`, `.slxf`, TXT, Markdown, DOCX, and PDF; system share and print support
- Manual native updater with SHA-256 verification and Android signature enforcement
- CI-built ARM64 and x86_64-compatible APKs

## File formats

| Format | Purpose |
| --- | --- |
| `.slx` | Portable rich-note document for Note-compatible content |
| `.slxf` | Lossless Slate document for complete Forge page and layout features |
| `.slatebackup` | Authenticated, password-encrypted whole-library backup |

All imported documents open in the word-processing workspace. Exporting to `.slx` can simplify Forge-only layout, but never changes the original `.slxf` document.

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
- Word processing is the product focus after October 2026
- Preserve existing files while evaluating a common editable format
- No silent network activity
- Accessible across phone, tablet, keyboard, mouse, and touch

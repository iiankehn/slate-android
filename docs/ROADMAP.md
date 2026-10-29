# Slate roadmap

October 2026 is the final release with Notes. Future work focuses on one Android word processor. Notes code and existing-file access are preserved; the published October release is unchanged.

## Retained foundation (including historical Notes work)

- One package, database, library, application, and monthly release line
- Forge-derived interface for both Note and Forge documents
- Context-aware Note/Forge classification based on document structure
- Sticky promotion that protects Forge-only layout
- Rich-note `.slx` and lossless word-processing `.slxf` packages
- TXT, Markdown, DOCX, PDF, print, and share paths
- Responsive phone, tablet, and expanded-window workspace
- Local autosave, checkpoints, startup recovery, archive, trash, favorites, folders, tags, and search
- Guided multi-file import from the former Forge app with per-file results
- Manual verified updater
- Password-encrypted whole-library backup and restore with integrity validation, preview, and conflict handling
- Continuous canvas for notes, including formatted text, lists, and image controls
- Matching paragraph-editing offsets and embedded-object preservation during text replacement
- Contextual, dismissible Note-to-Forge guidance in the Insert and Layout ribbon tabs
- Start-center folder/tag editing and filters, saved text searches, and version-history preview/restore
- CI architecture inspection for ARM64 and x86_64 native libraries

## Next priorities

1. Complete word-processing accessibility, keyboard navigation, large-text and touch-target verification.
2. Close DOCX round-trip gaps and evaluate a common editable default before changing formats.
3. Benchmark large documents, image-heavy packages, pagination, startup, autosave and recovery.
4. Expand corrupt-package, migration, round-trip and updater security coverage.
5. Refine document templates, formatting, page layout, tables, images, printing and exports.

## Preserved, not developed further

Notes-specific canvas and classification code remain in the repository with tests. The Notes experience is disabled in subsequent development builds. A separate notes product or web app is deferred. Existing notes remain accessible in the paged workspace.

## Release gate

A public unified release requires a green GitHub Actions build, migration coverage for the existing Notes database, `.slx`/`.slxf` round-trip checks, architecture verification, signed upgrade testing, and manual smoke testing on compact and expanded layouts.

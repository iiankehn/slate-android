# Slate roadmap

This roadmap describes the unified product after the Notes and Forge codebases were consolidated in October 2025. It is ordered by product risk and user value, not by separate R1/R2 tracks.

## Delivered foundation

- One package, database, library, application, and monthly release line
- Forge-derived interface for both Note and Forge documents
- Context-aware Note/Forge classification based on document structure
- Sticky promotion that protects Forge-only layout
- Rich-note `.slx` and lossless word-processing `.slxf` packages
- TXT, Markdown, DOCX, PDF, print, and share paths
- Responsive phone, tablet, and expanded-window workspace
- Local autosave, checkpoints, startup recovery, archive, trash, favorites, folders, tags, and search
- Manual verified updater
- Password-encrypted whole-library backup and restore with integrity validation, preview, and conflict handling
- Continuous canvas for text-only notes, including formatted text and lists
- Start-center folder/tag editing and filters, saved text searches, and version-history preview/restore
- CI architecture inspection for ARM64 and x86_64 native libraries

## Next priorities

1. Unify object/text editing offsets and extend the continuous Note canvas to image-bearing notes.
2. Surface contextual Forge suggestions only when users approach page-layout actions.
3. Add a guided import assistant for installations of the former standalone Forge package.
4. Complete TalkBack labeling, focus order, large text, contrast, keyboard navigation, and touch-target audits.
5. Benchmark large documents, image-heavy packages, pagination, startup, autosave, and low-memory recovery.
6. Expand migration, corrupt-package, fuzz, round-trip, and updater security tests.
7. Add optional protected documents and encrypted exports without weakening local-first behavior.
8. Explore templates, backlinks, attachments, quick capture, and reminders after the core is stable.

## Release gate

A public unified release requires a green GitHub Actions build, migration coverage for the existing Notes database, `.slx`/`.slxf` round-trip checks, architecture verification, signed upgrade testing, and manual smoke testing on compact and expanded layouts.

# Slate R1 roadmap

R1 and R2 are distinct products. This roadmap tracks the focused R1 repository and does not treat R2 as R1's next version.

## Delivered in the September 2026 release

### Foundation

- Native Kotlin/Compose app, Material 3 theme, adaptive shell, launcher identity, Android 12 minimum support.
- Versioned rich-text document model and selection-aware editor commands.
- Room-backed library, immediate recovery journal, debounced current snapshots, and interrupted-write restoration.
- GitHub CI, unit tests, lint, debug artifacts, optimized signing workflow, checksums, and monthly release automation.

### Editor and documents

- Bold, italic, underline, H1, quote, links, lists, checklists, image references, simple table template, and find.
- Session undo/redo and local version restoration.
- Rename, duplicate, pin, favorite, archive, Trash, permanent delete, folders, tags, and search.
- Compact system Back behavior and keyboard-safe formatting controls.

### Interchange and platform integration

- Plain text, Markdown, and DOCX import/export with explicit R1 fidelity boundaries.
- PDF export, Android printing, plain-text sharing, file associations, and scoped file access.
- User-initiated native updater with checksum and signature enforcement.

### Release polish

- Flat Material 3 Expressive-inspired visual system replacing the early glass direction.
- Phone and expanded-window refinement, system-bar fixes, compact image previews, and documentation.
- Public `r1-2026-09` release with full install/update path.

## R1 maintenance priorities

- Respond to reproducible data-loss, recovery, import, export, navigation, and update issues first.
- Expand tests for interrupted writes, database migration, malicious documents, long documents, and large libraries.
- Continue accessibility review for screen readers, focus order, touch targets, keyboard navigation, and contrast.
- Measure editor performance before increasing format or attachment complexity.
- Improve fidelity only where it remains understandable, local-first, and dependable.

## Outside the R1 product boundary

Full desktop-class word processing, advanced page layout, broad architecture commitments, Googlebook Android specialization, and the complete multi-form-factor word-processing experience belong to Slate R2. R2 has its own purpose and monthly release line; it is not an R1 milestone or replacement.

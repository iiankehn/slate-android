# Roadmap

## 0.1 — Foundation

- Brand direction and adaptive Compose shell.
- Versioned document model.
- Initial rich-text ranges and working editor commands.
- Document actions for rename, duplicate, pin, archive, and delete.
- Durable Room-backed library and autosave journal. **Implemented in the 0.1 development foundation.**
- Plain-text and Markdown import/export. **Implemented.**
- CI, lint, unit tests, and debug APK.

## 0.2 — Editor

- Structured rich-text editing commands.
- Selection-aware formatting toolbar.
- Checklists, links, quotes, images, and basic tables. **Implemented.**
- Undo/redo, search within document, and keyboard shortcuts. **Implemented.**

## 0.3 — Documents

- Folders, tags, favorites, archive, trash, and full-text search. **Implemented.**
- Local version history and recovery UI. **Implemented.**
- Share sheet, printing, and PDF export. **Implemented.**

## 0.4 — Interoperability

- DOCX import/export with round-trip test fixtures. **Implemented for the R1 subset.**
- Clear fidelity reporting for unsupported constructs. **Implemented.**
- File association and import-on-open workflow where Android access permits it. **Implemented.**

## 0.5 — Adaptive polish

- Tablet, foldable, and resizable-window refinement.
- Keyboard, mouse, stylus, and accessibility audit.
- Performance work for long documents and large libraries.

## 0.9 — Release candidate

- Security and dependency audit.
- Data-loss, migration, recovery, malicious-file, and interruption testing.
- Branding, screenshots, documentation, updater, and release signing review.

## 1.0 — Stable

- Local-first notes and document editing with dependable import/export.
- ARM64 Android release, with expanded architecture support evaluated separately.

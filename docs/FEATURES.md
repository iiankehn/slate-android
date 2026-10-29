# Feature reference

## Unified library

| Capability | Behavior |
| --- | --- |
| Creation | New documents use the Forge word-processing workspace |
| Legacy compatibility | Existing notes open as documents; classification and renderer code are retained |
| Search | Matches title, body, folder, and tags |
| Organization | Pin, favorite, folder, tags, archive, trash, restore, and duplicate |
| Recovery | Autosave plus up to 30 local checkpoints per document |
| Import | Adds supported external documents to the same library |
| Library backup | Password-encrypted `.slatebackup` file with authenticated integrity checks |
| Library restore | Preview plus keep-both, replace-existing, or keep-newer conflict behavior |

## Editing

- Automatic Android light/dark appearance, including a theme-aware editing canvas that does not modify document output
- Collapsible phone formatting ribbon with a remembered compact/expanded state

- Rich text: bold, italic, underline, headings, quotes, links, lists, and checklists
- Media and structure: images and tables
- Document navigation: find, undo, redo, selection-aware editing, and keyboard shortcuts
- Forge structure: sections, page setup, headers, footers, page breaks, and paginated layout
- Input: touch, software keyboard, hardware keyboard, and mouse-friendly controls

## Product direction

October 2026 is the final Notes release. Subsequent builds use a paged workspace for all documents. Notes-specific code and format support are preserved, with no conversion of existing stored data required.

## Interchange

| Format | Import | Export | Fidelity |
| --- | --- | --- | --- |
| `.slx` | Yes | Yes | Portable rich-note model |
| `.slxf` | Yes | Yes | Full Slate word-processing model |
| TXT | Yes | Yes | Plain UTF-8 text |
| Markdown | Yes | Yes | Supported rich-text subset |
| DOCX | Yes | Yes | Practical interchange; advanced Word layout may simplify |
| PDF | No | Yes | Paginated output |
| Android share | No | Yes | Plain text through the system share sheet |
| Android print | No | Yes | PDF through the system print service |

## Platform behavior

- Android 12+
- Compact and expanded layouts
- Phone, tablet, Googlebook Android, portrait, and landscape targets
- ARM64 and x86_64 compatibility checks in CI
- System Back returns to the library before exiting from an open compact document
- Editor controls remain reachable when the software keyboard opens
- User-triggered native updater verifies file size, SHA-256, package identity, and Android signature

## Current limitations

- DOCX is an interchange format, not a claim of Microsoft Word layout parity.
- Exporting a Forge document to `.slx` may simplify page-only features; the source remains unchanged.

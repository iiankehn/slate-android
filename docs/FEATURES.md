# Feature reference

## Unified library

| Capability | Behavior |
| --- | --- |
| Adaptive creation | Starts without forcing a Note/Forge decision |
| Classification | Labels content Note or Forge from persisted structure |
| Search | Matches title, body, folder, and tags |
| Organization | Pin, favorite, folder, tags, archive, trash, restore, and duplicate |
| Recovery | Autosave plus up to 30 local checkpoints per document |
| Import | Adds supported external documents to the same library |

## Editing

- Automatic Android light/dark appearance, including a theme-aware editing canvas that does not modify document output
- Collapsible phone formatting ribbon with a remembered compact/expanded state

- Rich text: bold, italic, underline, headings, quotes, links, lists, and checklists
- Media and structure: images and tables
- Document navigation: find, undo, redo, selection-aware editing, and keyboard shortcuts
- Forge structure: sections, page setup, headers, footers, page breaks, and paginated layout
- Input: touch, software keyboard, hardware keyboard, and mouse-friendly controls

## Adaptive rules

Ordinary text, rich formatting, links, and simple content remain Note-compatible. Multiple sections, custom page setup, headers or footers, non-continuous section starts, structured tables, and forced page breaks require Forge. Length alone never triggers Forge, and Forge documents are not silently downgraded.

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
- The start center does not yet expose every folder, tag, saved-search, and history control supported by the data layer.
- Whole-library encrypted backup and guided migration from the former standalone Forge app remain roadmap items.

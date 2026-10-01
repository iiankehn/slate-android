# Slate R1 feature reference

This page describes the implemented R1 behavior in the current repository.

## Library and organization

| Capability | R1 behavior |
| --- | --- |
| Create | Creates an untitled local document and opens it on compact layouts |
| Rename | Edit the title directly or use **Rename** in the editor menu |
| Duplicate | Copies title, text, formatting, folder, and tags into a new active document |
| Pin | Sorts an active document ahead of ordinary documents |
| Favorite | Adds the document to the Favorites filter |
| Archive | Removes the document from Documents and places it in Archive |
| Trash | Recoverable deletion with a confirmation prompt |
| Permanent delete | Available from Trash and removes the stored document |
| Folder and tags | One folder string and a set of comma-separated tags per document |
| Search | Case-insensitive title, body, folder, and tag search within the selected library filter |
| History | Up to 30 local recovery checkpoints per document |

## Editor

| Tool | Selection behavior |
| --- | --- |
| Bold, italic, underline | Toggles the selected text; with no selection, targets the current word |
| H1 and quote | Targets the selected text or current line |
| List | Toggles a `• ` prefix on the selected/current line |
| Check | Toggles a `☐ ` prefix on the selected/current line |
| Link | Uses selected/current-word text as the label and stores the entered URL |
| Image | Stores a persisted Android document URI and shows a compact preview |
| Table | Inserts a two-column Markdown-style text template |
| Find | Selects the next case-insensitive match and wraps to the start |
| Undo/redo | Editor-session stacks; durable checkpoints remain in Version history |

## Keyboard shortcuts

| Shortcut | Action |
| --- | --- |
| `Ctrl+B` | Bold |
| `Ctrl+I` | Italic |
| `Ctrl+U` | Underline |
| `Ctrl+F` | Find in document |
| `Ctrl+Z` | Undo |
| `Ctrl+Y` | Redo |

## Formats

| Format | Import | Export | Notes |
| --- | --- | --- | --- |
| Plain text | Yes | Yes | UTF-8 text |
| Markdown | Yes | Yes | R1 subset; see fidelity notes below |
| DOCX | Yes | Yes | Basic paragraph interchange, not Word parity |
| PDF | No | Yes | Simple paginated text output |
| Android share text | No | Yes | Sends title as subject and body as plain text |
| Android print | No | Yes | Prints the generated PDF through the system service |

### Fidelity boundaries

- Markdown import recognizes H1, block quote, and `[label](url)` link syntax.
- Markdown export emits bold, italic, underline using HTML tags, H1, quote, and links.
- DOCX import extracts readable paragraph text and reports that advanced layout may be simplified.
- DOCX export writes the title and body as basic paragraphs.
- Images and table range metadata are internal editing aids and are not fully round-tripped through every export format.

## Saving and recovery

- Each edit creates an immediate checkpoint.
- The current document snapshot is debounced by 450 ms.
- Startup compares the stored snapshot with recovery entries and restores a newer checkpoint.
- History is bounded to 30 entries per document.
- App-private storage is removed by Android when Slate is uninstalled.

## Adaptive behavior

- Compact windows use library/editor navigation.
- Windows at least 840 dp wide use a library/editor split view.
- Android Back returns from the compact editor to the library.
- The formatting bar uses IME insets and horizontal scrolling so it remains usable with the software keyboard.

R1 is intended for Android 12+ phones and adaptive Android windows. Expanded architecture and form-factor commitments belong to the separate Slate R2 product.

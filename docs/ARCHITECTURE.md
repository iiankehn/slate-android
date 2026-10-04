# Architecture

Slate is a single-activity Kotlin and Jetpack Compose application. It uses unidirectional data flow: Compose renders state from `SlateViewModel`, actions update that state, and `DocumentRepository` persists normalized documents through Room.

## Runtime layers

| Layer | Responsibility |
| --- | --- |
| UI | Forge-derived start center, responsive editor, ribbon, dialogs, file pickers, and update prompts |
| State | Library operations, editor sessions, autosave, history, import/export, and adaptive classification |
| Models | Lightweight rich text plus device-independent word-processing sections and blocks |
| Editing | Immutable commands, selection-aware transforms, undo/redo, and flat-text adaptation |
| Layout | Page geometry and pagination independent of display form factor |
| Data | Room entities, snapshots, recovery checkpoints, and schema migrations |
| Formats | `.slx`, `.slxf`, TXT, Markdown, DOCX, PDF, printing, and sharing |
| Updater | Release discovery, version comparison, bounded download, SHA-256 verification, and installer handoff |

## Adaptive document state

Documents persist an experience value: `Adaptive`, `Notes`, or `Forge`. `DocumentExperiencePolicy` derives the visible mode from document structure. Adaptive content displays as Note until it contains an advanced feature; publishing such an edit persists Forge. Explicit or promoted Forge state is not automatically downgraded.

This policy is semantic, not heuristic: it does not inspect length, word count, screen size, or device class.

## Persistence and migration

- The app retains package `com.iiankehn.slate` and the existing Notes database.
- Room schema version 4 adds the experience field after the prior rich-document payload migration.
- Every edit creates a recovery checkpoint; the current snapshot saves after a short idle delay.
- Startup can recover a newer checkpoint than the stored snapshot.
- History is bounded to the newest 30 checkpoints per document.
- Android automatic backup and device transfer are disabled.

Uninstalling removes app-private documents. Users should export or back up documents they cannot replace.

## UI and navigation

`MainActivity` owns Compose content and supported Android `VIEW`/`EDIT` intents. Compact windows move between the start center and editor; expanded windows use the same responsive workspace. Android Back returns from an open document before exiting. IME padding keeps editor controls visible above the software keyboard.

## File and network boundaries

The Storage Access Framework supplies user-selected content URIs; Slate requests no broad storage permission. Imports are bounded and parsed off the UI thread. Package codecs validate entry counts, sizes, paths, and checksums before committing content.

Slate makes no routine network requests. The only built-in network path is the user-triggered update check against the official GitHub repository. Cleartext traffic is disabled.

## Build architecture

GitHub Actions is the canonical environment. If dependencies package native libraries, CI verifies both `arm64-v8a` and `x86_64`; pure JVM/Android bytecode is architecture-neutral. Production signing material exists only as encrypted repository secrets.

# Architecture

Slate R1 is a single-activity Android application built with Kotlin and Jetpack Compose. It uses unidirectional data flow: Compose renders observable state from `SlateViewModel`, UI actions update that state, and `DocumentRepository` persists normalized documents through Room.

## Runtime layers

| Layer | Current responsibility |
| --- | --- |
| UI | Adaptive document library, editor, formatting/actions, dialogs, Android file pickers, and update prompts |
| State holder | Library ordering, document operations, editor state, autosave scheduling, and history loading |
| Model | Version-independent document text plus normalized rich-text ranges |
| Data | Room entities, DAO, current snapshots, recovery checkpoints, and interrupted-write recovery |
| Format adapters | Plain text, Markdown subset, DOCX subset, PDF generation, printing, and sharing |
| Updater | Published-release discovery, version comparison, APK download, SHA-256 verification, and installer handoff |

## UI and navigation

`MainActivity` owns the Compose content and handles supported Android `VIEW`/`EDIT` file intents. `SlateApp` switches at 840 dp:

- compact windows show either the library or editor;
- expanded windows show a 360 dp library pane next to the editor;
- Android Back returns from the compact editor to the library before the activity exits;
- `imePadding` keeps the formatting dock above the software keyboard.

Material dialogs and popup menus consume Back before the screen-level handler.

## Document model

`RichTextDocument` stores text and a list of `RichTextRange` values. A range contains a style, start/end offsets, and optional data such as a URL or image URI. Normalization clamps invalid offsets, removes empty ranges, removes duplicates, and produces stable ordering.

Supported internal styles are bold, italic, underline, H1, link, quote, image, and table. Bullets and checklists are represented as line prefixes.

The internal model is independent of Compose spans and external file formats. Text edits remap range offsets so formatting survives ordinary insertion and deletion.

## Persistence and recovery

- Current document metadata, text, and style ranges are stored in an app-private Room database.
- Each edit creates a recovery checkpoint immediately.
- The current snapshot is saved after a 450 ms idle delay.
- Recovery history is capped at the newest 30 checkpoints per document.
- Startup recovery restores a checkpoint when it is newer than the stored snapshot.
- The library is exposed as a `Flow` and sorted by trash/archive status, pin, favorite, and update time.

Android automatic backup and device transfer are disabled, so app data is not silently copied to cloud backup. Uninstalling Slate removes its app-private documents; users should export anything they need before uninstalling.

## File boundaries

The Storage Access Framework supplies user-selected imports, exports, and image URIs. Slate does not request broad storage or media access.

- Text and Markdown imports are limited to 25 MB.
- DOCX import is limited to 25 MB, 2,000 ZIP entries, and 20 MB of decoded `word/document.xml`.
- Imports run off the UI thread and are committed only after parsing succeeds.
- Export writes through a user-selected content URI.
- Sharing sends plain text through the Android share sheet.
- PDF output is generated with Android's `PdfDocument`; printing uses the system print service.

DOCX is an interchange format, not Slate's source of truth. See [FEATURES.md](FEATURES.md) for current fidelity boundaries.

## Native updater

The updater is manual and has no background worker:

1. The user selects **Check for updates**.
2. Slate reads the latest published GitHub release.
3. It downloads `slate-r1-update.json` and compares its internal version code.
4. It downloads only the listed official-repository APK, with a 128 MB safety limit.
5. It verifies SHA-256 before exposing the file through a narrowly scoped `FileProvider`.
6. Android's package installer verifies the application signature and requests final confirmation.

Cleartext traffic is disabled. Draft and prerelease artifacts are not the normal update channel.

## Dependency policy

- Prefer AndroidX and Android platform APIs.
- Review runtime dependencies for privacy, maintenance, licensing, and attack surface.
- Do not add analytics or crash-reporting SDKs.
- Pin build versions and verify changes through CI.
- Keep signing material exclusively in GitHub Actions secrets.

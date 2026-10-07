# Unified Slate — October 2026 development release

Slate Notes and Slate Forge now share one application, library, editor, document model, and release line. This development release keeps the existing Slate Notes package and signing identity so installed Notes libraries can upgrade in place, while bringing the Forge-derived workspace and advanced word-processing model into that same app.

## What changed

- Replaced the separate-product architecture with one adaptive Slate app.
- Made the Forge-derived ribbon workspace the interface for both quick notes and complete documents.
- Added structure-aware Note and Forge classification. Ordinary rich text remains a Note; sections, custom page setup, headers, footers, structured tables, and page breaks promote the document to Forge. Document length is never used.
- Made Forge promotion persistent so Slate cannot silently discard advanced layout.
- Added full `.slxf` import/export alongside the portable `.slx` rich-note format.
- Brought the Forge editing engine, document blocks, page model, pagination, DOCX importer, responsive workspace configuration, and tests into the main package.
- Unified search, favorite, pin, archive, trash, restore, duplicate, import, export, print, share, and recovery paths.
- Preserved the existing Notes Room database and added a schema migration for the adaptive experience state.
- Retargeted the native updater and monthly release automation to the unified `iiankehn/slate-android` repository.
- Added CI inspection for ARM64 and x86_64 native-library coverage.
- Added native system-controlled light and dark themes across the library, workspace, controls, canvas, pages, tables, and embedded-object editors.
- Kept editor theme colors presentation-only so changing the Android theme cannot alter saved documents or exported page colors.
- Added a collapsible phone ribbon with always-available bold, italic, and underline actions plus a remembered compact/expanded preference.
- Made phone classification orientation-aware using Android's stable smallest-width identity so landscape phones and unusual display scaling no longer receive tablet chrome, and introduced a dense short-window editor with a compact title bar, reduced page margins, and nonessential ruler/status elements removed.
- Compacted the start center in short landscape windows so document creation, templates, search, and recent files remain reachable without oversized cards or headers.
- Rewrote product, format, privacy, architecture, release, contribution, security, and user documentation for one product.

## Migration

- Existing Slate Notes users install the signed unified APK over their current installation; documents remain in the upgraded local database.
- Users of the former standalone Slate Forge package should export `.slxf` or `.slx` files and import them into unified Slate. The two packages cannot directly share private Android data.

## Known follow-up work

The Forge start center still needs complete folder, tag, saved-search, and version-history controls. Encrypted whole-library backup, a guided standalone-Forge migration assistant, broader accessibility auditing, and large-document performance work remain on the roadmap.

This section documents work on the unified release branch; it does not claim that a signed public APK has already been published.

---

# Historical release: Slate R1 — September 2026

Slate R1 is the first official release of Slate by CORE: a private, local-first Android writing app designed to feel immediate for notes while remaining structured enough for longer documents.

**Release date:** September 30, 2026

**Release tag:** `r1-2026-09`

**Source commit:** `5c1d662`

**Minimum Android version:** Android 12 (API 31)
**Package:** `com.iiankehn.slate`

[View the official release and download the signed APK](https://github.com/iiankehn/slate-android/releases/tag/r1-2026-09)

## What is included

### A complete local document library

- Create and edit notes or longer documents without an account.
- Rename, duplicate, pin, favorite, archive, move to Trash, restore, or permanently delete.
- Organize documents with a folder and comma-separated tags.
- Search across titles, body text, folders, and tags.
- Switch between Documents, Favorites, Archive, and Trash.
- Keep pinned and favorite work prominent through deterministic library sorting.

### Rich writing tools

- Bold, italic, underline, H1, quote, and link styles apply to the selection or the current word/line where appropriate.
- Add bullet prefixes and checkboxes to lines.
- Insert image references through Android's document picker.
- Insert a simple two-column Markdown-style table template.
- Find the next matching phrase inside the open document.
- Undo and redo within the active editor session.
- Rename directly from the editor menu or edit the title inline.

### Keyboard and navigation behavior

- `Ctrl+B`, `Ctrl+I`, and `Ctrl+U` toggle common text styles.
- `Ctrl+F` opens Find.
- `Ctrl+Z` and `Ctrl+Y` provide undo and redo.
- The formatting dock stays above the software keyboard instead of moving off screen.
- On phones and compact windows, Android Back returns from the editor to the library before exiting Slate.
- Larger windows use an adaptive two-pane library/editor layout.

### Durable autosave and recovery

- Edits create immediate recovery checkpoints in an app-private Room database.
- The current snapshot saves after a short 450 ms idle delay.
- The header reports **Saving…** or **Saved locally**.
- Slate restores a newer recovery checkpoint after an interrupted write or process stop.
- Version history retains up to 30 checkpoints per document and lets the user restore an earlier title, body, folder, and tag state.

### Import, export, and Android integration

- Import UTF-8 plain text, Markdown, and DOCX through the Android Storage Access Framework.
- Open supported text, Markdown, and DOCX files from other Android apps where the system offers Slate.
- Export plain text, Markdown, DOCX, or PDF to a user-chosen location.
- Share document text through the Android share sheet.
- Print through Android's system print service.
- Select images with scoped URI access; no broad storage permission is requested.

### Material 3 interface polish

- Replaced the earlier translucent/glass direction with a flat Material 3 Expressive-inspired design.
- Retained Slate's Midnight surfaces and CORE blue `#0072BC` identity.
- Added Material text fields, chips, raised surfaces, clearer selection states, and consistent rounded geometry.
- Polished phone and large-window layouts, system bars, spacing, editor hierarchy, and document rows.
- Capped image previews so attachments do not displace the editor and formatting controls.

### Private by construction

- Documents stay in app-private storage unless the user explicitly imports, exports, prints, or shares.
- No account, advertising, analytics, telemetry, diagnostics upload, location, contacts, microphone, or camera access.
- Screenshot and recent-app preview capture are blocked while Slate is visible.
- Android automatic cloud backup and device transfer are disabled.
- Cleartext network traffic is disabled.

### Verified native updates

- **Check for updates** is available from the editor menu.
- Slate contacts GitHub only when the user requests a check; there is no background polling.
- The updater reads the latest published R1 release and compares a hidden monotonic Android version code while keeping the public product name `R1`.
- Downloads are restricted to the official `iiankehn/slate-android` release path, limited to 128 MB, and verified against the release SHA-256 manifest.
- Android independently enforces the package name, signing certificate, and final installation confirmation.
- Updates install over the existing app and preserve app data when the installed copy has the same official signing certificate.

## Engineering and release work

- Native Kotlin/Compose application with Android SDK 36 targeting and Android 12 minimum support.
- Room-backed persistence, normalized rich-text ranges, recovery payload encoding, and deterministic library state.
- Unit coverage for rich-text editing, title behavior, range payloads, and document format adapters.
- GitHub Actions verification on pushes and pull requests: unit tests, Android lint, and debug APK assembly.
- Optimized release builds with code/resource shrinking and protected signing secrets.
- Monthly R1 workflow that builds, signs, checks, and publishes an APK, `.sha256` file, and `slate-r1-update.json` manifest.

## Format fidelity and current limitations

Slate R1 favors dependable local writing over desktop publishing complexity.

- DOCX import reads paragraph text and intentionally simplifies advanced Word formatting, page layout, headers, footers, comments, tracked changes, and embedded objects.
- DOCX export writes a valid basic document containing the title and text paragraphs; it does not reproduce the complete internal style model.
- Markdown import recognizes H1 lines, block quotes, and inline links. Markdown export supports bold, italic, underline-as-HTML, H1, quote, and links; images and table range metadata are not separately serialized.
- PDF export uses a straightforward text layout rather than print-press pagination or typographic controls.
- Image support stores scoped document URIs and displays a compact preview; Slate is not an image-layout editor.
- Tables are a simple text template rather than resizable spreadsheet-like objects.
- Undo/redo is scoped to the current editor session; durable recovery is provided separately by version history.
- There is no cloud sync, collaboration, comments, tracked changes, or mandatory account.
- Uninstalling Slate removes app-private documents because automatic Android backup is disabled. Export important work before uninstalling.

## Installation and upgrading

1. Download `Slate-R1-2026-09.apk` from the official GitHub release.
2. If Android asks, allow the browser or file manager to install apps from that source.
3. Confirm the Android package-installer prompt.

To update an existing official Slate build, install the APK over it or use **Check for updates**. Do not uninstall first. If Android reports an incompatible signature, the installed copy was not signed with Slate's official release key; export its documents before replacing it.

Official APK SHA-256:

```text
555a427bbc48acbac97527948750e611b443162c4f95a9a47ede3d51465f94e6
```

## R1 is not R2

R1 and R2 are distinct Slate products, not increments in a shared version sequence. R1 is the focused local-first notes/document product delivered here. R2 is the separate full word-processing product planned for broader form factors, Googlebook Android support, and ARM64/x86_64 targets.

## Report feedback

- [Report a bug](https://github.com/iiankehn/slate-android/issues/new?template=bug_report.yml)
- [Request an R1 feature](https://github.com/iiankehn/slate-android/issues/new?template=feature_request.yml)
- Read the [security policy](https://github.com/iiankehn/slate-android/security/policy) before reporting a vulnerability.

Never attach private writing, credentials, signing material, or unredacted personal information to a public issue.

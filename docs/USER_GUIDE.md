# Slate user guide

## Start writing

Select **New document** from the branded start panel, choose a quieter template card, or select **Open** to import an existing file. Search and library filters appear once you have documents to organize. Slate opens new files adaptively, so there is no required Note/Forge choice.

The library badge shows **Note** while the file uses note-compatible content. If you add page setup, sections, headers, footers, structured tables, or page breaks, Slate preserves it as **Forge**. A long note remains a note.

## Use the workspace

The Forge-derived ribbon groups editing and document actions. On phones it starts as a compact dock with bold, italic, and underline controls; select **More** to reveal every ribbon tab and **Collapse** to return to the dock. Slate remembers that choice. The dock remains above the software keyboard. Tablets and larger windows keep the expanded ribbon and expose more controls at once.

Rotating a phone to landscape keeps the compact phone workspace instead of switching to tablet controls. Slate condenses the title and ribbon areas, removes the ruler and bottom status bar, and reduces on-page margins so the document receives most of the available height and width. Rotate back to portrait at any time without changing the document.

Slate follows Android's light or dark appearance automatically. The editor canvas, document pages, tables, and object controls change with the system theme for comfortable editing. This is a display preference only: it does not change the document's stored formatting or the white page used by exported and printed output.

Common keyboard shortcuts include `Ctrl+B`, `Ctrl+I`, `Ctrl+U`, `Ctrl+F`, `Ctrl+Z`, and `Ctrl+Y`.

## Organize documents

Use search to match titles, body text, folders, or tags. Library filters expose active documents, favorites, archive, and trash. A document menu provides pin, favorite, duplicate, archive, trash, restore, and permanent delete where applicable.

Choose **Folder & tags** from a document menu to assign a folder and comma-separated tags. Select an existing folder or type a new one; empty fields remove the corresponding organization. The library cards show folder and tag labels.

Expand **Folders, tags & saved searches** below search to filter by an exact folder or tag. These filters combine with the selected library section and search text. Use **Clear filters** to remove folder and tag restrictions. **Save current search** remembers the search text on this device; selecting a saved search clears folder and tag restrictions and runs that text search in the current section. **Remove** deletes only the saved search.

Choose **Version history** from a document menu to preview dated local checkpoints. Select **Restore this version**, then confirm to restore its content and organization as a new edit. The document stays in the library; this does not restore unrelated documents.

Permanent deletion cannot be undone. Ordinary deletion moves a document to Trash first.

## Import and export

Slate imports `.slx`, `.slxf`, TXT, Markdown, and DOCX through Android's system picker. Importing does not overwrite the source file.

Use `.slx` for portable Note-compatible content and `.slxf` for complete Forge fidelity. TXT and Markdown are useful for open text interchange; DOCX is practical interchange with other word processors; PDF and print are presentation outputs.

When exporting Forge content as `.slx`, page-specific features may simplify in the exported copy. The original document is not changed.

## Back up and restore the library

Open **Library → Encrypted backup** from the start center, or use **File → Library → Backup** in the editor. Choose and confirm a passphrase of at least eight characters, then save the `.slatebackup` file through Android's system picker. The file includes active, archived, and trashed documents plus embedded Slate document images.

To restore, choose **Restore backup**, select the file, and enter its passphrase. Slate verifies the encrypted container and every embedded `.slxf` package before showing a preview. If document identities overlap, choose whether to keep both copies, replace the current items, or retain whichever version is newer.

Slate cannot recover a forgotten backup passphrase. Keep the passphrase somewhere safe and separate from the backup file.

## Recover work

Slate stores local checkpoints while you edit and saves the current snapshot after a short idle delay. Open **Version history** from a document menu in the start center to restore a prior checkpoint. After an interrupted write, Slate can recover a newer checkpoint during startup.

## Update Slate

Choose **Check for updates**. Slate contacts the official GitHub release endpoint only after this action, downloads the declared APK, verifies its SHA-256, and opens Android's installer. Android may ask once for permission to install updates from Slate, then confirms the replacement.

An official update replaces the installed version without deleting the library because it uses the same package and signing key. Never uninstall merely to update; uninstalling removes app-private data.

## Migrate from the former apps

- Existing Slate Notes: install the unified signed APK over the current app. Its database migrates in place.
- Standalone Slate Forge: export each document as `.slxf` or `.slx`, install/open unified Slate, then import those files. The old app used another package and cannot share its private database directly.

Keep exports until you have verified every important document in unified Slate.

## Report a problem

Open a [GitHub issue](https://github.com/iiankehn/slate-android/issues/new/choose) with the Slate version, Android version, device/form factor, document format, expected result, actual result, and repeatable steps. Remove private text and personal files before sharing logs or samples.

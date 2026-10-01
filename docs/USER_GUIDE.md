# Slate R1 user guide

## Install

Download the APK from the [latest official release](https://github.com/iiankehn/slate-android/releases/latest), open it, and approve Android's installer prompt. Android may first ask you to allow installs from the browser or file manager that opened the APK.

Slate requires Android 12 or newer. An account and network connection are not required for writing.

## Use the library

- Tap **New** to create a document.
- Tap a document row to open it.
- Use **Search documents** to search titles, text, folders, and tags.
- Use the chips to switch between Documents, Favorites, Archive, and Trash.
- Tap **Import** to choose a plain-text, Markdown, or DOCX file.

On phones, use the editor's arrow or Android Back to return to the library. On larger windows, the library and editor appear together.

## Write and format

Edit the title at the top of the page and write in the body below it. Select text before using Bold, Italic, or Underline. With no selection, Slate uses the current word for inline styles and the current line for H1 or Quote.

The formatting dock scrolls horizontally and remains above the software keyboard. Available tools are:

- **B**, **I**, **U**, and **H1**
- **List** and **Check** line prefixes
- **Quote** and **Link**
- **Image** and a simple **Table** template
- **Find**, Undo, and Redo

Hardware keyboards support `Ctrl+B`, `Ctrl+I`, `Ctrl+U`, `Ctrl+F`, `Ctrl+Z`, and `Ctrl+Y`.

## Organize and manage a document

Open the three-dot editor menu to:

- rename, favorite, pin, duplicate, archive, or move the document to Trash;
- assign a folder and comma-separated tags;
- open version history;
- export, share, or print;
- check for Slate updates.

Trash is recoverable. Open the Trash filter, select the document, and choose **Restore from trash**. **Delete permanently** cannot be undone through the normal library.

## Saving and version history

Slate saves locally while you work. The editor header shows **Saving…** during a pending write and **Saved locally** afterward.

Every edit also produces a recovery checkpoint. Open **Version history** to restore one of the newest 30 checkpoints. A restore changes the current document and is itself saved as a new edit.

Android backup and device transfer are disabled for Slate. Export important documents before uninstalling or clearing app storage.

## Import and export

### Import

Use **Import** in the library or open a supported file from another Android app. Imports are limited to 25 MB. DOCX files with unusually large or complex archives are rejected for safety.

### Export

The editor menu can export:

- `.txt` plain text;
- `.md` Markdown;
- `.docx` basic Word-compatible paragraphs;
- `.pdf` simple paginated text.

Android's file picker chooses the destination. **Share** sends plain text to another app. **Print** opens the system print service.

See [FEATURES.md](FEATURES.md) for format-fidelity details.

## Add images

Tap **Image** and choose an image through Android's document picker. Slate keeps a scoped reference to the selected file and shows a compact preview. Moving, deleting, or revoking access to the original image may make the preview unavailable.

## Update Slate

Open the editor menu and select **Check for updates**. Slate contacts the official GitHub release channel only after this action.

When an update is available:

1. Approve Slate as an installation source if Android asks.
2. Slate downloads the APK and verifies its SHA-256 checksum.
3. Android verifies the app signature and displays the final update prompt.
4. Install over the existing copy; do not uninstall first.

Documents remain in place during a correctly signed in-place update. Draft releases are not offered by the updater.

## Troubleshooting and feedback

Before reporting a bug, note the Android version, device model, window/form factor, Slate release, and exact steps that reproduce the problem. Remove private writing from screenshots or recordings.

Use the repository's [issue forms](https://github.com/iiankehn/slate-android/issues/new/choose). For security vulnerabilities, follow [SECURITY.md](../SECURITY.md) and do not publish exploit details.

# Slate Notes by CORE

Slate Notes (R1) is a private, local-first notes and document app for Android. It combines the speed of a notes app with document organization, rich-text tools, file interchange, recovery history, and a native update path—without requiring an account.

Visit the [Slate website](https://slate.iiankehn.com/) to compare the distinct R1 and R2 products, open their repositories, and find official downloads.

## Get Slate Notes

- [Download the latest signed APK](https://github.com/iiankehn/slate-android/releases/latest)
- Requires Android 12 or newer (API 31+)
- Package: `com.iiankehn.slate`

When installing the APK for the first time, Android may ask you to allow installs from the app that opened the download. Future releases can be installed from **Editor menu → Check for updates**. Android always shows the final installation confirmation.

## Highlights

### Writing and editing

- Selection-aware bold, italic, underline, H1, quote, and link formatting
- Bulleted lists, checklists, image references, and simple text tables
- Find in document, editor undo/redo, and hardware-keyboard shortcuts
- Keyboard-safe formatting dock that remains available while typing
- Continuous local saving with a visible saved/saving indicator

### Document library

- Create, rename, duplicate, pin, favorite, archive, trash, restore, and permanently delete documents
- Organize with folders and tags
- Search titles, document text, folders, and tags
- Separate Documents, Favorites, Archive, and Trash views
- Up to 30 local recovery checkpoints per document with version restoration

### Files and Android integration

- Import `.slx`, plain text, Markdown, and DOCX files
- Export `.slx`, plain text, Markdown, DOCX, and PDF
- Continue a rich-text `.slx` document directly in Slate Forge with embedded media intact
- Share through Android, print through the system print service, and open supported file associations
- Scoped file access without broad storage permission

### Native Android experience

- Native Kotlin and Jetpack Compose
- Flat Material 3 Expressive-inspired interface with Slate's Midnight and CORE-blue identity
- Compact phone navigation plus an adaptive two-pane layout for larger windows
- System Back returns from the phone editor to the library before exiting
- Formatting controls remain visible when the software keyboard opens

### Privacy and updates

- Writing is stored in an app-private Room database on the device
- No account, ads, analytics, telemetry, diagnostics upload, contacts, location, microphone, or camera access
- Network access is used only after the user selects **Check for updates**
- Update APKs are checked against the release SHA-256 manifest; Android also enforces the signing certificate
- No background update polling

See [Features](docs/FEATURES.md), the [User guide](docs/USER_GUIDE.md), and the full [R1 release notes](RELEASE_NOTES.md).

## Slate Notes and Slate Forge

Slate Notes and Slate Forge are distinct products for different purposes. R1 and R2 remain their internal identifiers, not sequential upgrades or release increments.

- **Slate Notes (R1)** is the focused, local-first notes and document experience in this repository.
- **Slate Forge (R2)** is the separate full word-processing product intended for broader form factors, Googlebook Android support, and ARM64/x86_64 targets.

Each product follows its own monthly release line.

## Build and verify

The project is pinned to JDK 17, Gradle 8.13, Android Gradle Plugin 8.13.2, Kotlin 2.3.10, and compile/target SDK 36.

With a compatible Android SDK installed:

```bash
gradle --no-daemon testDebugUnitTest lintDebug assembleDebug
```

GitHub Actions runs that verification for pushes to `main` and `beta`, and for pull requests. Successful CI runs retain a debug APK artifact. The monthly release workflow builds the optimized signed APK and its checksum/update manifest.

## Documentation

| Document | Purpose |
| --- | --- |
| [Release notes](RELEASE_NOTES.md) | Complete R1 release summary, installation, fixes, and limitations |
| [Features](docs/FEATURES.md) | Detailed capability and format-support reference |
| [User guide](docs/USER_GUIDE.md) | Installation, everyday use, shortcuts, files, recovery, and updates |
| [Product brief](docs/PRODUCT.md) | Product scope, principles, and the R1/R2 boundary |
| [Architecture](docs/ARCHITECTURE.md) | UI, state, persistence, format, and updater design |
| [Slate formats](docs/SLATE_FORMATS.md) | Shared `.slx`, Forge-native `.slxf`, compatibility, security, and handoff contract |
| [Privacy](docs/PRIVACY.md) | Data handling, permissions, network behavior, and security baseline |
| [Visual identity](https://github.com/iiankehn/slate-r2-android/blob/main/docs/BRAND.md) | Shared Slate family mark, colors, and usage rules |
| [Releasing](docs/RELEASING.md) | Signing, monthly workflow, version codes, and publication process |
| [Roadmap](docs/ROADMAP.md) | Completed R1 milestones and future maintenance areas |
| [Contributing](CONTRIBUTING.md) | Development workflow and pull-request expectations |
| [Security](SECURITY.md) | Private vulnerability-reporting guidance |

## Report a problem or request a feature

Use [GitHub Issues](https://github.com/iiankehn/slate-android/issues/new/choose) and select the appropriate form.

For bugs, include:

- Android version, device/model, and phone/tablet/foldable form factor
- The Slate release and installation source
- Exact reproduction steps, expected result, and actual result
- Whether the problem risks document loss or blocks access to a document
- Screenshots or a short recording when useful, after removing private writing and personal information

Do not post private documents, signing material, credentials, or exploit details in a public issue. Follow [SECURITY.md](SECURITY.md) for vulnerabilities.

## Product principles

1. Writing never depends on a connection or account.
2. The device copy is authoritative.
3. Autosave and recovery are core features.
4. Common file formats stay at the boundary; Slate's versioned document model remains independent.
5. No telemetry, advertising identifiers, behavioral analytics, or experiments.
6. Phone, tablet, keyboard, and touch behavior are designed together.

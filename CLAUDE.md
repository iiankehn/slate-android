@../brain/CORE.md

# Slate by CORE

Private, local-first Android writing app: Slate Notes and Slate Forge unified in
one package, one database and one monthly release line. Shared CORE context is
imported above from the `brain` repo; if it is missing, ask the user to clone
`iiankehn/brain` beside this repo.

## Commands

```bash
gradle --no-daemon testDebugUnitTest lintDebug assembleDebug   # what CI runs (Gradle 8.13, JDK 17)
gradle --no-daemon testDebugUnitTest --tests 'com.iiankehn.slate.io.*'   # a focused subset
```

There is no Gradle wrapper; `CONTRIBUTING.md`'s `./gradlew` is out of date. A
build needs the Android SDK; if it is not available, say so and rely on the
**Android CI** workflow rather than claiming tests passed.

## Layout (`app/src/main/java/com/iiankehn/slate/`)

| Package | Responsibility |
|---|---|
| `ui/` | Start center, responsive editor, ribbon, dialogs |
| `SlateViewModel.kt` | State, editor sessions, autosave, import/export |
| `model/` | Rich text, word-processing document, `DocumentExperiencePolicy` |
| `editing/` | Immutable commands, selection transforms, undo/redo |
| `layout/` | Page geometry and pagination |
| `data/` | Room (`SlateDatabase` v4), repository, checkpoints, backup |
| `io/` | `.slx`, `.slxf`, TXT, Markdown, DOCX, PDF codecs |
| `update/` | Manual verified GitHub updater |

Docs: `docs/ARCHITECTURE.md`, `docs/ROADMAP.md`, `docs/RELEASING.md`, `docs/SLATE_FORMATS.md`.

## Rules

- Never change package `com.iiankehn.slate`, the signing identity or the Notes
  database upgrade path. Migrations are additive; no destructive fallback.
- Note vs Forge is structural only (never length or screen size). Forge never
  downgrades silently.
- File input is untrusted: keep size, path, entry-count and checksum limits.
- No new network paths or permissions. The only network use is the
  user-triggered update check.
- Keep models free of device dimensions and Compose spans; unidirectional state flow.
- Add focused unit tests for editing commands, migrations, codecs, classification
  and layout changes.
- Commit messages: Conventional Commits (`feat:`, `fix:`, `ci:`, `test:`, `release:`).

## Skills

- `slate-release`: prepare the monthly `slate-YYYY-MM` release.
- `slate-db-migration`: change the Room schema safely.

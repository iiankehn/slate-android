# Contributing to Slate

Slate is one adaptive Android writing app. Contributions should strengthen the shared foundation instead of recreating separate Notes and Forge products.

## Before opening a change

- Search existing issues and the roadmap.
- Keep behavior local-first and avoid unnecessary permissions or network paths.
- Preserve the Notes package ID, upgrade path, and database migrations.
- Keep Note/Forge classification structural; never use document length as a signal.
- Avoid silently downgrading Forge content.
- Discuss large format, schema, dependency, or product changes in an issue first.

## Build and test

```bash
./gradlew test lint assembleDebug
```

GitHub Actions is canonical and also checks native-library architecture coverage. Add focused tests for editing commands, migration rules, codecs, classification, and layout behavior when changing those areas.

## Code expectations

- Kotlin and Compose code should follow existing unidirectional state flow.
- Keep models independent of device dimensions and Compose-specific spans.
- Treat file input as untrusted and preserve existing size/path/checksum limits.
- Make compact, expanded, keyboard, mouse, touch, and accessibility behavior explicit.
- Never commit keystores, credentials, production APKs, or private documents.

## Pull requests

Describe the user-visible outcome, implementation boundaries, tests, migration implications, and known limitations. Screenshots are useful for visual changes but must contain no private content.

By contributing, you agree that your work is provided under the repository's license.

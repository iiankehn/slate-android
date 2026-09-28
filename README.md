# Slate by CORE

Slate is a private, local-first writing app for Android. It is designed to be as immediate as a notes app and structured enough for complete documents.

## Foundation status

The `0.1.0-dev` groundwork includes:

- a native Kotlin and Jetpack Compose application shell;
- compact phone navigation and an adaptive two-pane tablet/laptop layout;
- an editable in-memory document prototype;
- the Midnight and CORE Glass visual direction using CORE blue `#0072BC`;
- no network, advertising, location, contacts, or analytics permissions;
- Android 12 (API 31) as the minimum supported release;
- a GitHub Actions verification workflow for `main` and `beta`;
- initial unit tests and lint/build gates.

The current launcher mark is deliberately a placeholder. Final Slate identity work should be completed before a public beta.

## Build

The project is pinned to JDK 17, Gradle 8.13, and Android Gradle Plugin 8.13.2. GitHub Actions can do the heavy build work:

1. Create a public repository, recommended name `slate-android`.
2. Push this project to `main`.
3. GitHub Actions builds and uploads `slate-debug-apk`.

For a local build with a compatible Android SDK:

```bash
gradle testDebugUnitTest lintDebug assembleDebug
```

## Product principles

1. Writing must never depend on a connection or account.
2. The device copy is authoritative.
3. Autosave and recovery are core features, not polish.
4. Common formats belong at the boundary; Slate's internal document model stays independent.
5. No telemetry, diagnostics, advertising identifiers, behavioral analytics, or experiments.
6. Tablet and keyboard behavior is designed alongside phone behavior.

See `docs/` for the product brief, architecture, privacy baseline, and roadmap.

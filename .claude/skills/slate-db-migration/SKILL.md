---
name: slate-db-migration
description: Change Slate's Room database schema without losing user data — version bump, additive migration for documents and recovery_entries, and tests. Use when adding or changing entity fields in app/src/main/java/com/iiankehn/slate/data.
---

# Change the Slate database schema

Existing Slate Notes users upgrade in place. A broken migration destroys their
documents, so this is the highest-risk change in the app.

## Rules

- Bump `version` in `@Database` (`data/SlateDatabase.kt`) by exactly one and add
  `MIGRATION_<n>_<n+1>` to `addMigrations(...)`. Never use
  `fallbackToDestructiveMigration`.
- Migrations are additive: `ALTER TABLE ... ADD COLUMN ... NOT NULL DEFAULT ...`
  with a default that preserves current behavior. Renames and type changes need
  a create-copy-swap migration and a discussion first.
- `documents` and `recovery_entries` carry the same document fields. A new
  document field goes into **both** tables, entities and the mapping in
  `DocumentRepository`, so recovery checkpoints restore it.
- Match the entity exactly (column name, affinity, nullability, default) or
  Room fails validation at startup on upgraded installs.
- Consider the export formats: if the field is user data, decide whether
  `.slx`, `.slxf` and the encrypted library backup must carry it, and update
  `docs/SLATE_FORMATS.md`.

## Tests

- Add unit tests for any new mapping or codec behavior.
- There is no Room migration test harness yet (`exportSchema = false`). If the
  change is non-trivial, propose enabling schema export plus a
  `MigrationTestHelper` instrumentation test rather than shipping it untested.
- Run `gradle --no-daemon testDebugUnitTest lintDebug assembleDebug`.

## Update the docs

Update the schema version line in `docs/ARCHITECTURE.md` and add a
migration-implications section to the PR.

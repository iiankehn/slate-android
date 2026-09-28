# Architecture

## Direction

Slate uses a single-activity Compose UI with unidirectional data flow. A repository will expose documents as observable state; the editor sends explicit actions back through its state holder. The local data source remains authoritative.

## Planned layers

| Layer | Responsibility |
| --- | --- |
| UI | Adaptive library, editor, settings, file picker, and accessibility semantics |
| Domain | Document operations, search, formatting commands, versioning, and import/export contracts |
| Data | Local database metadata, document body storage, attachments, and recovery journal |
| Format adapters | DOCX, Markdown, text, HTML clipboard, and PDF/printing boundaries |

## Storage model

- Document metadata belongs in Room after the prototype phase.
- Document content uses a versioned Slate document model rather than storing Android UI spans.
- Attachments use app-private files addressed by stable identifiers.
- Every committed edit produces a recoverable journal checkpoint before compaction.
- The Storage Access Framework handles user-selected imports and exports; broad storage permission is not required.

## Format independence

DOCX is an interchange format, not Slate's internal source of truth. Importers translate supported constructs into the internal model and preserve unsupported content where practical. Exporters report fidelity limits instead of silently discarding content.

## Dependency policy

- Prefer AndroidX and platform APIs.
- Every runtime dependency requires a privacy, maintenance, license, and attack-surface review.
- No analytics or crash-reporting SDK.
- Pin build versions and update through reviewed pull requests.

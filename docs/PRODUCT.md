# Product definition

Slate is a private, local-first Android word processor. October 2026 is the final release to offer the Notes experience. Subsequent development and releases focus on one paged word-processing workspace for letters, reports, manuscripts and other formatted documents.

## Preserve existing work

The Notes renderer, adaptive classification, import/export code and tests remain in this repository. `NOTES_EXPERIENCE_ENABLED` in `CanvasPresentation.kt` disables that experience in subsequent development builds; it does not delete source or migrate stored documents. New documents use Forge. Previously created notes open in the word-processing workspace with their content retained.

Published October release tags and artifacts remain historical releases. This direction does not retroactively change them.

## Product priorities

- Reliable text editing, formatting, lists, tables and images
- Page setup, sections, headers/footers, pagination, print and PDF export
- Document compatibility and explicit handling of unsupported features
- Responsive phone, tablet and larger Android-window layouts
- Local autosave, recovery, version history, library organization and encrypted backup
- Accessible touch, keyboard and mouse interaction

## Formats

Existing `.slx` and `.slxf` support is retained. A common editable format, particularly DOCX, is under consideration. No change to the default portable format has been approved or implemented. See [compatibility assessment](FORMAT_COMPATIBILITY.md) for current converter gaps and migration gates.

## Foundation

One Android package (`com.iiankehn.slate`), one Room database, one library and one monthly release line. Android 12+, ARM64 and x86_64 remain supported targets. The app requires no account and has no telemetry, background update polling or required cloud service.

## Deferred

Notes-specific development stops after October. A possible future notes product or web app is separate exploratory work, with no implementation commitment. Quick capture, backlinks and reminders are outside the current word-processor roadmap.

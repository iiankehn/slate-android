# Product definition

Slate is one adaptive Android writing product. It replaces the former plan for separate Slate Notes and Slate Forge applications with a shared library, editor, document model, formats, and release line.

## Product promise

Open Slate and begin writing immediately. The workspace stays suitable for a note until the document actually uses page-layout structure, then preserves the complete Forge model without asking the user to choose an app first.

## Experiences

| Experience | Intended work | Selection rule |
| --- | --- | --- |
| Note | Quick notes, lists, journals, research, and ordinary rich text | Default for adaptive documents without advanced structure |
| Forge | Letters, reports, manuscripts, and page-structured documents | Explicitly selected or required by advanced structure |

Advanced structure means multiple sections, non-default page setup, headers or footers, non-continuous section starts, structured tables, or forced page breaks. Length and word count are deliberately excluded.

Promotion to Forge is sticky. Slate will not silently downgrade a file and risk discarding layout information.

## Shared foundation

- one Android application and package: `com.iiankehn.slate`;
- one library and Room database;
- one Forge-derived responsive interface;
- one adaptive document policy;
- `.slx` for portable rich notes and `.slxf` for full-fidelity documents;
- one monthly release line using calendar and commit-derived versions;
- ARM64 and x86_64 compatibility;
- local-only storage unless the user explicitly imports, exports, shares, prints, or checks for updates.

## Supported form factors

Slate targets Android 12+ phones, tablets, large and resizable Android windows, Googlebook Android environments, keyboard/mouse workflows, and both portrait and landscape use.

## Non-goals

- Recreating two independently installed products
- Choosing Forge merely because a document is long
- Requiring an account or proprietary cloud service
- Advertising exact Microsoft Word layout parity
- Automatic background update checks

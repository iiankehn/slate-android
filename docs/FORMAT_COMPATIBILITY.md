# Common-format assessment

Status: proposal under consideration; no format-default or migration change is approved.

The direction being evaluated is DOCX for formatted documents, Markdown for simpler notes, and PDF for presentation exports. Existing `.slx` and `.slxf` files must remain readable during any transition. The local database and encrypted library backup are separate from the choice of portable document format.

## Current code findings

| Path | Current limitation | Required before using it as the normal editable save format |
| --- | --- | --- |
| DOCX sections | Import combines body blocks into one section and uses the last section properties | Preserve section boundaries, per-section page setup, headers and footers |
| DOCX lists | Import reads `numPr` as numbered lists without resolving `numbering.xml` | Preserve bullets, numbering, nesting, restarts and checklist state or disclose unsupported features |
| DOCX images | Import does not restore dimensions, offsets or wrapping; export emits inline pictures | Verify image content, dimensions, descriptions and supported placement in both directions |
| DOCX links | Import resolves links; run export does not write hyperlink relationships | Preserve link targets through export and reopen |
| Markdown formatting | Export emits bold and italic markers, but import does not parse them | Choose a supported Markdown dialect and make the supported features round-trip |
| Markdown images/tables | Export ignores image and table ranges | Define portable asset handling and limitations; never silently discard content |

These findings come from `DocumentFormats.kt` and `DocxImporter.kt`; they describe this implementation, not limitations of the external formats.

## Migration gates

1. Agree on the default format and the supported feature set.
2. Add export/reopen tests for all supported document features, plus fixtures produced by other editors.
3. Add visible warnings before saving a feature that cannot be preserved.
4. Keep original files, import as new copies, and verify conversion before offering cleanup.
5. Validate interoperability with independent readers; a Slate-to-Slate test alone is insufficient.

Changing portable file formats must not remove local autosave, recovery, version history, library organization or encrypted backup. Whether those app features travel with an exported file needs an explicit design decision.

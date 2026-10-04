# Slate document formats

Slate uses two related packages so quick notes remain portable while full word-processing structure remains lossless.

## `.slx` — portable rich note

`.slx` stores note-compatible text, normalized rich ranges, metadata, and checked assets in a bounded package. It is the preferred editable format for notes and interchange that does not require page layout.

Typical content includes headings, emphasis, quotes, links, lists, checklists, and portable media references. Importing an `.slx` file normally classifies it as Note.

## `.slxf` — full Slate document

`.slxf` stores the complete word-processing model, including sections, page setup, headers, footers, section starts, structured blocks, and page-break behavior. Importing an `.slxf` file classifies it as Forge.

The package contains a manifest, the encoded document model, and optional checked assets. Paths, entry counts, decoded sizes, and checksums are validated before content is accepted.

## Adaptive selection

The extension is an interchange promise, while the in-app label is derived from actual structure. New documents begin Adaptive. Slate presents them as Note until advanced structure requires Forge, then persists Forge to prevent accidental loss. Text length has no role in classification.

## Conversion

- Forge to `.slxf`: lossless native export.
- Note to `.slx`: portable native export.
- Forge to `.slx`: creates a compatible copy and may simplify page-only features.
- `.slx` to Forge: Slate can promote the imported document when advanced features are added.

Conversions never silently modify the source document.

## Other formats

TXT and Markdown prioritize readable text. DOCX provides practical exchange with common word processors but may simplify advanced layout. PDF and printing produce presentation output rather than editable Slate source.

## Safety limits

Codecs bound total bytes, individual text values, collection sizes, archive entries, decoded XML, and asset paths. Imports complete parsing before they enter the library. A malformed or oversized package should fail without replacing existing content.


---
name: slate-release
description: Prepare and publish Slate's monthly release (slate-YYYY-MM) — release notes in docs/releases, the pre-release checklist, the draft created by the scheduled workflow, and the manual publish. Use when asked to release, ship or write release notes for Slate.
---

# Slate monthly release

`docs/RELEASING.md` is authoritative. Identity: tag `slate-YYYY-MM`, APK
`Slate-YYYY-MM.apk`, version name `YYYY.MM+COMMIT`, manifest `slate-update.json`.

## 1. Release notes (on a branch, before the 1st of the month)

- Create `docs/releases/YYYY-MM.md` using `docs/releases/2026-10.md` as the
  model: title `# Slate — Month YYYY`, a summary paragraph, then Highlights,
  Files and interoperability, Local-first by design, and known limitations.
- Build the change list from `git log slate-<previous>..main --oneline`
  (`feat:` and `fix:` commits) and describe user-visible behavior, not code.
- If the file is missing, the workflow falls back to `RELEASE_NOTES.md`. Keep
  that file current too.
- Update `docs/ROADMAP.md` "Delivered foundation" and `README.md` highlights if
  a feature graduated.

## 2. Pre-release checklist

Copy the checklist from `docs/RELEASING.md` into the PR or ask the user to
confirm each item. Items like signed upgrade testing and device smoke tests are
manual: never mark them done yourself.

## 3. Build and publish

- The **Slate Monthly Release** workflow runs on the 1st at 15:00 UTC and
  creates or refreshes a **draft** release. A manual dispatch without
  `publish` does the same.
- Publishing is public and permanent: **confirm with the user**, then dispatch
  with `publish: true`.
- Re-running in the same month moves the tag to the new commit and replaces
  assets. Make sure that is intended.

## 4. Verify

- The release has the APK, `.apk.sha256` and `slate-update.json`.
- The manifest `versionCode`, `sizeBytes` and `sha256` match the APK, and
  `apkUrl` points at this tag.
- The workflow summary shows package `com.iiankehn.slate` and the expected certificate.

Never publish a lower version code or an APK signed with another key. Fix forward.
Afterwards follow `../brain/playbooks/ship-a-release.md` and update
`../brain/projects/slate.md`.

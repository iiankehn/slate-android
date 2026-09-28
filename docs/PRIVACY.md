# Privacy and security baseline

Slate is private by construction.

- No telemetry, diagnostics upload, analytics, ads, identifiers, experiments, or sponsored content.
- No network permission in the base application.
- No contacts, location, microphone, camera, or broad storage permission.
- User writing stays in app-private storage unless the user explicitly imports, exports, shares, or later enables an optional sync provider.
- Automatic Android cloud backup and device transfer are disabled at the foundation stage to prevent an unclear copy of private writing. A future encrypted backup design requires an explicit product decision.
- External files are treated as untrusted input. Import is size-limited, parsed off the UI thread, and validated before commit.
- Export and sharing use Android's scoped file APIs and temporary grants.
- Release builds use shrinking and static verification; GitHub workflows receive read-only repository permissions by default.

Before beta, add threat-model tests covering malicious documents, oversized attachments, interrupted writes, recovery, clipboard leakage, screen capture policy, and device-lock behavior.

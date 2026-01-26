# Vidown 📥🎬

Vidown is a modern Android application focused on media downloading, built with
Jetpack Compose, Hilt, Room, and native downloader tooling.

This repository uses **modular documentation** instead of a single bloated README.

---

## 🧱 Tech Stack

- Kotlin + Jetpack Compose
- Hilt (DI) + KSP
- Room (offline persistence)
- Coroutines + Flow
- Native downloader binaries (yt-dlp stack)
- Android 13+ permission model

---

## 📚 Documentation Index

### Build & Tooling
- [Gradle setup](docs/gradle/README.md)
- [Version Catalog](docs/gradle/version-catalog.md)
- [Hilt + KSP configuration](docs/gradle/ksp-hilt-setup.md)

### App Configuration
- [AndroidManifest overview](docs/manifest/README.md)
- [Permissions & SDK behavior](docs/permissions/README.md)

### Architecture
- [UI & Navigation structure](docs/architecture/README.md)

---

## 🛠️ Why Multiple READMEs?

This project intentionally avoids a single README file because:
- Gradle setup evolves independently
- Manifest permissions change per Android version
- Architecture decisions deserve their own space

This keeps documentation **maintainable and review-friendly**.

---

## 🚧 Status

Active development. Documentation will evolve alongside code changes.

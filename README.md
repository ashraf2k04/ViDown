# Vidown 📥🎬

Vidown is a modern Android application focused on media downloading, built using
Kotlin, Jetpack Compose, Hilt, Room, and native downloader tooling following modern development android architecture.

This repository uses **modular documentation** instead of a single bloated README.

---

## ✨ Features

- Download videos and playlists using the yt-dlp engine
- Modern UI built with Jetpack Compose
- Offline download tracking with Room database
- Background downloading using Android foreground services
- Playlist and video extraction support
- Modular screen-based UI architecture

---

## 🧱 Tech Stack

- Kotlin + Jetpack Compose
- Hilt (DI) + KSP
- Room (offline persistence)
- Coroutines + Flow
- Native downloader binaries (yt-dlp stack)
- Android 13+ permission model

---

## 📂 Project Structure

The UI layer is organized using a **feature-based screen architecture**.

Example structure:

` 
ui
 └ screens
      ├ homescreen
      ├ downloads
      ├ playlist
      └ mainscreen
`

Each screen contains its own:

- UI components
- ViewModel
- UI state models
- utilities

This keeps features isolated and easy to maintain.

---

## 📚 Documentation Index

### Build & Tooling
- [Gradle setup](docs/gradle/Gradle.md)
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

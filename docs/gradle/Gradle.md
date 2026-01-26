# Gradle Configuration

This project uses a *modern, scalable Gradle setup* built around **Kotlin DSL**,  
**Version Catalogs**, and **KSP**.

The goal is to keep build logic **explicit**, **fast**, and **maintainable**, while
avoiding legacy or brittle configurations.

This document explains **what is used**, **why it is used**, and **how it is wired together**.

---

## 🧩 Overview

Gradle configuration in this project is organized into **three layers**:

### 1. Top-level Gradle
- Declares plugin versions
- Shares common configuration
- Does **not** apply plugins directly

### 2. Version Catalog (`libs.versions.toml`)
- Central source of truth for:
  - Dependency versions
  - Plugin versions
- Eliminates hardcoded versions

### 3. Module-level Gradle (`app`)
- Applies required plugins
- Defines Android, Compose, and dependency behavior

This structure keeps modules clean and makes future scaling straightforward.

---

## 🔝 Top-level Gradle Setup

The root `build.gradle.kts` defines plugins using aliases and disables automatic application.

### What is used

- Android Application plugin
- Kotlin Android plugin
- Kotlin Compose plugin
- Hilt (Dependency Injection)
- KSP (Kotlin Symbol Processing)

### Why this approach

- Prevents plugin version duplication
- Keeps plugin upgrades centralized
- Allows selective usage per module
- Makes multi-module expansion trivial

Plugins are declared once and applied only where needed.

---

## 📦 Version Catalog (libs)

All dependencies and plugin versions are managed via a **Version Catalog**.

### Why Version Catalogs are used

- One source of truth for versions
- Cleaner Gradle files
- Easier dependency upgrades
- Fewer merge conflicts
- Better readability

### How it is used

Instead of hardcoding versions:
```kotlin
implementation("androidx.core:core-ktx:1.13.1")
```

### The project uses aliases:
```kotlin
implementation(libs.androidx.core.ktx)
```

## 📦 Bundles

Dependency bundles are used to group related libraries and reduce repetitive
dependency declarations.

| Bundle   | Purpose                 |
|----------|-------------------------|
| Compose  | UI, Material, Tooling   |
| Hilt     | Dependency Injection    |
| Room     | Local database          |
| Testing  | Unit & UI testing       |

---

## 📱 App Module Configuration

The `app` module applies all required plugins and defines Android-specific behavior.

### Plugins Used

| Plugin               | Purpose                                      |
|----------------------|----------------------------------------------|
| Android Application  | App packaging and build system               |
| Kotlin Android       | Kotlin support for Android                   |
| Kotlin Compose       | Jetpack Compose compiler integration         |
| Hilt                 | Dependency Injection framework               |
| KSP                  | Annotation processing replacement for KAPT  |
| Kotlin Serialization | JSON serialization without reflection        |

This combination supports a fully modern **Compose-based Android application**.

---

## ⚙️ Android Configuration

### SDK Configuration

- `compileSdk = 36`
- `targetSdk = 36`
- `minSdk = 28`

### Rationale

- `minSdk 28` simplifies storage and permission handling
- Latest `targetSdk` ensures Android 13+ and future compatibility
- Java 11 is explicitly enabled for toolchain consistency

---

## 🎨 Jetpack Compose Setup

- Compose is explicitly enabled via build features
- Dependencies are managed using a **BOM (Bill of Materials)**

The BOM ensures version consistency across:

- UI
- Material
- Tooling
- Testing

This avoids subtle crashes caused by mismatched Compose versions.

---

## 🧠 Dependency Injection (Hilt)

Hilt is used for dependency injection across the app.

### Configuration Highlights

- Hilt plugin applied at module level
- Compiler wired using **KSP**, not KAPT
- Application class configured as the Hilt entry point

### Why KSP instead of KAPT

- Faster builds
- Better incremental compilation
- Lower memory usage
- Officially recommended for Compose + Room

*KAPT is intentionally avoided in this project.*

---

## 🗄️ Database (Room)

Room is used for local persistence and offline support.

### Configuration

- Room runtime dependencies via Version Catalog
- Compiler configured with KSP
- Schema export prepared (optional)

This keeps database builds fast and predictable.

---

## 🔄 Coroutines & Serialization

### Coroutines

Used for:

- Background downloads
- Async IO
- UI state updates

### Kotlin Serialization

Used instead of Gson/Moshi for:

- Compile-time safety
- No reflection
- Better performance

---

## 📦 Native Libraries & Packaging

Native binaries (such as downloader tools) require special packaging handling.

### Configuration

- Legacy JNI packaging enabled
- Native libraries extracted correctly at runtime

This ensures compatibility with tools like **yt-dlp** and **FFmpeg-based binaries**.

---

## 🧪 Testing Setup

The testing stack includes:

- JUnit for unit tests
- AndroidX testing libraries
- Compose UI testing
- Shared Compose BOM for test consistency

This avoids version mismatch between production and test code.

---

## 🧭 Design Philosophy

This Gradle setup prioritizes:

- Explicit configuration over implicit magic
- Performance over legacy compatibility
- Modern Android tooling
- Clear separation of concerns
- Easy onboarding for contributors

The intention is for Gradle to be *boring, predictable, and invisible* during development.

---

## 🚧 Notes

- KAPT is intentionally not used
- Dependency versions are never hardcoded in module files
- New modules should follow the same plugin + catalog pattern
- Gradle documentation evolves alongside code changes

# AndroidManifest Configuration

This document explains how the AndroidManifest.xml is structured,
what permissions are declared, and why each configuration exists.

The intent is to keep the manifest:
- Explicit
- Minimal
- Android-version aware
- Free of business logic

---

## 📁 File Location

`app/src/main/AndroidManifest.xml`

---

## 🧾 Manifest Responsibilities

The AndroidManifest is responsible for declaring:

- Application-level metadata
- Required permissions
- App entry points
- Platform-specific behaviors

All runtime logic is handled in code, not in XML.

---

## 🔐 Permissions

### Internet Access
- `android.permission.INTERNET`

Used for:
- Network requests
- Media metadata fetching
- Download operations

---

### Storage Permissions

#### Android 12 and below
- `android.permission.READ_EXTERNAL_STORAGE`
- Scoped using maxSdkVersion = 32

Reason:
- Required for legacy storage access
- Prevents requesting deprecated permissions on newer Android versions

---

#### Android 13 and above
- `android.permission.READ_MEDIA_VIDEO`

Reason:
- Android 13 introduced media-scoped permissions
- Ensures Play Store compliance
- Avoids over-privileged access

---

### Foreground Service
- `android.permission.FOREGROUND_SERVICE`

Used for:
- Long-running downloads
- Preventing system termination during active tasks

---

### Notifications
- `android.permission.POST_NOTIFICATIONS`

Used for:
- Download progress updates
- Completion notifications
- Error and failure alerts

---

## 🧠 Application Configuration

Key application-level attributes:

- `android:name = .App`
  - Custom Application class
  - Required for Hilt initialization

- `android:extractNativeLibs = true`
  - Required for native binaries (yt-dlp, ffmpeg)

- `android:allowBackup = true`
  - Enables system-managed backups

- `android:supportsRtl = true`
  - Enables right-to-left layout support

---

## 🚀 Activity Declaration

Main entry activity:
- MainActivity

Key attributes:
- `android:exported = true`
  - Mandatory for launcher activities on Android 12+
  - Explicit declaration prevents install-time crashes

---

## ⚠️ Design Notes

- No unnecessary permissions are declared
- All permissions are scoped by Android version
- The manifest avoids feature logic
- Platform behavior is handled in Kotlin code

---

## 🧭 Philosophy

The manifest should:

- Say what the app needs
- Keep its opinions to itself
- Remain so boring that no one touches it without a reason

If you change the manifest, you should know exactly why — and future you should forgive you for it.

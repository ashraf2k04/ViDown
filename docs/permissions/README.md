## 🔐 Permissions and SDK Behavior

This app follows the modern Android permission model and adapts its behavior
based on the device SDK version.

Permissions are requested only when required and are scoped as narrowly as
possible to avoid unnecessary access.

---

## 🌐 Internet Permission

The app declares internet access to support:

- Network requests
- Media metadata retrieval
- Download operations

This permission is always required and does not vary by Android version.

---

## 💾 Storage Permissions

Storage permissions change significantly across Android versions.
The app explicitly handles this to remain compliant and predictable.

### Android 12 and Below

For devices running Android 12 or earlier:

- Legacy external storage access is used
- Storage permission is limited to older SDK versions
- Deprecated permissions are not requested on newer devices

This prevents unnecessary permission prompts on modern Android versions.

---

### Android 13 and Above

For devices running Android 13 and later:

- Media-scoped permissions are used
- Access is limited to specific media types
- Broad storage access is avoided entirely

This aligns with Android’s privacy-first storage model.

---

## 🔔 Notification Permission

On Android versions that require it:

- Notification permission is requested explicitly
- Used for download progress and completion updates
- Not requested if notifications are not shown

No permission is requested without a user-visible reason.

---

## ⏱️ Foreground Service Behavior

Foreground services are used for:

- Long-running downloads
- Preventing the system from killing active tasks

The foreground service exists only while work is ongoing
and is stopped immediately after completion.

---

## 🧠 SDK-Aware Behavior Summary

- Permissions are requested based on SDK version
- Deprecated permissions are scoped or avoided
- No permission is requested "just in case"
- Behavior changes are handled in code, not guessed by the system

---

## 🧭 Philosophy (with honesty)

Permissions should be:
- Understandable to users
- Predictable for developers
- Easy to audit later

If a permission exists, there should be a clear reason.
If there is no reason, the permission does not belong here.

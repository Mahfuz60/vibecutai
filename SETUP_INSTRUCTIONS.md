# VibeCut AI — Android Studio Setup Guide

## Prerequisites
- Android Studio Ladybug (2024.2) or newer — download from developer.android.com/studio
- JDK 17 (bundled with Android Studio — no separate install needed)
- Android SDK Platform 35 (API 35 / Android 15)
- Android emulator or physical device running Android 7.0+ (API 24+)

## Step 1 — Create a New Android Studio Project
1. Open Android Studio → **New Project**
2. Choose **Empty Activity** template
3. Set:
   - Name: `VibeCut AI`
   - Package: `com.vibecut.ai`
   - Language: **Kotlin**
   - Minimum SDK: **API 24 (Android 7.0)**
   - Build configuration language: **Kotlin DSL (.kts)**

## Step 2 — Replace Build Files
Copy these files from the ZIP into your project root:
- `settings.gradle.kts` → replace the one Android Studio generated
- `gradle/libs.versions.toml` → place in the `gradle/` folder

Copy this into your `app/` folder:
- `app/build.gradle.kts` → replace the generated one

## Step 3 — Copy Source Files
Copy the entire `app/src/` folder from the ZIP into your project, merging with the existing structure.

## Step 4 — Add Firebase
1. Go to [console.firebase.google.com](https://console.firebase.google.com)
2. Create a new project → Add Android app → Package: `com.vibecut.ai`
3. Download `google-services.json`
4. Place it in the `app/` folder (same level as `app/build.gradle.kts`)
5. Enable in Firebase console:
   - Authentication (Google Sign-In + Email/Password)
   - Firestore Database
   - Storage
   - Crashlytics

## Step 5 — Sync and Build
```
File → Sync Project with Gradle Files
```
Wait for Gradle sync to complete, then:
```
Build → Make Project  (Ctrl+F9 / Cmd+F9)
```

## Step 6 — Run
Click the green **Run** button or press **Shift+F10**.
Select your emulator or connected device.

## Common Errors and Fixes

### "Unresolved reference: hilt"
Make sure `ksp` plugin is applied in `app/build.gradle.kts` and `google-services.json` exists.

### "google-services.json file is missing"
Add `google-services.json` to the `app/` folder as described in Step 4.

### FFmpegKit build error
FFmpegKit is a large library (~100 MB). Allow Gradle to fully download it — this can take several minutes on the first sync. Ensure your internet connection is stable.

### "Compose compiler version mismatch"
Make sure `kotlin = "2.1.0"` and `composeBom = "2025.01.01"` in `libs.versions.toml` match exactly — do not upgrade them individually.

### "Missing resource ic_launcher_foreground"
Android Studio generates launcher icons when you create a new project. Keep the generated `res/mipmap-*/` folders — do not delete them.

## Architecture Overview
```
app/
├── data/
│   ├── ai/          ← On-device AI (SceneDetector, BeatSync, etc.)
│   ├── effects/     ← FFmpegKit video effects
│   ├── repository/  ← Repository pattern (Firebase + local)
│   ├── service/     ← Foreground services
│   └── video/       ← Video processing (transitions, filters)
├── di/              ← Hilt dependency injection modules
└── ui/
    ├── navigation/  ← NavGraph + Screen routes
    ├── screens/     ← One folder per screen
    │   ├── auth/    ← Login, onboarding
    │   ├── editor/  ← Main video editor
    │   └── home/    ← Video gallery / home feed
    └── theme/       ← Color.kt, Theme.kt, Type.kt
```

## No API Keys Needed
All AI features (Phase 3) run 100% on-device using:
- **ML Kit** — scene detection, background removal, object tracking
- **Android SpeechRecognizer** — captions/subtitles
- **FFmpegKit** — all video processing, effects, transitions, filters
- **Pure Kotlin math** — beat sync (RMS energy), highlight scoring (frame diff)

No Gemini API key, no OpenAI key, no Replicate key required.
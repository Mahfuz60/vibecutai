# VibeCut AI — Android Source Code

This ZIP contains all Phase 1 source files, Gradle config, Play Store assets, and dependency catalog.

## Structure
- `app/` — Kotlin source files organized by phase
- `gradle/` — Build configuration files
- `playstore/` — Play Store listing assets

## Getting Started
1. Open Android Studio Ladybug (2024.2+)
2. Create a new project and copy these files in
3. Add your `google-services.json` from Firebase Console
4. Run `./gradlew assembleDebug`

## Tech Stack
- Kotlin 2.1.0 + Jetpack Compose (Material 3)
- MVVM + Clean Architecture + Hilt DI
- Firebase Auth, Firestore, Storage
- FFmpegKit, Media3/ExoPlayer, CameraX
- ML Kit + TensorFlow Lite for on-device AI
- Google Gemini 1.5 API for cloud AI
"# vibecutai" 

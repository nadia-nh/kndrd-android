# Kndrd Android

Android companion app for [Kndrd](https://kndrd.com) — the NYC women's social app for making real friends IRL. Browse and join hangouts, chat with attendees, post on the community forum, and create your own plans.

> **Status:** Phase 1 MVP — fully functional with stub/seed data. No backend connected yet.

---

## Features

| Feature | Status |
|---|---|
| Browse feed of NYC hangout plans | ✅ |
| Filter by status (All / Going / Hosting / Saved) | ✅ |
| Filter by interest (Coffee, Fitness, Art, …) | ✅ |
| Tap location → opens Google Maps | ✅ |
| Tap date → opens Calendar with pre-filled event | ✅ |
| Join / Leave a plan | ✅ |
| Group chat per plan | ✅ |
| Community forum | ✅ |
| Create a plan | ✅ |
| Profile screen | ✅ |
| Guest mode (no signup required) | ✅ |
| Onboarding flow (name, phone, interests) | ✅ |
| FCM push notifications | 🔧 Stubbed — enable after adding `google-services.json` |
| Real backend | ⏳ Phase 2 |

---

## Tech Stack

| Layer | Library |
|---|---|
| UI | Jetpack Compose + Material 3 |
| Navigation | Navigation Compose 2.9.0 |
| DI | Hilt 2.59.2 (KSP) |
| Local DB | Room 2.7.1 |
| Preferences | DataStore 1.1.4 |
| Networking | Retrofit 2.11.0 + OkHttp 4.12.0 (stubbed) |
| Images | Coil 2.7.0 |
| Async | Coroutines + StateFlow |
| Splash | AndroidX SplashScreen API |

**Min SDK:** 24 (Android 7.0)  
**Target SDK:** 36  
**Language:** Kotlin 2.1.20

---

## Getting Started

### Prerequisites
- Android Studio Meerkat or later
- JDK 11

### Run the app

```bash
git clone https://github.com/nadia-nh/kndrd-android.git
cd kndrd-android
```

Open in Android Studio and run on a device or emulator. No API keys or backend setup required — the app runs entirely on local stub data.

### Enable Firebase / FCM (optional)

1. Create a project in the [Firebase Console](https://console.firebase.google.com)
2. Add an Android app with package name `com.kndrd.android`
3. Download `google-services.json` and place it in `app/`
4. Uncomment the `google-services` plugin in `build.gradle.kts` (root and `app/`)
5. Uncomment Firebase dependencies in `app/build.gradle.kts`
6. Uncomment the body of `KndrdFcmService.kt`

---

## Project Structure

```
app/src/main/java/com/kndrd/android/
├── KndrdApp.kt                   @HiltAndroidApp entry point
├── MainActivity.kt               Single activity, splash + navigation host
│
├── core/
│   ├── data/
│   │   ├── local/                Room database, DAOs, entities, TypeConverters
│   │   ├── preferences/          DataStore wrapper (session, onboarding state)
│   │   └── remote/               Retrofit API interface (placeholder)
│   ├── di/                       Hilt modules (App, Database, Repository)
│   ├── model/                    Domain models (Plan, User, ChatRoom, …)
│   ├── navigation/               NavGraph, Route sealed class, MainScaffold
│   └── ui/theme/                 KndrdTheme — brand colours + Material 3 scheme
│
├── feature/
│   ├── onboarding/               Welcome → SignUp → Verification → Interests
│   ├── feed/                     Feed screen, plan cards, filter bar, VM
│   ├── plandetail/               Plan detail, join/leave, calendar/maps intents
│   ├── createplan/               Create plan form
│   ├── chats/                    Chat room list
│   ├── chatdetail/               Group chat screen
│   ├── forum/                    Community forum + create post sheet
│   └── profile/                  User profile screen
│
└── notification/
    └── KndrdFcmService.kt        FCM token + notification handling (stubbed)
```

---

## Brand

The Android app mirrors the iOS visual identity:

- **Primary:** Deep plum-charcoal `#2C2028` — dark, editorial
- **Secondary / accent:** Rose-mauve `#C2778A` — selected states, chips
- **Background:** Blush-cream `#F5EAE4` — warm parchment
- **App icon:** Official Kndrd brand icon, adapted for Android adaptive icon format

---

## Seed Data

The app ships with 8 NYC seed plans so the feed looks real out of the box:

| Plan | Neighbourhood | Interest |
|---|---|---|
| Morning coffee at Blue Bottle | Williamsburg, Brooklyn | ☕ Coffee |
| Sunrise yoga in Central Park | Manhattan | 🏃 Fitness |
| Gallery opening at Hauser & Wirth | Chelsea, Manhattan | 🎨 Art |
| Rooftop drinks at LIC Landing | Long Island City, Queens | 🌙 Nightlife |
| Park Slope book club | Park Slope, Brooklyn | 📚 Books |
| Hike at Inwood Hill Park | Inwood, Manhattan | 🌿 Outdoors |
| Ramen crawl in the East Village | East Village, Manhattan | 🍜 Food |
| Live jazz at Smalls | West Village, Manhattan | 🎵 Music |

---

## Roadmap

- **Phase 2:** Connect real backend API, authentication, live push notifications
- **Phase 3:** Saved plans, event discovery map, verified badge flow, app review prompt

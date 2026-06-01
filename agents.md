# Agents

This project was built with [Claude Code](https://claude.com/claude-code) (Anthropic's agentic coding tool) acting as the primary developer across two sessions.

---

## What the agent did

### Session 1 — Full Phase 1 MVP build

Starting from an empty Android Studio project, the agent planned and implemented the entire Phase 1 MVP in a single session:

- Rewrote the Gradle build files (`libs.versions.toml`, root and app `build.gradle.kts`) to add Compose, Hilt, Room, DataStore, Retrofit, Coil, Navigation, and the SplashScreen API
- Renamed the package from `com.example.kndrdandroid` → `com.kndrd.android`
- Built the full navigation graph (onboarding flow + main graph with bottom nav)
- Implemented all 9 feature screens: Welcome, SignUp, Verification, InterestSelection, Feed, PlanDetail, CreatePlan, Chats, ChatDetail, Forum, Profile
- Created stub repositories with in-memory `MutableStateFlow` and 8 NYC seed plans
- Set up Room database with TypeConverters, DataStore preferences, Hilt DI modules
- Wrote a gated `KndrdFcmService` (commented out pending `google-services.json`)
- Added the SplashScreen API with a branded starting theme
- Debugged and fixed two navigation crashes introduced during the session
- Added guest mode ("Browse without an account") to bypass signup during development
- Tracked all work in `implementation.md` and committed in small focused git commits

### Session 2 — Polish, brand alignment, and pitch prep

- Fixed a crash on plan tap (root `NavController` being used to navigate to routes only registered in the inner `NavController`)
- Made the bottom navigation bar always visible so Home/Feed is reachable from any screen
- Added calendar intent (tap date → pre-filled Calendar event) and maps intent (tap location → Google Maps)
- Added leave plan functionality end-to-end (repo → VM → UI)
- Stayed on PlanDetail after joining instead of auto-navigating to chat
- Inspected the live iOS app on the App Store using a browser automation tool, extracted the visual identity, and realigned the Android theme: deep plum-charcoal primary, rose-mauve accent, blush-cream background
- Added iOS-style status filter tabs (All / Going / Hosting / Saved) to the feed
- Added hero images to plan cards and the detail screen using curated Unsplash photos
- Downloaded the official Kndrd iOS app icon via the iTunes lookup API and generated all Android adaptive icon assets (5 densities of legacy launcher icons + adaptive foreground PNGs with safe-zone padding)
- Created two pull requests with structured descriptions

---

## How it was prompted

The initial prompt was a full product spec describing the app, tech stack, and constraints. Subsequent prompts were short and conversational — bug reports ("clicking on any of them closes the app"), feature requests ("add a way to leave an event"), and design direction ("make it similar to the original with a bit of a pink twist").

The agent operated with write access to the repository, running git commits after each logical unit of work.

---

## Humans in the loop

- Gradle build file changes (plugin versions, dependency additions) were made manually by the developer after the agent's initial setup, then handed back
- All navigation crash root causes were reported by the developer after testing on a physical device; the agent diagnosed and fixed them
- Visual direction for the theme ("pink twist") and UX decisions ("stay on the same screen after joining") came from the developer

---

## Tools used

| Tool | Purpose |
|---|---|
| File read/write/edit | All source code and resource files |
| Bash | Git operations, Python script execution |
| Python + Pillow | Icon download and resizing |
| Browser automation (Chrome MCP) | Live inspection of iOS App Store screenshots |
| iTunes Lookup API | Fetching the official app icon URL |
| GitHub CLI (`gh`) | Creating pull requests |
| WebFetch / WebSearch | Research on iOS app visual identity |

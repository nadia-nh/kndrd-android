# Kndrd Android — Implementation Tracker

## Sprint 1 — Foundation
- [x] Update `gradle/libs.versions.toml` (Compose, Hilt, Room, Navigation, DataStore, Retrofit, Coil, FCM, Coroutines)
- [x] Update root `build.gradle.kts` (add kotlin, hilt, ksp, google-services plugins)
- [x] Replace `app/build.gradle.kts` (apply all plugins, Compose buildFeatures, new dependencies)
- [x] Rename package `com.example.kndrdandroid` → `com.kndrd.android` everywhere
- [x] Create `KndrdApp.kt` (`@HiltAndroidApp`)
- [x] Update `AndroidManifest.xml` (KndrdApp, permissions, FCM service, adjustResize)
- [x] Replace `MainActivity.kt` (Compose shell with `installSplashScreen`)
- [x] Create `core/ui/theme/KndrdTheme.kt` (brand colors, Material 3)

## Sprint 2 — Data Layer
- [x] Create domain models: `Interest`, `Plan`, `User`, `ChatRoom`, `ChatMessage`, `ForumPost`
- [x] Create Room entities: `PlanEntity`, `UserEntity`, `MessageEntity`, `ForumPostEntity`
- [x] Create `KndrdTypeConverters.kt` (`List<String>` ↔ JSON)
- [x] Create DAOs: `PlanDao`, `UserDao`, `MessageDao`, `ForumPostDao`
- [x] Create `KndrdDatabase.kt`
- [x] Create `UserPreferences.kt` (DataStore wrapper)
- [x] Create `KndrdApiService.kt` (empty Retrofit interface)
- [x] Create `DatabaseModule.kt`, `AppModule.kt`
- [x] Create repository interfaces: `FeedRepository`, `ChatRepository`, `ForumRepository`, `UserRepository`
- [x] Create stub implementations with NYC seed data
- [x] Create `RepositoryModule.kt` (`@Binds` stubs to interfaces)

## Sprint 3 — Navigation Skeleton
- [x] Create `Route.kt` (sealed class with all routes)
- [x] Create `KndrdNavGraph.kt` (onboarding_graph + main_graph)
- [x] Create `MainScaffold.kt` with `BottomNavigationBar` (Feed, +, Chats, Forum, Profile)
- [x] Create `MainViewModel.kt` (reads `isOnboardingComplete` from DataStore)

## Sprint 4 — Onboarding
- [x] Create `OnboardingViewModel.kt`
- [x] Create `WelcomeScreen.kt`
- [x] Create `SignUpScreen.kt`
- [x] Create `VerificationScreen.kt`
- [x] Create `InterestSelectionScreen.kt`
- [x] Wire onboarding-done check to skip if already complete

## Sprint 5 — Feed + Plan Detail
- [x] Create `FeedViewModel.kt`
- [x] Create `PlanCard.kt` (reusable ElevatedCard)
- [x] Create `FeedFilterBar.kt` (horizontal LazyRow of FilterChip)
- [x] Create `FeedScreen.kt` (PullToRefreshBox, LazyColumn, empty state)
- [x] Create `PlanDetailViewModel.kt`
- [x] Create `PlanDetailScreen.kt` (metadata, join button → navigate to chat)

## Sprint 6 — Chat
- [x] Create `ChatsViewModel.kt`
- [x] Create `ChatsScreen.kt` + `ChatRoomRow.kt`
- [x] Create `ChatDetailViewModel.kt`
- [x] Create `MessageBubble.kt` (own=right/primary, other=left/surface)
- [x] Create `ChatDetailScreen.kt` (auto-scroll, keyboard-aware input)
- [x] Wire: joining a plan navigates to ChatDetail

## Sprint 7 — Create Plan + Forum
- [x] Create `CreatePlanViewModel.kt`
- [x] Create `CreatePlanScreen.kt` (date/time pickers, interest dropdown, attendee slider)
- [x] Create `ForumViewModel.kt`
- [x] Create `ForumPostCard.kt` (inline in ForumScreen)
- [x] Create `CreatePostBottomSheet.kt` (ModalBottomSheet, inline in ForumScreen)
- [x] Create `ForumScreen.kt`

## Sprint 8 — Profile + FCM
- [x] Create `ProfileViewModel.kt`
- [x] Create `ProfileScreen.kt` (avatar, verified badge, interests FlowRow, settings items)
- [x] Create `KndrdFcmService.kt` (ready, wrapped in comment until Firebase configured)
- [x] Add `installSplashScreen()` to MainActivity
- [x] Set up splash screen theme in `themes.xml` + splash background color

## Sprint 9 — Polish ✓
- [x] Empty states for Feed, Chats, Forum
- [x] Loading indicators (CircularProgressIndicator) in Feed, PlanDetail, Profile
- [x] Error field in CreatePlan
- [x] Splash screen setup
- [x] Edge-to-edge via `enableEdgeToEdge()` + `imePadding()` in ChatDetail

---

## Pending — Next Steps

### FCM (when Firebase is ready)
1. Create Firebase project at console.firebase.google.com
2. Add Android app with package `com.kndrd.android`
3. Download `google-services.json` → place in `app/`
4. Uncomment `google-services` plugin in both `build.gradle.kts` files
5. Uncomment Firebase dependencies in `app/build.gradle.kts`
6. Uncomment `KndrdFcmService` body and manifest service entry

### Real Backend (when API is documented)
- Replace stub repository implementations with Retrofit-backed ones
- Add auth token interceptor to OkHttpClient
- Wire Room cache as offline-first layer

### Phase 2 (future)
- Material You dynamic theming
- Home screen widget
- Calendar integration
- Deep links (Android App Links)
- Adaptive layouts for tablets/foldables
- Predictive back gesture

---

## Commit Log

| Commit | Description |
|---|---|
| `feat: add Compose, Hilt, Room, and Navigation gradle dependencies` | Sprint 1: Gradle + package rename |
| `feat: add KndrdApp, Compose entry point, and brand theme` | Sprint 1: App foundation |
| `feat: add domain models, Room database, repositories, and Hilt modules` | Sprint 2: Full data layer |
| `feat: add navigation skeleton, MainScaffold, and onboarding screens` | Sprint 3 + 4 |
| `feat: add Feed and Plan Detail screens` | Sprint 5 |
| `feat: add Chats and Chat Detail screens` | Sprint 6 |
| `feat: add Create Plan and Forum screens` | Sprint 7 |
| `feat: add Profile screen and FCM service stub` | Sprint 8 |
| `feat: splash screen, polish pass, and implementation tracker update` | Sprint 9 |

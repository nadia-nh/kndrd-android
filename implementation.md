# Kndrd Android — Implementation Tracker

## Sprint 1 — Foundation
- [ ] Update `gradle/libs.versions.toml` (Compose, Hilt, Room, Navigation, DataStore, Retrofit, Coil, FCM, Coroutines)
- [ ] Update root `build.gradle.kts` (add kotlin, hilt, ksp, google-services plugins)
- [ ] Replace `app/build.gradle.kts` (apply all plugins, Compose buildFeatures, new dependencies)
- [ ] Rename package `com.example.kndrdandroid` → `com.kndrd.android` everywhere
- [ ] Create `KndrdApp.kt` (`@HiltAndroidApp`)
- [ ] Update `AndroidManifest.xml` (KndrdApp, permissions, FCM service, adjustResize)
- [ ] Replace `MainActivity.kt` (Compose shell with `installSplashScreen`)
- [ ] Create `core/ui/theme/KndrdTheme.kt` (brand colors, Material 3)
- [ ] Verify app builds and empty Compose surface launches

## Sprint 2 — Data Layer
- [ ] Create domain models: `Interest`, `Plan`, `User`, `ChatRoom`, `ChatMessage`, `ForumPost`
- [ ] Create Room entities: `PlanEntity`, `UserEntity`, `MessageEntity`, `ForumPostEntity`
- [ ] Create `KndrdTypeConverters.kt` (`List<String>` ↔ JSON)
- [ ] Create DAOs: `PlanDao`, `UserDao`, `MessageDao`, `ForumPostDao`
- [ ] Create `KndrdDatabase.kt`
- [ ] Create `UserPreferences.kt` (DataStore wrapper)
- [ ] Create `KndrdApiService.kt` (empty Retrofit interface)
- [ ] Create `DatabaseModule.kt`, `AppModule.kt`
- [ ] Create repository interfaces: `FeedRepository`, `ChatRepository`, `ForumRepository`, `UserRepository`
- [ ] Create stub implementations with NYC seed data
- [ ] Create `RepositoryModule.kt` (`@Binds` stubs to interfaces)

## Sprint 3 — Navigation Skeleton
- [ ] Create `Route.kt` (sealed class with all routes)
- [ ] Create `KndrdNavGraph.kt` (onboarding_graph + main_graph, placeholder composables)
- [ ] Create `MainScaffold.kt` with `BottomNavigationBar` (Feed, +, Chats, Forum, Profile)
- [ ] Verify all navigation paths work end-to-end

## Sprint 4 — Onboarding
- [ ] Create `OnboardingViewModel.kt`
- [ ] Create `WelcomeScreen.kt`
- [ ] Create `SignUpScreen.kt`
- [ ] Create `VerificationScreen.kt` (6-digit OTP, auto-advance, 30s resend countdown)
- [ ] Create `InterestSelectionScreen.kt` (LazyVerticalGrid FilterChip, min 3 required)
- [ ] Wire onboarding-done check to skip if already complete

## Sprint 5 — Feed + Plan Detail
- [ ] Create `FeedViewModel.kt`
- [ ] Create `PlanCard.kt` (reusable ElevatedCard)
- [ ] Create `FeedFilterBar.kt` (horizontal LazyRow of FilterChip)
- [ ] Create `FeedScreen.kt` (PullToRefreshBox, LazyColumn)
- [ ] Create `PlanDetailViewModel.kt`
- [ ] Create `PlanDetailScreen.kt` (hero image, join button → navigate to chat)

## Sprint 6 — Chat
- [ ] Create `ChatsViewModel.kt`
- [ ] Create `ChatsScreen.kt` + `ChatRoomRow.kt`
- [ ] Create `ChatDetailViewModel.kt`
- [ ] Create `MessageBubble.kt` (own=right/primary, other=left/surface)
- [ ] Create `ChatInputBar.kt`
- [ ] Create `ChatDetailScreen.kt` (reverseLayout=true, auto-scroll)
- [ ] Wire: joining a plan navigates to ChatDetail

## Sprint 7 — Create Plan + Forum
- [ ] Create `CreatePlanViewModel.kt`
- [ ] Create `CreatePlanScreen.kt` (date/time pickers, interest dropdown, attendee slider)
- [ ] Create `ForumViewModel.kt`
- [ ] Create `ForumPostCard.kt`
- [ ] Create `CreatePostBottomSheet.kt` (ModalBottomSheet)
- [ ] Create `ForumScreen.kt`

## Sprint 8 — Profile + FCM
- [ ] Create `ProfileViewModel.kt`
- [ ] Create `ProfileScreen.kt` (avatar, verified badge, interests FlowRow, joined plans)
- [ ] Create `KndrdFcmService.kt` (onNewToken, onMessageReceived, notification channel)
- [ ] Add runtime `POST_NOTIFICATIONS` permission request after onboarding
- [ ] Set up splash screen in `themes.xml`

## Sprint 9 — Polish
- [ ] Empty states for all screens
- [ ] Loading skeletons / shimmer placeholders
- [ ] Error snackbars
- [ ] Chats tab unread badge
- [ ] Edge-to-edge insets handling in all screens
- [ ] Final walkthrough of all flows

---

## Commit Strategy

Each sprint = 1–3 focused commits. Prefix: `feat:`, `refactor:`, `fix:`.

Examples:
- `feat: add Compose, Hilt, Room, and Navigation gradle dependencies`
- `feat: add domain models, Room entities, and DAOs`
- `feat: add navigation skeleton with bottom nav`
- `feat: add onboarding flow`
- `feat: add Feed and Plan Detail screens`
- `feat: add Chat screens`
- `feat: add Create Plan and Forum screens`
- `feat: add Profile screen and FCM service`
- `feat: polish empty states, skeletons, and error handling`

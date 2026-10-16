# HU-003 base-app — Tareas

- [x] T01 Limpiar el prototipo anterior (pantallas y navegación de PatitasID).
- [x] T02 Gradle: `minSdk 26`, `compileSdk 37`, Koin, Retrofit, OkHttp, kotlinx-serialization, JUnit5; `API_BASE_URL` y `USE_LOCAL_DATA` vía `BuildConfig`.
- [x] T03 `common/SealedResult` + `DomainError`.
- [x] T04 `domain/`: `HealthRepository`, `CheckServerHealthUseCase`.
- [x] T05 `data/`: `HealthApiService`, `safeApiCall` (manejo común de errores y sin conexión), repositorio remoto y local.
- [x] T06 `di/AppModules.kt`: core, data (selección local/remoto), feature.
- [x] T07 `ui/theme` con colores del Figma (#1F4E79, #F4A261, #F0F3F7) y tipografía.
- [x] T08 `ui/components`: `PetPrimaryButton`, `PetOutlinedButton`, `PetTextField`, `PetCard` (≥48 dp).
- [x] T09 Navegación tipada: `WelcomeRoute` → `HomeRoute`.
- [x] T10 Manifiesto: `INTERNET`, `allowBackup=false`, `usesCleartextTraffic=false`, `PetMateApp`.
- [x] T11 Pruebas unitarias: `SafeApiCallTest`, `HomeViewModelTest`.
- [x] T12 `./gradlew assembleDebug testDebugUnitTest` en verde.
- [ ] T13 Prueba manual en emulador Android 8.0 (API 26): navegación, modo avión, `-PpetmateDataSource=local`.

# HU-008 bienvenida — Tareas

- [x] T01 `domain/model`: `Session` y `StartDestination`.
- [x] T02 `domain`: `SessionRepository` y `GetStartDestinationUseCase` (sin sesión → bienvenida; con sesión → mascotas o panel).
- [x] T03 `data/local`: `SessionStorage` (interfaz) y `EncryptedSessionStorage` con `EncryptedSharedPreferences`; `SessionLocalRepository` fuera del hilo principal.
- [x] T04 `app/build.gradle.kts` y catálogo de versiones: dependencia `androidx.security:security-crypto`.
- [x] T05 `di/AppModules.kt`: `SessionStorage`, `SessionRepository`, `GetStartDestinationUseCase` y `StartupViewModel`.
- [x] T06 `ui/feature/startup`: `StartupViewModel` y `StartupScreen` (carga mientras se lee la sesión).
- [x] T07 `ui/feature/welcome`: `WelcomeScreen` con «Iniciar sesión», «Crear cuenta» y «Probar demo».
- [x] T08 Navegación: `StartupRoute` como destino inicial, `LoginRoute`, `SignUpRoute`, `DemoRoute`, `PetsRoute` y `BusinessPanelRoute`; la bienvenida se saca de la pila al entrar con sesión.
- [x] T09 Pantallas marcador `PetsScreen`, `BusinessPanelScreen` y `DemoScreen`.
- [x] T10 Pruebas unitarias: `GetStartDestinationUseCaseTest`, `StartupViewModelTest`, `SessionLocalRepositoryTest`.
- [x] T11 `./gradlew assembleDebug testDebugUnitTest` en verde (45 pruebas, 0 fallos).
- [ ] T12 Prueba manual en emulador: abrir sin sesión, guardar sesión y reabrir, cerrar y reabrir la app (RF-57).
- [ ] T13 Prueba de UI `WelcomeScreenTest` (androidTest) con los tres botones.
- [ ] T14 Al existir HU-010: `LoginRoute` deja de ser marcador y se quita de la bienvenida el acceso a «¿Olvidaste tu contraseña?».
- [ ] T15 Al existir HU-013: «Probar demo» abre el modo demo real en lugar del marcador.
- [ ] T16 Cambiar `EncryptedSharedPreferences` (ya marcado como obsoleto) por la alternativa que se decida; la interfaz `SessionStorage` deja el cambio aislado.

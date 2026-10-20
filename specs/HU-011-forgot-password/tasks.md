# HU-011 forgot-password — Tareas

- [x] T01 `common/DomainError.ExpiredLink` y `safeApiCallNoBody` (2xx sin cuerpo = éxito).
- [x] T02 `domain/`: `PasswordRepository`, casos de uso de pedir enlace, validar enlace y restablecer; `CredentialRules`.
- [x] T03 `data/`: `PasswordApiService`, `PasswordRemoteRepository` (404/410 → enlace expirado), `PasswordLocalRepository` (token `expirado` para la demo).
- [x] T04 `di/AppModules.kt`: repositorio local/remoto, casos de uso y ViewModels (token por `parametersOf`).
- [x] T05 `ui/components`: `PetPasswordField`, `PetTopBar`, `PetStatusMessage`; `PetTextField` acepta teclado y transformación.
- [x] T06 `ui/feature/password`: `ForgotPasswordScreen` + ViewModel, `ResetPasswordScreen` + ViewModel.
- [x] T07 Navegación: `ForgotPasswordRoute`, `ResetPasswordRoute(token)` con deep link `petmate://restablecer-contrasena/{token}`; `intent-filter` en el manifiesto.
- [x] T08 Acceso temporal desde Bienvenida («¿Olvidaste tu contraseña?»).
- [x] T09 Pruebas unitarias: `ForgotPasswordViewModelTest`, `ResetPasswordViewModelTest`, `PasswordRemoteRepositoryTest`, `CredentialRulesTest`.
- [x] T10 `./gradlew assembleDebug testDebugUnitTest` en verde.
- [ ] T11 Confirmar con Miguel rutas, cuerpos y códigos del API, y que el correo use `petmate://restablecer-contrasena/{token}`.
- [ ] T12 Prueba manual en emulador: enviar enlace, abrir enlace válido y expirado con `adb`, modo avión.
- [ ] T13 Al existir Login: mover el acceso y cambiar `onGoToLogin` a `LoginRoute`.

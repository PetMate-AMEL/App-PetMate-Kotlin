# HU-009 crear-cuenta — Tareas

- [x] T01 `common/DomainError.EmailAlreadyUsed` y `UserMessages.EMAIL_ALREADY_USED`.
- [x] T02 `domain/validation`: `CredentialRules.isValidName`.
- [x] T03 `domain`: `AccountRepository` y `SignUpUseCase` (guarda la sesión al crear la cuenta).
- [x] T04 `data`: `AccountApiService`, `AccountRemoteRepository` (409 → correo ya registrado) y `AccountLocalRepository` (`registrado@correo.com` para la demo).
- [x] T05 `di/AppModules.kt`: repositorio local/remoto, `SignUpUseCase` y `SignUpViewModel`.
- [x] T06 `ui/feature/signup`: `SignUpViewModel` con validación por campo antes de llamar al API.
- [x] T07 `ui/feature/signup`: `SignUpScreen` con nombre, correo, contraseña y confirmación, con mostrar u ocultar.
- [x] T08 Navegación: `SignUpRoute` desde la bienvenida; al crear la cuenta va a `PetsRoute` sacando la bienvenida de la pila.
- [x] T09 Las contraseñas se borran del estado al crearse la cuenta.
- [x] T10 Pruebas unitarias: `SignUpViewModelTest` (10 casos), `AccountRemoteRepositoryTest`, `CredentialRulesTest`.
- [x] T11 `./gradlew assembleDebug testDebugUnitTest` en verde (45 pruebas, 0 fallos).
- [ ] T12 Confirmar con Miguel la ruta `api/auth/registro`, los nombres de los campos y el 409.
- [ ] T13 Prueba manual con datos demo: datos válidos, `registrado@correo.com`, contraseña corta y modo avión.
- [ ] T14 Prueba de UI `SignUpScreenTest` (androidTest) con los errores por campo.
- [ ] T15 Agregar el código de verificación de 6 números después del registro (RF-42, historia aparte).

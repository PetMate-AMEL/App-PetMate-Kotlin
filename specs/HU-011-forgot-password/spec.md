# HU-011 forgot-password — Pantalla «Olvidé mi contraseña» y restablecimiento

> Estructura SDD (spec → plan → tasks). Sprint 1 (20/10/2026). Responsable: Eduardo. Requerimiento: RF-04.

## 1. Contexto y objetivo

Como usuario que olvidó su contraseña, quiero pedir un enlace a mi correo y crear una contraseña nueva para volver a entrar sin perder mis datos.
Alcance: pantalla para pedir el enlace, apertura del enlace del correo dentro de la app (deep link), pantalla para crear la contraseña nueva y manejo del enlace expirado.
Fuera de alcance: envío del correo y generación/expiración del token (API), pantalla de inicio de sesión (otra HU).

## 2. Requisitos funcionales

- «Olvidé mi contraseña»: campo de correo y botón «Enviar enlace». Siempre muestra «Si el correo existe, te enviamos un enlace» aunque el correo no esté registrado (un 404 del API se trata igual que un éxito), para no revelar qué correos existen.
- Enlace del correo: `petmate://restablecer-contrasena/{token}` abre la pantalla «Nueva contraseña».
- Al abrirla, la app valida el token. Si es válido, pide la contraseña dos veces; si expiró o ya se usó, muestra «Este enlace ya no es válido» y el botón «Pedir otro enlace».
- Al guardar: «Tu contraseña se guardó» y botón «Iniciar sesión».
- Contraseña válida: al menos 8 caracteres, con letras y números (`domain/validation/CredentialRules.kt`).

## 3. Requisitos no funcionales

- RNF-14: textos en español de México.
- RNF-15: campos y botones de al menos 48 dp; los campos de contraseña tienen botón para mostrar/ocultar con `contentDescription`.
- RNF-18: sin internet se muestra un aviso claro y se puede reintentar.
- RNF-22: `PasswordLocalRepository` (demo) y `PasswordRemoteRepository` (API) se intercambian solo en `di/AppModules.kt`.
- Seguridad: el token viaja en el cuerpo de la petición, no en la URL del API; la contraseña se borra del estado al guardarse.

## 4. Arquitectura y contratos

| Capa | Archivos |
|---|---|
| common | `DomainError.ExpiredLink` (nuevo) |
| domain | `PasswordRepository`, `RequestPasswordResetUseCase`, `ValidateResetLinkUseCase`, `ResetPasswordUseCase`, `CredentialRules` |
| data | `PasswordApiService`, `PasswordRemoteRepository`, `PasswordLocalRepository`, `safeApiCallNoBody` |
| di | registro en `dataModule` y `featureModule` |
| ui | `feature/password/` (`ForgotPassword*`, `ResetPassword*`), `components/` (`PetPasswordField`, `PetTopBar`, `PetStatusMessage`), `common/UserMessages.kt` |
| navegación | `ForgotPasswordRoute`, `ResetPasswordRoute(token)` con deep link; `intent-filter` en `AndroidManifest.xml` |

Contrato del API supuesto (**pendiente de confirmar con Miguel**):

| Método | Ruta | Cuerpo | Respuestas |
|---|---|---|---|
| POST | `api/auth/olvide-contrasena` | `{ "correo": "..." }` | 2xx enviado (404 se trata igual) |
| POST | `api/auth/restablecer-contrasena/validar` | `{ "token": "..." }` | 2xx válido · 404/410 expirado o usado |
| POST | `api/auth/restablecer-contrasena` | `{ "token": "...", "nuevaContrasena": "..." }` | 2xx guardado · 404/410 expirado o usado |

El correo que envía el API debe apuntar a `petmate://restablecer-contrasena/{token}`.

## 5. Criterios de aceptación

| # | Dado que | Cuando | Entonces | Evidencia |
|---|---|---|---|---|
| 1 | el usuario escribe su correo | toca «Enviar enlace» | ve «Si el correo existe, te enviamos un enlace» | `ForgotPasswordViewModelTest` |
| 2 | abre el enlace del correo | escribe una contraseña válida dos veces | se guarda y puede volver a iniciar sesión | `ResetPasswordViewModelTest`, prueba manual con `adb` |
| 3 | el enlace expiró | lo abre | ve que el enlace ya no es válido y puede pedir otro | `ResetPasswordViewModelTest`, `PasswordRemoteRepositoryTest` |

## 6. Plan de pruebas

Unit (JUnit5 + coroutines-test): `ForgotPasswordViewModelTest`, `ResetPasswordViewModelTest`, `PasswordRemoteRepositoryTest`, `CredentialRulesTest`, `SafeApiCallTest`.

Manual con datos demo (`-PpetmateDataSource=local`):

```bash
adb shell am start -W -a android.intent.action.VIEW -d "petmate://restablecer-contrasena/abc123" com.example.patitasid
adb shell am start -W -a android.intent.action.VIEW -d "petmate://restablecer-contrasena/expirado" com.example.patitasid
```

## 7. Riesgos, límites y deuda

- Todavía no existe la pantalla de inicio de sesión: «Iniciar sesión» regresa a Bienvenida y el acceso a «¿Olvidaste tu contraseña?» está en Bienvenida. Al terminar la HU de login hay que mover ese botón a Login y cambiar `onGoToLogin` en `NavGraph.kt` a `LoginRoute`.
- Varios clientes de correo (por ejemplo Gmail web) no abren enlaces con esquema propio (`petmate://`). Lo ideal es usar App Links (`https://<dominio>/restablecer-contrasena/{token}` + `assetlinks.json`) cuando haya dominio.
- La contraseña nueva no se compara con la anterior; eso lo decide el API.
- Se agregó `safeApiCallNoBody` y `HealthRemoteRepository` lo usa: antes un 204 de `/api/salud` se mostraba como servidor caído.

## Aprobación

| Estado | Revisor | Fecha | Evidencia |
|---|---|---|---|
| Borrador | — | — | — |

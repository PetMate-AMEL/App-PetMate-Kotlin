# HU-009 crear-cuenta — Pantalla de crear cuenta con validaciones

> Estructura SDD (spec → plan → tasks). Sprint 1 (cierre 20/10/2026). Responsable: Adrián. Requerimientos: RF-02.

## 1. Contexto y objetivo

Como visitante, quiero crear mi cuenta con nombre, correo y contraseña, para empezar a guardar los datos de mis mascotas.

Alcance: formulario con nombre, correo, contraseña y confirmación, con botón para mostrar u ocultar la contraseña; validación en pantalla antes de llamar al API; al registrarse se guarda el token cifrado y se abre «Mis mascotas».
Fuera de alcance: el código de verificación de 6 números (HU-006 en el API, RF-42 en la app), el inicio de sesión (HU-010) y la pantalla «Mis mascotas» (HU-018).

## 2. Requisitos funcionales

- Formulario con cuatro campos: nombre, correo electrónico, contraseña y confirmar contraseña.
- Los campos de contraseña tienen botón para mostrar u ocultar (`PetPasswordField`).
- Validación en pantalla **antes** de llamar al API: nombre no vacío, correo con formato válido, contraseña de al menos 8 caracteres con letras y números, y confirmación igual a la contraseña. Si algo falla, el campo se marca con el motivo y no se llama al API.
- Con datos válidos: se llama al API, se guarda la sesión cifrada y se abre «Mis mascotas».
- Si el correo ya está registrado (409 del API), se muestra «Este correo ya está registrado» **junto al campo de correo**, no como error general.
- Sin internet o con el servidor caído se muestra el aviso general correspondiente y se puede reintentar.
- Se llega a esta pantalla desde el botón «Crear cuenta» de la bienvenida (HU-008).

## 3. Requisitos no funcionales

- RNF-01: contraseña de al menos 8 caracteres; la sesión se guarda cifrada.
- RNF-06: el token se guarda en almacenamiento cifrado de Android.
- RNF-14 y RNF-16: textos y mensajes de error sencillos, en español de México, junto al campo que los causa.
- RNF-15: campos y botones de al menos 48 dp; los botones de mostrar u ocultar tienen `contentDescription`.
- RNF-18: sin internet se muestra un aviso claro y se puede reintentar.
- RNF-22: `AccountLocalRepository` (demo) y `AccountRemoteRepository` (API) se intercambian solo en `di/AppModules.kt`.
- Seguridad: las contraseñas se borran del estado en cuanto se crea la cuenta; nunca se escriben en logs.

## 4. Arquitectura y contratos

| Capa | Archivos |
|---|---|
| common | `DomainError.EmailAlreadyUsed` (nuevo) |
| domain | `AccountRepository`, `SignUpUseCase`, `CredentialRules.isValidName` (nuevo) |
| data | `AccountApiService`, `AccountRemoteRepository` (409 → correo ya usado), `AccountLocalRepository` |
| di | registro en `dataModule` y `featureModule` |
| ui | `feature/signup/` (`SignUpScreen`, `SignUpViewModel`) |
| navegación | `SignUpRoute`; al crear la cuenta se va a `PetsRoute` limpiando la pila |

Contrato del API supuesto (**pendiente de confirmar con Miguel**, HU-006):

| Método | Ruta | Cuerpo | Respuestas |
|---|---|---|---|
| POST | `api/auth/registro` | `{ "nombre": "...", "correo": "...", "contrasena": "..." }` | 2xx `{ "accessToken", "refreshToken", "tieneNegocio" }` · 409 correo ya registrado |

`SignUpUseCase` valida de nuevo antes de llamar al API (la pantalla no es la única defensa) y, con el registro correcto, guarda la sesión mediante `SessionRepository` (HU-008).

## 5. Criterios de aceptación

| # | Dado que | Cuando | Entonces | Evidencia |
|---|---|---|---|---|
| 1 | los datos son válidos | toca «Crear cuenta» | se crea la cuenta, se guarda la sesión cifrada y entra a «Mis mascotas» | `SignUpViewModelTest` |
| 2 | el correo ya está registrado | toca «Crear cuenta» | ve «Este correo ya está registrado» junto al campo | `SignUpViewModelTest`, `AccountRemoteRepositoryTest` |
| 3 | la contraseña tiene menos de 8 caracteres | intenta continuar | el campo se marca con el motivo y no se llama al API | `SignUpViewModelTest`, `CredentialRulesTest` |

## 6. Plan de pruebas

Unit (JUnit5 + coroutines-test): `SignUpViewModelTest`, `AccountRemoteRepositoryTest`, `SignUpUseCaseTest`, `CredentialRulesTest`.
UI (Compose, androidTest): `SignUpScreenTest` (errores por campo y botón habilitado).

Manual con datos demo (`-PpetmateDataSource=local`): crear cuenta con datos válidos, con el correo `registrado@correo.com` para ver el 409, con contraseña corta y en modo avión.

## 7. Riesgos, límites y deuda

- Falta el código de verificación de 6 números por correo o SMS (RF-42): la maqueta aprobada lo pide después de crear la cuenta. Es otra historia; aquí el registro entra directo.
- El contrato del API es supuesto: hay que confirmarlo con Miguel, en especial el 409 y los nombres de los campos de la respuesta.
- «Mis mascotas» todavía es una pantalla marcador (HU-018).
- La fuerza de la contraseña no se muestra con una barra; solo el mensaje de error. Puede agregarse si la maqueta lo pide.

## Aprobación

| Estado | Revisor | Fecha | Evidencia |
|---|---|---|---|
| Borrador | — | — | — |

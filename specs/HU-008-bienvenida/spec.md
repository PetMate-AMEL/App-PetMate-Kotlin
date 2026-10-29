# HU-008 bienvenida — Pantalla de bienvenida con Iniciar sesión, Crear cuenta y Probar demo

> Estructura SDD (spec → plan → tasks). Sprint 1 (cierre 19/10/2026). Responsable: Adrián. Requerimientos: RF-01, RF-57.

## 1. Contexto y objetivo

Como visitante, quiero ver al abrir la app las opciones Iniciar sesión, Crear cuenta y Probar demo, para elegir cómo entrar a PetMate.

Alcance: pantalla de bienvenida con logo, frase de PetMate y los tres botones; decisión de arranque, es decir, si ya hay una sesión guardada la app se salta la bienvenida y entra directo; entrada al modo demo.
Fuera de alcance: la pantalla de inicio de sesión (HU-010), el formulario de crear cuenta (HU-009, en este mismo entregable) y el contenido del modo demo (HU-013).

## 2. Requisitos funcionales

- Sin sesión, al abrir la app aparece la bienvenida con los tres botones: «Iniciar sesión», «Crear cuenta» y «Probar demo».
- Con una sesión válida guardada, la app no muestra la bienvenida y entra directo al destino de la sesión: «Mis mascotas» para el dueño o el panel para el afiliado.
- Mientras se lee la sesión guardada se muestra una pantalla de carga, para no alcanzar a ver la bienvenida y que luego se la lleve la navegación.
- «Probar demo» entra en modo demo (HU-013); por ahora abre el destino de demo disponible.
- Se conserva el acceso a «¿Olvidaste tu contraseña?» que HU-011 dejó temporalmente en la bienvenida, hasta que exista la pantalla de inicio de sesión (HU-010).

## 3. Requisitos no funcionales

- RNF-01 y RNF-06: la sesión se guarda en almacenamiento cifrado de Android (`EncryptedSharedPreferences`), nunca en texto plano.
- RNF-14: textos en español de México.
- RNF-15: botones de al menos 48 dp.
- RNF-22: `SessionLocalDataSource` se puede sustituir desde `di/AppModules.kt`.
- RNF-30: la sesión sigue viva entre aperturas de la app (RF-57); solo se borra al cerrar sesión (HU-010).

## 4. Arquitectura y contratos

| Capa | Archivos |
|---|---|
| domain | `model/Session.kt`, `model/StartDestination.kt`, `repository/SessionRepository`, `usecase/GetStartDestinationUseCase` |
| data | `local/SessionStorage` (interfaz), `local/EncryptedSessionStorage` (`EncryptedSharedPreferences`), `repository/SessionLocalRepository` |
| di | registro en `dataModule` y `featureModule` |
| ui | `feature/welcome/` (`WelcomeScreen` actualizada, `WelcomeViewModel`), `feature/startup/StartupViewModel` |
| navegación | `StartupRoute` como destino inicial; `LoginRoute` y `SignUpRoute`; `PetsRoute` y `BusinessPanelRoute` como marcadores |

Modelo de sesión guardada:

```kotlin
data class Session(val accessToken: String, val refreshToken: String, val hasBusiness: Boolean)
```

`GetStartDestinationUseCase` devuelve `StartDestination.Welcome`, `StartDestination.Pets` o `StartDestination.BusinessPanel`.

**Nota de alcance:** el almacenamiento cifrado de la sesión pertenece formalmente a HU-010. Se adelanta aquí el mínimo necesario (guardar, leer y borrar) porque sin él no se puede verificar el criterio 2 de esta historia ni el criterio 1 de HU-009. HU-010 solo tendrá que conectar `/auth/login`, `/auth/refresh` y `/auth/logout` encima de esta base.

## 5. Criterios de aceptación

| # | Dado que | Cuando | Entonces | Evidencia |
|---|---|---|---|---|
| 1 | no hay sesión | se abre la app | aparece la bienvenida con los tres botones | `StartupViewModelTest`, `WelcomeScreenTest` |
| 2 | hay una sesión válida guardada | se abre la app | entra directo a «Mis mascotas» o al panel | `StartupViewModelTest`, `GetStartDestinationUseCaseTest` |
| 3 | el visitante toca «Probar demo» | se abre la demo | la app entra en modo demo | `WelcomeScreenTest` |

## 6. Plan de pruebas

Unit (JUnit5 + coroutines-test): `StartupViewModelTest`, `GetStartDestinationUseCaseTest`, `SessionLocalRepositoryTest`.
UI (Compose, androidTest): `WelcomeScreenTest` (los tres botones y sus callbacks).

Manual con datos demo (`-PpetmateDataSource=local`): abrir la app sin sesión, guardar una sesión de prueba y volver a abrir, cerrar y reabrir la app para confirmar RF-57.

## 7. Riesgos, límites y deuda

- `PetsRoute` y `BusinessPanelRoute` son pantallas marcador: se llenan en HU-018 y HU-044. Aquí solo existe el destino para poder probar la decisión de arranque.
- «Probar demo» todavía no tiene el modo demo real (HU-013): por ahora abre el destino de demo disponible.
- La sesión guardada no se valida contra el servidor: se asume válida si existe. HU-010 agregará la renovación con `/auth/refresh` y el borrado si el refresh falla.
- `EncryptedSharedPreferences` y `MasterKey` ya están marcados como obsoletos en AndroidX y la compilación lo avisa. Se usan porque siguen funcionando y son lo que pide el RNF-06; la interfaz `SessionStorage` deja aislado el cambio a la alternativa que se decida, sin tocar pantallas ni casos de uso.
- Al terminar HU-010 hay que quitar de la bienvenida el acceso temporal a «¿Olvidaste tu contraseña?» y moverlo a la pantalla de inicio de sesión.

## Aprobación

| Estado | Revisor | Fecha | Evidencia |
|---|---|---|---|
| Borrador | — | — | — |

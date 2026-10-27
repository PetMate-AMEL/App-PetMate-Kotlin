# App-PetMate-Kotlin

Aplicación móvil Android de **PetMate**: el carnet digital de tu mascota.

## Stack

| Área | Tecnología |
|---|---|
| Lenguaje | Kotlin 2.2 |
| UI | Jetpack Compose + Material 3 |
| Navegación | Navigation Compose con rutas tipadas (`@Serializable`) |
| Inyección de dependencias | Koin 4 |
| Red | Retrofit 2 + OkHttp 4 + kotlinx.serialization |
| Pruebas | JUnit 5 + kotlinx-coroutines-test |
| Android | Mínimo 8.0 (API 26), compila con API 37 |

## Arquitectura

Capas separadas para que cada pantalla nueva siga el mismo camino:
**Repositorio (interfaz) → Caso de uso → ViewModel (UiState) → Pantalla → Ruta → DI**.

```text
app/src/main/java/com/example/patitasid/
├── common/        SealedResult y DomainError (éxito / error uniforme)
├── domain/        Interfaces de repositorio y casos de uso (sin Android)
├── data/
│   ├── remote/    Servicios Retrofit y safeApiCall (manejo común de errores)
│   └── repository/ Implementaciones local (demo) y remota (API)
├── di/            Módulos Koin: aquí se elige repositorio local o remoto
├── ui/
│   ├── theme/     Colores y tipografía de PetMate
│   ├── components/ Botones, campos y tarjetas reutilizables
│   ├── navigation/ Rutas y NavGraph
│   └── feature/   Una carpeta por pantalla (Screen + ViewModel)
└── util/          Utilidades (formato de fechas dd/mm/aaaa)
```

Cada historia de usuario se documenta en `specs/<HU>/` con `spec.md` y `tasks.md`.

## Cómo ejecutar

```bash
./gradlew assembleDebug            # compila el APK de debug
./gradlew testDebugUnitTest        # corre las pruebas unitarias
```

Opciones de compilación:

| Propiedad | Valores | Por defecto | Uso |
|---|---|---|---|
| `petmateDataSource` | `remote` / `local` | `remote` | `local` usa datos demo sin servidor |
| `petmateApiUrl` | URL del API | `https://10.0.2.2:7229/` | URL del servidor de pruebas |

Ejemplo: `./gradlew assembleDebug -PpetmateDataSource=local`

## Ramas

`sprintN-<Nombre>` → `sprintN` → `developer` → `release` → `main`

## Avance por sprint

### Sprint 1 (14/10/2026 al 23/10/2026)

#### Adrián Adonai García Jiménez — HU-003 · Proyecto Android base

> Como Eduardo (desarrollador de la app), quiero un proyecto base con navegación, estilos y repositorios definidos para construir pantallas sin duplicar código ni decidir la arquitectura cada vez.

Lo que se hizo:

- **Proyecto base** con Android 8.0 (API 26) como mínimo, Koin, Retrofit y kotlinx.serialization.
- **Navegación base**: Bienvenida → Inicio, con rutas tipadas.
- **Tema visual de PetMate** tomado del Figma (frame «01 · Bienvenida»): azul `#1F4E79`, naranja `#F4A261`, fondo `#F0F3F7`, y tipografías.
- **Componentes reutilizables**: `PetPrimaryButton`, `PetOutlinedButton`, `PetTextField` y `PetCard`, de al menos 48 dp (RNF-15).
- **Capa de datos local/remota**: interfaz `HealthRepository` con `HealthRemoteRepository` (API) y `HealthLocalRepository` (demo). Se cambia entre una y otra sin tocar las pantallas (RNF-22).
- **Cliente HTTP** hacia `GET /api/salud`, con manejo común de errores (`safeApiCall`). Sin internet muestra un aviso claro y la app no se cierra (RNF-18).
- **Textos en español de México** y fechas en `dd/mm/aaaa` (RNF-14).
- **Pruebas unitarias**: `SafeApiCallTest` y `HomeViewModelTest` (5 pruebas en verde).
- **Documentación**: `specs/HU-003-base-app/`.

Criterios de aceptación:

| # | Criterio | Estado |
|---|---|---|
| 1 | Se instala en Android 8.0, abre sin errores y muestra la navegación base | Compila con `minSdk 26`; falta prueba manual en emulador API 26 |
| 2 | Cambiar de repositorio remoto a local no modifica la pantalla | ✅ |
| 3 | Sin internet muestra un aviso claro y no se cierra | ✅ |

Pendiente: URL real del servidor de pruebas.

#### Eduardo — HU-011 · «Olvidé mi contraseña» y restablecimiento

> Como usuario que olvidó su contraseña, quiero pedir un enlace a mi correo y crear una contraseña nueva para volver a entrar sin perder mis datos.

Lo que se hizo:

- **Pantalla «Olvidé mi contraseña»**: pide el correo y siempre responde «Si el correo existe, te enviamos un enlace», sin revelar qué correos están registrados.
- **Deep link** `petmate://restablecer-contrasena/{token}` que abre la pantalla «Nueva contraseña».
- **Pantalla «Nueva contraseña»**: valida el enlace al abrirse, pide la contraseña dos veces (8+ caracteres con letras y números) y avisa si el enlace expiró, con botón «Pedir otro enlace».
- **Capa de datos local/remota**: `PasswordRepository` con `PasswordRemoteRepository` (API) y `PasswordLocalRepository` (demo; el token `expirado` simula un enlace vencido).
- **Componentes nuevos**: `PetPasswordField` (mostrar/ocultar), `PetTopBar` y `PetStatusMessage`.
- **Corrección**: `safeApiCallNoBody` trata cualquier 2xx como éxito; antes un 204 de `/api/salud` se mostraba como servidor caído.
- **Pruebas unitarias**: `ForgotPasswordViewModelTest`, `ResetPasswordViewModelTest`, `PasswordRemoteRepositoryTest` y `CredentialRulesTest`.
- **Documentación**: `specs/HU-011-forgot-password/`.

Criterios de aceptación:

| # | Criterio | Estado |
|---|---|---|
| 1 | Al tocar «Enviar enlace» ve «Si el correo existe, te enviamos un enlace» | ✅ |
| 2 | Al abrir el enlace y escribir una contraseña válida dos veces, se guarda y vuelve a iniciar sesión | ✅ guarda; «Iniciar sesión» regresa a Bienvenida hasta que exista Login |
| 3 | Si el enlace expiró, ve que ya no es válido y puede pedir otro | ✅ |

Pendiente: confirmar el contrato del API con Miguel (ver `spec.md`) y prueba manual en emulador.

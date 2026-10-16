# HU-003 base-app — Proyecto Android base: navegación, tema visual y capa de datos local/remota

> Estructura SDD tomada de MovilSDD (spec → plan → tasks). Sprint 1 (14/10/2026 al 16/10/2026). Responsable: Adrián Adonai García Jiménez.

## 1. Contexto y objetivo

Como Eduardo (desarrollador de la app) quiero un proyecto base con navegación, estilos y repositorios definidos para construir pantallas sin duplicar código ni decidir la arquitectura cada vez.
Alcance: proyecto Android (API mínima 26), navegación, tema PetMate, componentes reutilizables (botones, campos, tarjetas), repositorios con implementación local (demo) y remota (API de Miguel), cliente HTTP con manejo común de errores y de falta de conexión.
Fuera de alcance: registro, inicio de sesión y pantallas de negocio (siguientes HU).

## 2. Requisitos funcionales

- Navegación base: Bienvenida (frame Figma «01 · Bienvenida», ajustado al alcance) → Inicio.
- Inicio consulta `GET /api/salud` y muestra el estado del servidor con botón «Reintentar».
- Cada repositorio se define como interfaz en `domain/` con dos implementaciones en `data/`: local y remota.

## 3. Requisitos no funcionales

- RNF-14: textos en español de México; fechas `dd/mm/aaaa` (`util/DateFormats.kt`).
- RNF-15: botones y campos de al menos 48 dp (`ui/components`, 52 dp según diseño).
- RNF-18: sin internet la app muestra un aviso claro y no se cierra (`data/remote/SafeApiCall.kt`).
- RNF-20: Android 8.0 (API 26) o superior (`minSdk = 26`).
- RNF-22: cambiar de repositorio remoto a local no modifica pantallas (solo `di/AppModules.kt`).
- Seguridad: `allowBackup=false`, `usesCleartextTraffic=false`, URL fuera del código (propiedad Gradle `petmateApiUrl`).

## 4. Arquitectura y contratos internos

Model → Repository interface → UseCase → ViewModel(UiState) → Screen → Route → DI → Tests.

| Capa | Archivos |
|---|---|
| common | `SealedResult`, `DomainError` (Network, Server, Unexpected) |
| domain | `HealthRepository`, `CheckServerHealthUseCase` |
| data | `HealthApiService` (`GET api/salud`), `safeApiCall`, `HealthRemoteRepository`, `HealthLocalRepository` |
| di | `coreModule` (Retrofit/OkHttp), `dataModule` (elige local/remoto), `featureModule` |
| ui | `theme/`, `components/PetComponents.kt`, `navigation/` (rutas `@Serializable`), `feature/welcome`, `feature/home` |

Selección de fuente: `./gradlew assembleDebug -PpetmateDataSource=local` (por defecto `remote`).

## 5. Criterios de aceptación

| # | Dado que | Cuando | Entonces | Evidencia |
|---|---|---|---|---|
| 1 | el proyecto se compila | se instala en Android 8.0 | abre sin errores y muestra la navegación base | `minSdk 26`, prueba manual en emulador API 26 |
| 2 | una pantalla pide datos | se cambia de repositorio remoto a local | funciona igual sin modificar la pantalla (RNF-22) | `HomeViewModelTest` con `HealthLocalRepository` |
| 3 | no hay internet | la app intenta llamar al API | muestra un aviso claro y no se cierra (RNF-18) | `SafeApiCallTest`, `HomeViewModelTest` |

## 6. Plan de pruebas

Unit (JUnit5 + coroutines-test): `SafeApiCallTest`, `HomeViewModelTest`. Manual: emulador API 26 en modo avión.

## 7. Riesgos, límites y deuda

- URL del servidor de pruebas de Miguel pendiente: valor por defecto `https://10.0.2.2:7229/`; se sobreescribe con `-PpetmateApiUrl`.
- El cuerpo de `/api/salud` no se interpreta: cualquier 2xx = servidor activo.
- Sin modo oscuro ni fuente personalizada (Inter) por ahora.
- Toolchain del proyecto (AGP 9.2.1, Kotlin 2.2.10) distinta a MovilSDD (AGP 9.4.1, Kotlin 2.3.20); no se migró para no romper el build.

## Aprobación

| Estado | Revisor | Fecha | Evidencia |
|---|---|---|---|
| Borrador | — | — | — |

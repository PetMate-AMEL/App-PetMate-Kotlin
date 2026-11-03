# PLAY eliminar-cuenta — Eliminar mi cuenta

> Requisito de publicación en Google Play (política de eliminación de cuentas). Responsable: Eduardo.

## 1. Contexto y objetivo

Como usuario con cuenta en PetMate, quiero eliminar mi cuenta y mis datos desde la app para dejar de usar el servicio sin tener que pedirlo por otro medio.
Google Play exige que toda app que permite crear cuenta ofrezca eliminarla **desde la app** y **desde una página web**.
Alcance: pantalla «Eliminar mi cuenta» con confirmación, llamada al API y cierre de la sesión local.
Fuera de alcance: borrado de los datos en el servidor (API) y pantalla de ajustes de la cuenta (otra HU; mientras tanto el acceso está en «Mis mascotas» y «Panel del negocio»).

## 2. Requisitos funcionales

- Botón «Eliminar mi cuenta» en «Mis mascotas» y «Panel del negocio».
- La pantalla explica que se borran la cuenta, los datos personales y los de las mascotas, y que no se puede deshacer.
- Al tocar el botón se pide confirmación («Sí, eliminar» / «Cancelar»). Cancelar no borra nada.
- Si el API confirma, se borra la sesión guardada y la app regresa a «Bienvenida» sin poder volver atrás.
- Si falla (sin internet o error del servidor), se muestra el mensaje común y la sesión se conserva para reintentar.

## 3. Requisitos no funcionales

- RNF-14: textos en español de México.
- RNF-15: botón de al menos 48 dp (`PetOutlinedButton`).
- RNF-18: sin internet se muestra un aviso claro y se puede reintentar.
- RNF-22: `AccountLocalRepository` (demo) y `AccountRemoteRepository` (API) se intercambian solo en `di/AppModules.kt`.

## 4. Arquitectura y contratos

| Capa | Archivos |
|---|---|
| domain | `AccountRepository.deleteAccount`, `DeleteAccountUseCase` |
| data | `AccountApiService.deleteAccount`, `AccountRemoteRepository`, `AccountLocalRepository` |
| di | registro en `featureModule` |
| ui | `feature/account/` (`DeleteAccountScreen`, `DeleteAccountViewModel`), botón en `feature/pets/PlaceholderScreens.kt` |
| navegación | `DeleteAccountRoute` |

Contrato del API supuesto (**pendiente de confirmar con el equipo del API**):

| Método | Ruta | Encabezado | Respuestas |
|---|---|---|---|
| DELETE | `/api/cuenta` | `Authorization: Bearer {accessToken}` | `204` cuenta eliminada · `401` token inválido · `500` error |

## 5. Pruebas

- `DeleteAccountUseCaseTest`: borra la sesión solo si el API confirma; sin sesión no llama al API.
- `DeleteAccountViewModelTest`: confirmación, cancelar, éxito y error sin internet.
- `AccountRemoteRepositoryTest`: envía el token `Bearer` y conserva el error del servidor.

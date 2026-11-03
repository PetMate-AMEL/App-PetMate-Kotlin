# Publicar PetMate en Google Play

Paquete de la app en Play: **`com.petmateamel.app`**. Es permanente una vez que se sube el primer `.aab`.

## 1. Crear la llave de subida (solo una vez)

En la raíz del proyecto:

```bash
keytool -genkeypair -v -keystore petmate-upload.jks -keyalg RSA -keysize 2048 \
  -validity 10000 -alias petmate-upload
```

Después copia `keystore.properties.example` como `keystore.properties` y llena tus contraseñas.

> Guarda `petmate-upload.jks` y sus contraseñas en un lugar seguro (Drive personal, gestor de contraseñas).
> Ambos archivos están en `.gitignore`: **nunca** se suben a GitHub.

## 2. Generar el `.aab`

Con el API publicado:

```bash
./gradlew bundleRelease -PpetmateReleaseApiUrl=https://api.tudominio.com/
```

Sin API todavía (versión demo con datos locales, útil para la prueba cerrada):

```bash
./gradlew bundleRelease
```

El archivo queda en `app/build/outputs/bundle/release/app-release.aab`.

En cada nueva versión, sube `versionCode` (+1) y `versionName` en `app/build.gradle.kts`.

## 3. Archivos para la ficha de Play

| Archivo | Uso en Play Console |
|---|---|
| `icono-512.png` | Ícono de la app (512×512) |
| `grafico-destacado-1024x500.png` | Gráfico destacado |
| `politica-de-privacidad.md` | Publícala en una URL pública y pega el enlace en Play Console |
| `eliminar-cuenta.md` | Publícala en una URL pública: Play la pide en «Seguridad de los datos» |

Faltan las capturas de pantalla: mínimo 2, tomadas del teléfono o del emulador.

## 4. Pasos en Play Console

1. **Crear app**: nombre "PetMate", idioma Español (México), App, Gratis.
2. **Configurar la app** (panel principal): política de privacidad, acceso a la app (credenciales de prueba para revisores), anuncios (No), clasificación de contenido, público objetivo (18+ recomendado), Seguridad de los datos y eliminación de cuenta.
3. **Ficha de Play Store**: descripción breve y completa, ícono, gráfico destacado y capturas.
4. **Pruebas > Prueba cerrada**: crear la pista, subir el `.aab` y agregar al menos 12 testers por correo. Deben mantenerse inscritos 14 días seguidos (cuentas personales nuevas).
5. Al terminar los 14 días: **Solicitar acceso a producción** y publicar.

# Plataforma Educativa — Sistema de Inscripción

Microservicio desarrollado con Spring Boot para gestionar cursos e inscripciones de una plataforma educativa virtual, con almacenamiento de archivos en AWS S3, expuesto a través de AWS API Gateway y autenticado mediante Azure AD B2C como servicio IDaaS.

## Tecnologías
- Java 21
- Spring Boot 4.0.6
- Spring Security + JWT
- H2 (base de datos en memoria)
- AWS SDK v2 (S3)
- AWS API Gateway (HTTP API)
- Azure AD B2C (IDaaS)
- Docker

## Requisitos previos
- Java 21
- Maven
- Docker
- Cuenta AWS Academy
- Cuenta Azure

## Configuración

1. Clona el repositorio
2. Copia `.env.example` y renómbralo a `.env`
3. Completa los valores del `.env`
4. Ejecuta el proyecto

## Variables de entorno

| Variable | Descripción |
|---|---|
| `JWT_SECRET` | Clave secreta para firmar tokens JWT internos (mínimo 32 caracteres) |
| `AWS_ACCESS_KEY` | Access key obtenida desde AWS Academy |
| `AWS_SECRET_KEY` | Secret key obtenida desde AWS Academy |
| `AWS_SESSION_TOKEN` | Session token obtenido desde AWS Academy |
| `AWS_REGION` | Región del bucket S3 (ej: us-east-1) |
| `AWS_BUCKET_NAME` | Nombre del bucket S3 creado en AWS |

## Levantar el proyecto

### Local con Maven
```bash
mvn clean package -DskipTests
java -jar target/*.jar
```

### Con Docker
```bash
docker build -t plataforma-educativa:1.4 .
docker run -d -p 8080:8080 \
  -e JWT_SECRET=tu_secret \
  -e AWS_ACCESS_KEY=tu_access_key \
  -e AWS_SECRET_KEY=tu_secret_key \
  -e AWS_SESSION_TOKEN=tu_session_token \
  -e AWS_REGION=us-east-1 \
  -e AWS_BUCKET_NAME=tu_bucket \
  --name plataforma-educativa plataforma-educativa:1.4
```

## Usuarios de prueba

| Email | Password | Rol |
|---|---|---|
| admin@duoc.cl | password123 | ADMIN |
| profesor@duoc.cl | password123 | PROFESOR |
| alumno@duoc.cl | password123 | ALUMNO |

## Endpoints

### Auth
| Método | Endpoint | Acceso | Descripción |
|---|---|---|---|
| POST | `/auth/register` | Público | Registrar usuario |
| POST | `/auth/login` | Público | Login y obtener token JWT interno |

### Cursos
| Método | Endpoint | Acceso | Descripción |
|---|---|---|---|
| GET | `/cursos` | Todos | Listar cursos disponibles |
| GET | `/cursos/{id}` | Todos | Obtener curso por ID |
| POST | `/cursos` | ADMIN, PROFESOR | Crear curso |
| PUT | `/cursos/{id}` | ADMIN, PROFESOR | Actualizar curso |
| DELETE | `/cursos/{id}` | ADMIN, PROFESOR | Eliminar curso |

### Inscripciones
| Método | Endpoint | Acceso | Descripción |
|---|---|---|---|
| POST | `/inscripciones` | ADMIN, ALUMNO | Crear inscripción |
| GET | `/inscripciones/mis-inscripciones` | ADMIN, ALUMNO | Ver mis inscripciones |
| DELETE | `/inscripciones/{id}` | ADMIN, ALUMNO | Eliminar inscripción |
| GET | `/inscripciones/{id}/resumen` | ADMIN, ALUMNO | Descargar resumen y subir a S3 |

### Storage S3
> Los endpoints de storage están protegidos por AWS API Gateway mediante autorizador JWT de Azure AD B2C.
> Para consumirlos se requiere un Access Token obtenido desde Azure AD B2C, enviado en el header `Authorization: Bearer <token>`.

| Método | Endpoint | Descripción |
|---|---|---|
| POST | `/storage/upload/{folder}` | Subir archivo al bucket |
| GET | `/storage/download/{folder}/{fileName}` | Descargar archivo del bucket |
| PUT | `/storage/update/{folder}/{fileName}` | Reemplazar contenido de un archivo en el bucket |
| PUT | `/storage/move/{folder}` | Renombrar archivo en el bucket |
| DELETE | `/storage/delete/{folder}/{fileName}` | Eliminar archivo del bucket |

## Autenticación

### Endpoints internos (cursos e inscripciones)
1. Obtén el token haciendo login en `/auth/login`
2. Agrega el token en el header de cada request:
```
Authorization: Bearer <token_jwt_interno>
```

### Endpoints de storage (via API Gateway)
1. Obtén el Access Token desde Azure AD B2C usando OAuth 2.0 Client Credentials en Postman
2. Agrega el token en el header de cada request:
```
Authorization: Bearer <access_token_azure>
```

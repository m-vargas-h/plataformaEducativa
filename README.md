# Plataforma Educativa — Sistema de Inscripción

Microservicio desarrollado con Spring Boot para gestionar cursos e inscripciones de una plataforma educativa virtual, con almacenamiento de archivos en AWS S3, expuesto a través de AWS API Gateway y autenticado mediante Azure AD B2C como servicio IDaaS.

## Tecnologías
- Java 21
- Spring Boot 4.0.6
- Spring Security — OAuth2 Resource Server
- H2 (base de datos en memoria)
- AWS SDK v2 (S3)
- AWS API Gateway (HTTP API)
- Azure AD B2C (IDaaS)
- Docker
- GitHub Actions (CI/CD)

## Requisitos previos
- Java 21
- Maven
- Docker
- Cuenta AWS Academy
- Cuenta Azure con tenant AD B2C configurado

## Configuración

1. Clona el repositorio
2. Copia `.env.example` y renómbralo a `.env`
3. Completa los valores del `.env`
4. Ejecuta el proyecto

## Variables de entorno

| Variable | Descripción |
|---|---|
| `AZURE_ISSUER_URI` | Issuer URI del tenant Azure AD B2C |
| `AZURE_JWK_SET_URI` | JWK Set URI para verificar firma de tokens JWT |
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
docker build -t plataforma-educativa:1.5 .
docker run -d -p 8080:8080 \
  -e AZURE_ISSUER_URI=tu_issuer_uri \
  -e AZURE_JWK_SET_URI=tu_jwk_set_uri \
  -e AWS_ACCESS_KEY=tu_access_key \
  -e AWS_SECRET_KEY=tu_secret_key \
  -e AWS_SESSION_TOKEN=tu_session_token \
  -e AWS_REGION=us-east-1 \
  -e AWS_BUCKET_NAME=tu_bucket \
  --name plataforma-educativa plataforma-educativa:1.5
```

## Autenticación

Todos los endpoints están protegidos mediante Azure AD B2C. Se requiere un Access Token válido obtenido desde Azure AD B2C usando OAuth 2.0.

### Obtener token en Postman
1. Auth Type: `OAuth 2.0`
2. Grant Type: `Client Credentials`
3. Access Token URL: `https://login.microsoftonline.com/<tenant-id>/oauth2/v2.0/token`
4. Client ID: `<application-client-id>`
5. Client Secret: `<client-secret>`
6. Scope: `https://<tenant-name>.onmicrosoft.com/<client-id>/.default`

### Usar el token
```
Authorization: Bearer <access_token>
```

## Endpoints

### Cursos
| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/cursos` | Listar cursos disponibles |
| GET | `/cursos/{id}` | Obtener curso por ID |
| POST | `/cursos` | Crear curso |
| PUT | `/cursos/{id}` | Actualizar curso |
| DELETE | `/cursos/{id}` | Eliminar curso |

### Inscripciones
| Método | Endpoint | Descripción |
|---|---|---|
| POST | `/inscripciones` | Crear inscripción |
| GET | `/inscripciones/mis-inscripciones` | Ver mis inscripciones |
| DELETE | `/inscripciones/{id}` | Eliminar inscripción |
| GET | `/inscripciones/{id}/resumen` | Descargar resumen y subir a S3 |

### Storage S3
| Método | Endpoint | Descripción |
|---|---|---|
| POST | `/storage/upload/{folder}` | Subir archivo al bucket |
| GET | `/storage/download/{folder}/{fileName}` | Descargar archivo del bucket |
| PUT | `/storage/update/{folder}/{fileName}` | Reemplazar contenido de un archivo |
| PUT | `/storage/move/{folder}` | Renombrar archivo en el bucket |
| DELETE | `/storage/delete/{folder}/{fileName}` | Eliminar archivo del bucket |

## Arquitectura

```
Cliente → AWS API Gateway (JWT Authorizer) → EC2:8080 (Spring Boot)
                    ↑                               ↑
             Azure AD B2C                  Spring Security
           (valida token)                (OAuth2 Resource Server)
```

## CI/CD

El proyecto usa GitHub Actions para el despliegue automático:
1. Build y test con Maven
2. Build y push de imagen Docker a DockerHub
3. Deploy en AWS EC2 vía SSH

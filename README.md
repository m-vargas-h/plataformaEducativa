# Plataforma Educativa — Sistema de Inscripción

Microservicio desarrollado con Spring Boot para gestionar cursos e inscripciones de una plataforma educativa virtual.

## Tecnologías
- Java 21
- Spring Boot 4.0.6
- Spring Security + JWT
- H2 (base de datos en memoria)
- Docker

## Requisitos previos
- Java 21
- Maven
- Docker

## Configuración

1. Clona el repositorio
2. Copia `.env.example` y renómbralo a `.env`
3. Completa los valores del `.env`
4. Ejecuta el proyecto

## Variables de entorno

| Variable | Descripción |
|---|---|
| `JWT_SECRET` | Clave secreta para firmar tokens JWT (mínimo 32 caracteres) |

## Levantar el proyecto

### Local con Maven
```bash
mvn clean package
java -jar target/*.jar
```

### Con Docker
```bash
docker build -t plataforma-educativa:1.0 .
docker run -d -p 8080:8080 --name plataforma-educativa plataforma-educativa:1.0
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
| POST | `/auth/login` | Público | Login y obtener token JWT |

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

## Uso de endpoints protegidos

1. Obtén el token haciendo login en `/auth/login`
2. Agrega el token en el header de cada request:
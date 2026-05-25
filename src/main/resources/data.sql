-- Usuarios (contraseñas encriptadas con BCrypt, todas son "password123")
INSERT INTO usuario (id, nombre, email, password, rol) VALUES
(1, 'Admin Sistema', 'admin@duoc.cl', '$2a$10$VI57aiihCK0uxtgUP6Vy6e0LwlQCRghACNeZYalnhTvCkIU2e3MYO', 'ADMIN'),
(2, 'Juan Profesor', 'profesor@duoc.cl', '$2a$10$VI57aiihCK0uxtgUP6Vy6e0LwlQCRghACNeZYalnhTvCkIU2e3MYO', 'PROFESOR'),
(3, 'María Alumna', 'alumno@duoc.cl', '$2a$10$VI57aiihCK0uxtgUP6Vy6e0LwlQCRghACNeZYalnhTvCkIU2e3MYO', 'ALUMNO');

-- Cursos
INSERT INTO curso (id, nombre, instructor, duracion, costo) VALUES
(1, 'Java Avanzado', 'Juan Profesor', 40, 49990),
(2, 'Spring Boot', 'Juan Profesor', 30, 39990),
(3, 'Cloud Native', 'Juan Profesor', 35, 44990);
FROM mysql:latest AS onlinestore_db
# Autor
LABEL author="Daniel Boj"

# Establecemos las variables de entorno de la BD.
ENV MYSQL_ROOT_USER=root
ENV MYSQL_ROOT_PASSWORD=ciricefp
ENV MYSQL_DATABASE=onlinestore_db

# Archivo SQL de inicialización de la base de datos
COPY CiriceFP/CiriceFP-OnlineStore/src/main/resources/script_olinestore_db.sql /docker-entrypoint-initdb.d/

EXPOSE 3306
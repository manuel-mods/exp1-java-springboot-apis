FROM amazoncorretto:17-alpine
# Usar la imagen base de OpenJDK

# Crear un directorio de trabajo para la aplicación
WORKDIR /app

# Copiar el archivo JAR de la aplicación al directorio de trabajo
COPY target/demo-0.0.1-SNAPSHOT.jar app.jar

# Copiar el Oracle Wallet al contenedor
# Supongamos que el Oracle Wallet está en el directorio local `wallet` y lo copiamos a `/app/wallet` dentro del contenedor
COPY wallet /app/wallet

# Establecer la variable de entorno TNS_ADMIN para que apunte al directorio del wallet
ENV TNS_ADMIN=/app/wallet

# Exponer el puerto que utilizará la aplicación
EXPOSE 8080

# Ejecutar la aplicación Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]

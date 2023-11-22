#### Stage 1: Build the application
FROM openjdk:11 as build

# Set the current working directory inside the image
WORKDIR /app

# Copy maven executable to the image
COPY TonTubeWS/tontube/mvnw .
COPY TonTubeWS/tontube/.mvn .mvn

# Copy the pom.xml file
COPY TonTubeWS/tontube/pom.xml .

# remove ending of window format
RUN sed -i 's/\r$//' mvnw

# Build all the dependencies in preparation to go offline. 
# This is a separate step so the dependencies will be cached unless 
# the pom.xml file has changed.
RUN chmod +x ./mvnw
RUN ./mvnw dependency:go-offline -B

# Copy the project source
COPY TonTubeWS/tontube/src src

# Package the application
RUN ./mvnw package -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

#### Stage 2: A minimal docker image with command to run the app 
FROM openjdk:11

ARG DEPENDENCY=/app/target/dependency

# Copy project dependencies from the build stage
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

ENTRYPOINT ["java","-cp","app:app/lib/*","ch.hearc.spring.TonTubeApplication"]

# Dockerfile
FROM node:lts

WORKDIR /app-front

COPY tontube-front/package.json .

RUN npm install

COPY tontube-front/ .

RUN npm run build

EXPOSE 3000

ENV NUXT_HOST=0.0.0.0
ENV NUXT_PORT=3000

CMD [ "npm", "start" ]
CMD [ "CD", ".." ]
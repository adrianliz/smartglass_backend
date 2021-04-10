FROM adoptopenjdk:11-jre-hotspot as builder
WORKDIR smartglass
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} smartglass-backend.jar
RUN java -Djarmode=layertools -jar smartglass-backend.jar extract

FROM adoptopenjdk:11-jre-hotspot
WORKDIR smartglass
COPY --from=builder smartglass/dependencies/ ./
COPY --from=builder smartglass/spring-boot-loader/ ./
COPY --from=builder smartglass/snapshot-dependencies/ ./
RUN true
COPY --from=builder smartglass/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
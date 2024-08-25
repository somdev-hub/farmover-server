FROM eclipse-temurin:22
 
COPY target/*.jar farm.jar
EXPOSE 9090
ENTRYPOINT [ "java","-jar","farm.jar" ]
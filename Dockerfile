FROM adoptopenjdk/openjdk11:latest

RUN mkdir -p /software

ADD target/*.jar /software/smartglass-backend.jar

CMD java -Dserver.port=$PORT -Xmx300m -Xss512k -XX:CICompilerCount=2 -Dfile.encoding=UTF-8 -XX:+UseContainerSupport -jar /software/smartglass-backend.jar
FROM adoptopenjdk/openjdk11

WORKDIR /
ADD target/scala-2.13/bacscan.jar bacscan.jar

ENTRYPOINT [ "java", "-jar", "bacscan.jar" ]

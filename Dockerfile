FROM adoptopenjdk:11-jre-hotspot
COPY target/transactions-1.jar
ENTRYPOINT ["java","-java","/transactions.jar"]
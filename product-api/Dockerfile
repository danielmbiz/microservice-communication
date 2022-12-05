FROM maven:3.6.3-jdk-11
WORKDIR .
COPY . .
RUN mvn clean install
EXPOSE 8081
CMD ["mvn", "spring-boot:run"]
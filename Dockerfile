FROM eclipse-temurin:17-jdk-focal AS builder
WORKDIR /app
COPY . ./
RUN ./mvnw package -Dmaven.test.skip

FROM eclipse-temurin:17-jre-focal
RUN groupadd -r -g 2200 app && useradd -r -M -s /bin/bash -g app -u 2200 user
WORKDIR /app
RUN mkdir file
COPY --from=builder /app/target/RegexDataGenerator-jar-with-dependencies.jar app.jar
COPY start.sh ./start.sh
COPY arg.sh ./arg.sh
COPY env.sh ./env.sh
RUN chmod +x ./app.jar && chmod +x ./env.sh && chmod +x ./arg.sh && chmod +x ./start.sh
RUN chown -R user:app /app
USER user
ENTRYPOINT ["/app/start.sh"]

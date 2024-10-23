FROM adoptopenjdk/openjdk11:latest as build
RUN mkdir -p /dsk/app
COPY . /dsk/app
WORKDIR /dsk/app
RUN ls -R /dsk/app  # Debugging
RUN chmod +x ./mvnw  # Ensure mvnw is executable
RUN ./mvnw package
RUN ls -R /dsk/app  # Debugging


FROM adoptopenjdk/openjdk11:latest as run
COPY --from=build /dsk/app/target /dsk/app
CMD ["java", "-cp", "/dsk/app/dsk_consumer_service-1.0-SNAPSHOT.jar:/dsk/app/lib/*", "com.dsk.consumer.startup.Startup"]
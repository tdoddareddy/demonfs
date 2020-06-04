# demonfs
demo app for TAS / PAS Volume Services functionality

### Getting Started

* ./mvnw clean install (Create fat jar)
* ./mvnw spring-boot:run (Only to test locally)
* cf push -f manifest.yaml (Deploy to TAS)

### Testing CF Volume Services -- Utilize container_dir present in volume_mounts of VCAP_SERVICES nfs

* curl http://localhost:8080/actuator/env -- Dump application environment data in json format
* curl http://localhost:8080/ -- Returns "Greetings from Spring Boot!"
* curl http://localhost:8080/file/list -- Returns list of files in json format in the mounted directory
* curl -T source-file http://localhost:8080/file/create/{destination-file-name} -- Creates a file with contents of source-file at the mounted directory with {destination-file-name}
* curl -X DELETE http://localhost:8080/file/delete/{file-name} -- Replace file-name with actual file name to deleted from mounted directory
* Replace localhost with application route when running app on TAS

### Testing CF Volume Services -- Utilize mount passed in cf bind-service

* curl http://localhost:8080/actuator/env -- Dump application environment data in json format
* curl http://localhost:8080/file1/ -- Returns "Greetings from Spring Boot!"
* curl http://localhost:8080/file1/list -- Returns list of files in json format in the mounted directory
* curl -T source-file http://localhost:8080/file1/create/{destination-file-name} -- Creates a file with contents of source-file at the mounted directory with {destination-file-name}
* curl -X DELETE http://localhost:8080/file1/delete/{file-name} -- Replace file-name with actual file name to deleted from mounted directory
* Replace localhost with application route when running app on TAS


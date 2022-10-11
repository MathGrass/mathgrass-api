# Developer Documentation Mathgrass API
## Developer Setup
- install dependencies
- execute `build generateApi` from terminal in api root or use IntelliJ: `Gradle Tasks -> other -> generateApi`
- launch with .env file (use IntelliJ SpringBoot Config and EnvFile Plugin)
- for swager docs go to: `http://localhost:8080/swagger-ui/`
- install postgres on host
- connect to postgres and run "CREATE DATABASE mathgrass-db"

![IntelliJ Setup](docs/intellij-debug-setup.png?raw=true "IntelliJ Setup")


## Swagger Spec
- can be found in `openapi-spec/graphex-minimal.yaml`
- endpoints in the beginning of the file
- dtos in the end of the file

## Inner Workings
- API und DB models are transformed to each other by classes that inherit from ModelTransformer under `src/main/java/de/tudresden/inf/st/mathgrassserver/transform`



### Entity Relationship Diagram

![Entity Relationship Diagram](docs/er-diagram.png?raw=true "Entity Relationship Diagram")




# Building & Deploying MathGrass

In the following the process of building and deploying MathGrass is described. Before that it is to be ensured that all dependencies are installed. For that refer to `Development & Operations->Prerequisites`.

## Database
- Connect to postgres and run `CREATE DATABASE mathgrass-db`
- Configure database with `api/src/main/resources/application.properties`

## API
- Execute `build generateApi` from terminal in api root or use IntelliJ: `Gradle Tasks -> other -> generateApi`
- Configure with .env file and us3 EnvFile Plugin
- For IntelliJ setup use Spring Boot config and see picture below 
![IntelliJ Setup](../images/intellij-debug-setup.png?raw=true "IntelliJ Setup")
(for more information see [here](https://www.jetbrains.com/help/idea/spring-boot.html))

## Evaluator
- Install requirements via `pip install -r requirements.txt`






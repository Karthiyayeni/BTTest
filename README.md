**Session Log Processor Application**
This application processes session log data to calculate session durations per user.

Building the Application
To build the application and create the executable JAR file, follow these steps:

Build with Maven:

mvn clean install

This command compiles the source code, runs tests, and packages the application into a JAR file located at target/session-log-processor.jar.

Building Docker Image:
To build a Docker image for the Session Log Processor application, execute the following command from the project's root directory:
docker 

buildx build -t session_log_processor D:/BT

This command builds a Docker image tagged as session_log_processor using the Dockerfile located at D:/BT.(This is the working directory at where the docker file is placed)


Running the Docker Container
After building the Docker image,  the Session Log Processor application in a Docker container can be run with the following command:

docker run -it --rm session_log_processor

This command starts a new Docker container from the session_log_processor image. The application processes the session log data provided within the container environment.




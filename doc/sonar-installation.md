
# Install and run SonarQube Analysis


## 1. Install
Easiest way: Use DockerCompose
https://docs.sonarsource.com/sonarqube/latest/setup-and-upgrade/install-the-server/installing-sonarqube-from-docker/

## 1.1 Watch Pre-Installation steps
https://docs.sonarsource.com/sonarqube/latest/setup-and-upgrade/pre-installation/linux/

Execute the settings in your linux-on-windows subsystem (wsl)
>sysctl -w vm.max_map_count=524288
>sysctl -w fs.file-max=131072
>ulimit -n 131072
>ulimit -u 8192

## 1.2 Docker Compose
https://docs.sonarsource.com/sonarqube/latest/setup-and-upgrade/install-the-server/installing-sonarqube-from-docker/

Create a dockerCompose file with the content from Appendix 1:
Put it into your docker-installation directory

start sonarqube locally (in the background)

>docker compose up -d

The sonarqube server runs on localhost:9000

#2. Run the analysis
you can now run the analysis with maven
>mvn verify sonar:sonar



# Appendix 1: Docker Compose file content

version: "3"

services:
  sonarqube:
    image: sonarqube:community
    depends_on:
      - db
    environment:
      SONAR_JDBC_URL: jdbc:postgresql://db:5432/sonar
      SONAR_JDBC_USERNAME: sonar
      SONAR_JDBC_PASSWORD: sonar
    volumes:
      - sonarqube_data:/opt/sonarqube/data
      - sonarqube_extensions:/opt/sonarqube/extensions
      - sonarqube_logs:/opt/sonarqube/logs
    ports:
      - "9000:9000"
  db:
    image: postgres:12
    environment:
      POSTGRES_USER: sonar
      POSTGRES_PASSWORD: sonar
    volumes:
      - postgresql:/var/lib/postgresql
      - postgresql_data:/var/lib/postgresql/data

volumes:
  sonarqube_data:
  sonarqube_extensions:
  sonarqube_logs:
  postgresql:
  postgresql_data:
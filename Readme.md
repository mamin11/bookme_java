## Run resources
- execute `docker compose up -d` to start the following
  - rabbitmq 
  - mariadb
  - mailhog
- The resources are in `.resources` folder. RabbitMQ has custom config 
   setup to auto create notification queues
- run `./mvnw clean package jib:build -pl . -am -DskipTests ` to build image
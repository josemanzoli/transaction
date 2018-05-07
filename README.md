# Transaction API

Project that implements two basic endpoints
- One to POST transactions
- One to POST multiple Payments

## Suggestion for improvement
- 

## Requirements

* [Java8](http://www.oracle.com/technetwork/pt/java/javase/downloads/jdk8-downloads-2133151.html)
* [Maven] (https://maven.apache.org/)

#### Mac Os

* [Docker](https://docs.docker.com/docker-for-mac/)

#### Linux

* [Docker](https://docs.docker.com/engine/installation/linux/ubuntulinux/)

### Development

 * Some IDE of your choice *[Eclipse] (http://www.eclipse.org/home/index.php) *[Netbeans] (https://netbeans.org/) *[IntelliJ IDEA] (https://www.jetbrains.com/idea/)

## Installation

### Docker

##### Setup

```
mvn clean install
docker-compose up --build
```

## Executing commands inside the container.
If you want to check out what is inside the container

```
docker ps
```
The result should be like this:
```
CONTAINER ID        IMAGE               COMMAND                  CREATED                  STATUS              PORTS                    NAMES
c32dc1bd81ee        transacti           "/usr/lib/jvm/java-8…"   Less than a second ago   Up 2 seconds        0.0.0.0:8080->8080/tcp   transaction
```

Get the CONTAINER ID and execute this:
```
docker exec -it c32dc1bd81ee /bin/bash
```
That's it, now you can execute commands inside the container.

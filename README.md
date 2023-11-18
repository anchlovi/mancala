# Mancala Game
Online Mancala Game

## IDE

This app uses `JOOQ`. To generate the JOOQ classes, run the following command:
```sh
./gradlew generateJooq
```

To start the database, run the following command
```sh
docker-compose up db
```

No need to run migrations, as the game application is already configured to run the migrations on startup using `Liquibase`.

Import the game application your IDE and execute the main class `com.bol.mancalaapp.Application` to start the service.

The game will be available at `http://localhost:8080`

## Command Line

Build the game using the command below:
```sh
./gradlew clean build
```

To start the game, run the following command
```sh
docker-compose up -d --build
```

To stop the game, run the following command
```sh
docker-compose down
```

The game will be available at `http://localhost:8080`
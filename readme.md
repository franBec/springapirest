# springapirest

A Spring Boot application made by Pollito
https://pollitodev.netlify.app/

## Motivation \(why did I made this app?\)

This app started as a task from an interview for Devsu Software. Even though it is not complete, I will improve on it
just for personal pleasure

## How to start the application

This is an standard Java Spring Boot Maven application. For further details check the pom.xml file at the root of the
project

When booting up, the application creates a in memory database (h2) using the queries in src/main/resources/import.sql,
creating some mock data to start working with

# How to use it

If everything goes well, the application should be running in port 8080. You can import the postman collection located
in src/main/resources/springapirest.postman_collection.json, and create the following global variables \(or replace
them when invoked\)

- baseUrl = http://localhost:8080/api
- clientesUrl = /clientes
- cuentasUrl = /cuentas
- movimientosUrl = /movimientos

## Things still left to do

- Write more tests: only wrote tests for ClienteService and CuentaService. Also some integration test would be nice
- Check why annotations in PaginationUtils does not work
- Check why dates are kinda sloppy when asking for report (timezones and stuff)
- Add swagger
- Dockerize it

Thnks for your time! ~Pollito

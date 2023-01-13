# Vehicle Charging Station Management System

This assignment provides a car charging management service.

# Task Description

The task is to implement Rest-API for the electric vehicle charging station management system.

Notes:

* You are free to choose any kind of technology/language that fits you the best.
* You must use the provided database schema in your implementation, however, feel free to add/modify everything as
  needed.
* Pay attention to the scalability of the API.
* One charging company can own one or more other charging companies.

  Hence, the parent company should have the access to all the child company's stations hierarchically. For example, we
  got 3 companies A, B and C accordingly with 10,5 and 2 stations. Company B belongs to A and company C belongs to B.
  Then we can say that company A has 17, company B has 7 and company C has 2 stations in total.

#### The database schema for start point:

    1. Station (id, name, latitude, longitude, company_id)
    2. Company (id, parent_company_id, name)

## Task 1

##### Api should support CRUD for stations and companies.

## Task 2

##### Implement endpoint which gets all stations.

* Within the radius of n kilometers from a point (latitude, longitude) ordered by distance.
* Including all the children stations in the tree, for the given company_id.

## Task 3

##### Write a simple, not fancy interface that will consume your API programmatically.

## Task 4

##### You will get extra 100 points if you will do all with TDD :D (it is optional though).

## Prerequisites

#### Java Version 14

#### Maven 3+

#### PostgresSql version 14

You need postgress to run this application. The information for connecting to database should be provided
in `application.yml` config file.

You also need run scripts/00_initial_scripts.sql at first on your database.

## How to run

To run this app by maven wrapper command run below command on you terminal:
<p></p>
For Linux users: 

```shell
./mvnw spring-boot:run
```

For Windows uses:

```shell
./mvnw.cmd spring-boot:run
```

## Build

To build this application, you need maven 3+ to be installed on your system. After installing maven run the following
command:

```shell
./mvn package
```

Then go to the target directory and run the following command:

```shell
java -jar target/vehicle-station-management-1.0-SNAPSHOT.jar

```

### Calling APIs

The application default port is 8080, and after running successfully, you can find the swagger address at
http://localhost:8080/swagger-ui/


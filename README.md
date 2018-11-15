# bpm-people-time-api

## Build Status and Code coverage

[![Build Status](https://travis-ci.org/ioet/bpm-people-time-api.svg?branch=master)](https://travis-ci.org/ioet/bpm-people-time-api)
[![Code Coverage](https://codecov.io/gh/ioet/bpm-people-time-api/branch/master/graph/badge.svg)](https://codecov.io/gh/ioet/bpm-people-time-api)

## Run it locally

First run this to start eureka and edge server
```
docker-compose up
```
Then start the people-time-api using your IDE or this command
```
./gradlew bootRun
```

## Swagger

Swagger is integrated and available in this URL:

```
http://IP_ADDRESS:PORT/swagger-ui.html
```

## Postman
There is a Postman Collection included to test the api.  
You can run it with newman with this command:
```
newman run postman/collection.json -e postman/env.json
```

## Docker

The project has integrated a docker plugin so you can generate a docker image using the following Gradle task:

```
$ ./gradlew build docker
```

Don't forget to pass the `AWS_ACCESS_KEY_ID` and `AWS_SECRET_ACCESS_KEY` to make it work locally.
For any other environment the credentials should be provided by the CI server.



## Dynamo

In order to make the API works and establish a connection with Dynamo (Cloud DB provided by AWS) you'll need to export the following environment variables:

```
export AWS_ACCESS_KEY_ID="YOUR_ACCESS_KEY"
export AWS_SECRET_ACCESS_KEY="YOUR_SECRET_KEY"
export AWS_REGION="us-east-1"
```

If you don't have AWS Credentials, feel free to ask Juan Garcia, Roland or Rene Enriquez for it. Your account needs to be part of the group bpm-people to have enough permission to access Dynamo tables which are part of this project. 


## Configuring IntelliJ IDE
If you want to run the application from IntelliJ you must configure the required environment variables following the next steps:

Setting the environment variables

```
1. Go to menu Run and choose Edit Configurations.
2. It will show a configurations window. Go to the tab Configuration.
3. Add the required environment variables AWS_SECRET_ACCESS_KEY, AWS_ACCESS_KEY_ID, and AWS_REGION with the correspondent values.
```

Run or Debug

```
Now, you can run or debug the app from IntelliJ, you can use JRbel to debug and redeploy the app.
```

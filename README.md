# bpm-people-time-api

## Build Status and Code coverage

[![Build Status](https://travis-ci.org/ioet/bpm-people-time-api.svg?branch=master)](https://travis-ci.org/ioet/bpm-people-time-api)
[![Code Coverage](https://codecov.io/gh/ioet/bpm-people-time-api/branch/master/graph/badge.svg)](https://codecov.io/gh/ioet/bpm-people-time-api)

## Run it locally

Set up your own credentials to be able to connect to dependent AWS services:

```
export AWS_ACCESS_KEY_ID="YOUR_ACCESS_KEY"
export AWS_SECRET_ACCESS_KEY="YOUR_SECRET_KEY"
export AWS_REGION="us-east-1"
```


Then start the people-time-api using your IDE or this command
```
./gradlew bootRun
```


And you can access the people-time-api and it's swagger here: 
```
http://localhost:8084/people-time
http://localhost:8084/swagger-ui.html
```

## Postman
There is a Postman Collection included to test the api.  
Install Newman in your machine
```
$ npm install -g newman
```

### Running locally
```
newman run postman/collection.json -e postman/env.json
```

### Running against AWS
```
newman run postman/collection.json -e postman/aws-env.json
```

## Playing with the API 
Query time-templates

```
curl -X GET http://localhost:8084/time-templates?personId=a7c759a7-190d-47e9-baf7-ed89b4de9783
```

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

## Deploying as Lambda

### Create a S3 bucket
```
aws s3 mb s3://cf-template-spring-boot-apps-as-lambda
```

### Generate the bundle
Generate the artifact containing the code
```
./gradlew buildZip
```

### Package the CouldFormation template
```
aws cloudformation package --template-file sam-people-time-api.yml --output-template-file output-sam-people-time-api.yml --s3-bucket cf-template-spring-boot-apps-as-lambda --s3-prefix people-time-api
```

### Deploy the code as lambda

- Deploy the code to AWS
```
aws cloudformation deploy --template-file output-sam-people-time-api.yml --stack-name bpm-people-time-api --capabilities CAPABILITY_IAM
```

- Get the URL
```
aws cloudformation describe-stacks --stack-name bpm-people-time-api
```

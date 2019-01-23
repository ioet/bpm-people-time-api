package com.ioet.bpm.peopletime.aws.handler;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.ioet.bpm.peopletime.BpmPeopleTimeApiApplication;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AwsLambdaHandler implements RequestStreamHandler {


    private final SpringBootLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;

    public AwsLambdaHandler() throws ContainerInitializationException {
        handler = SpringBootLambdaContainerHandler.getAwsProxyHandler(BpmPeopleTimeApiApplication.class);
    }

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        handler.proxyStream(inputStream, outputStream, context);
        outputStream.close();
    }
}

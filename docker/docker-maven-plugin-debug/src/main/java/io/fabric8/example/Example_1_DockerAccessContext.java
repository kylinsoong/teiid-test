package io.fabric8.example;

import java.util.Properties;

import io.fabric8.maven.docker.access.DockerAccess;
import io.fabric8.maven.docker.service.DockerAccessFactory;

public class Example_1_DockerAccessContext {

    public static void main(String[] args) throws Exception {

        DockerAccessFactory.DockerAccessContext dockerAccessContext = new DockerAccessFactory.DockerAccessContext.Builder()
                .dockerHost(null)
                .certPath(null)
                .machine(null)
                .maxConnections(100)
                .minimalApiVersion(null)
                .projectProperties(new Properties())
                .log(new SampleLogger())
                .skipMachine(false)
                .build();
        
        DockerAccessFactory factory = new DockerAccessFactory();
        
        DockerAccess access = factory.createDockerAccess(dockerAccessContext);
        
        System.out.println(access);
    }

}

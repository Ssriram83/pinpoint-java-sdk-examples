## Pinpoint Java SDK Example

This is an example maven project - to demonstrate how to demonstrate: 
1. How to invoke Pinpoint SDK from Java. 
2. How to package this into a "Fat Jar" so that it can be deployed into systems like Pega / OSB for custom java integrations. 

### Instructions:

#### Pre-reqs: 
1. [Install Maven](https://maven.apache.org/install.html)
2. [Setup Pinpoint AWS project](https://pinpoint-jumpstart.workshop.aws/en/prerequisites/create-a-project.html) 

#### Build


```shell

git clone https://github.com/Ssriram83/pinpoint-java-sdk-examples
mvn clean compile assembly:single
```

If this is successful - a jar will be created in the folder: target/pinpoint-java-client-1.0-SNAPSHOT-jar-with-dependencies.jar

#### Run

```shell
java -cp pinpoint-java-client-1.0-SNAPSHOT-jar-with-dependencies.jar com.example.pinpoint.SendSMSMessage "Test Pinpoint Message" "YOUR_Pinpoint_PROJECT"  "YOUR_PHONE_NUMBER" ACCESS_KEY SECRET

```

In some cases its required to create custom credentials provider. To illustrate this - have included below snippet in SendSMSMessage.java 

```java

 PinpointClient pinpoint = PinpointClient.builder()
                    .credentialsProvider(new AwsCredentialsProvider() {
                        @Override
                        public AwsCredentials resolveCredentials() {
                            return AwsBasicCredentials.create(accessKey, accessValue);
                        }
                    })
                    .region(Region.of(region))
                    .build();
```

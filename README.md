## Pinpoint Java SDK Example

This is an example maven project - to demonstrate how to demonstrate: 
1. How to invoke Pinpoint SDK from Java. 
2. How to package this into a "Fat Jar" so that it can be deployed into systems like Pega / OSB for custom java integrations. 

### Instructions:

#### Build

The above comma

```shell

git clone https://github.com/Ssriram83/pinpoint-java-sdk-examples
mvn clean compile assembly:single
```

If this is successful - a jar will be created in the folder: pinpoint-java-client-1.0-SNAPSHOT-jar-with-dependencies.jar

#### Run

```shell
java -cp pinpoint-java-client-1.0-SNAPSHOT-jar-with-dependencies.jar com.example.pinpoint.SendSMSMessage "Test Pinpoint Message" "YOUR_Pinpoint_PROJECT"  "YOUR_PHONE_NUMBER"

```


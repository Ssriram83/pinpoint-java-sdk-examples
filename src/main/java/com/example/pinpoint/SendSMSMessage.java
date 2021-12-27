package com.example.pinpoint;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.pinpoint.PinpointClient;
import software.amazon.awssdk.services.pinpoint.model.*;

import java.util.HashMap;
import java.util.Map;

public class SendSMSMessage {

    public static String messageType = "TRANSACTIONAL";

    public static String registeredKeyword = "myKeyword";
    public static String senderId = "MySenderID";

        public static void sendSMSMessage (PinpointClient pinpoint, String message, String appId, String
        originationNumber, String destinationNumber){

            try {

                Map<String, AddressConfiguration> addressMap =
                        new HashMap<String, AddressConfiguration>();

                AddressConfiguration addConfig = AddressConfiguration.builder()
                        .channelType(ChannelType.SMS)
                        .build();

                addressMap.put(destinationNumber, addConfig);

                SMSMessage smsMessage = SMSMessage.builder()
                        .body(message)
                        .messageType(messageType)
                        .originationNumber(originationNumber)
                        .senderId(senderId)
                        .keyword(registeredKeyword)
                        .build();

                // Create a DirectMessageConfiguration object
                DirectMessageConfiguration direct = DirectMessageConfiguration.builder()
                        .smsMessage(smsMessage)
                        .build();

                MessageRequest msgReq = MessageRequest.builder()
                        .addresses(addressMap)
                        .messageConfiguration(direct)
                        .build();

                // create a  SendMessagesRequest object
                SendMessagesRequest request = SendMessagesRequest.builder()
                        .applicationId(appId)
                        .messageRequest(msgReq)
                        .build();

                SendMessagesResponse response = pinpoint.sendMessages(request);

                MessageResponse msg1 = response.messageResponse();
                Map map1 = msg1.result();

                //Write out the result of sendMessage
                map1.forEach((k, v) -> System.out.println((k + ":" + v)));

            } catch (PinpointException e) {
                System.err.println(e.awsErrorDetails().errorMessage());
                System.exit(1);
            }
        }

        public static void main (String[]args){
            final String USAGE = "\n" +
                    "Usage: " +
                    "SendMessage <message> <appId> <originationNumber> <destinationNumber> \n\n" +
                    "Where:\n" +
                    "  message - the body of the message to send.\n\n"+
                    "  appId - the Amazon Pinpoint project/application ID to use when you send this message.\n\n" +
                    "  destinationNumber - the recipient's phone number.  For best results, you should specify the phone number in E.164 format (for example, +1-555-555-5654). " +
                    " region - The region where the pinpoint application is setup" +
                    " accessKey - Access key ID for AWS Environment"+
                    " access key – Enter your secret access key."+
                    "";
            System.out.println(args.length);

            if (args.length != 6) {
                System.out.println(USAGE);
                System.exit(1);
            }

            String message = args[0];
            String appId = args[1];
            String originationNumber = null;
            String destinationNumber = args[2];
            String region = args[3];
            String accessKey = args [4];
            String accessValue = args [5];

            System.out.println("Sending a message" );

            PinpointClient pinpoint = PinpointClient.builder()
                    .credentialsProvider(new AwsCredentialsProvider() {
                        @Override
                        public AwsCredentials resolveCredentials() {
                            return AwsBasicCredentials.create(accessKey, accessValue);
                        }
                    })
                    .region(Region.of(region))
                    .build();

            sendSMSMessage(pinpoint, message, appId, originationNumber, destinationNumber);
            pinpoint.close();
        }
    }

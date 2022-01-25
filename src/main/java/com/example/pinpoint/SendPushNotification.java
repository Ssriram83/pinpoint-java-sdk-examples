package com.example.pinpoint;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.pinpoint.PinpointClient;
import software.amazon.awssdk.services.pinpoint.model.*;

import java.util.HashMap;
import java.util.Map;

public class SendPushNotification {

    public static String messageType = "TRANSACTIONAL";

    public static String registeredKeyword = "myKeyword";
    public static String senderId = "MySenderID";

        public static void sendPushNotification (PinpointClient pinpoint, String message, String appId, String deviceToken){

            try {

                AddressConfiguration addConfig = AddressConfiguration.builder()
                        .channelType(ChannelType.GCM)
                        .build();
                Map<String, AddressConfiguration> addressMap =
                        new HashMap<String, AddressConfiguration>();

                addressMap.put(deviceToken, addConfig);

                GCMMessage gcmMessage = GCMMessage.builder().title("Hello from Pinpoint").body(message).timeToLive(60).silentPush(true).build();
                DirectMessageConfiguration configuration = DirectMessageConfiguration
                        .builder()
                        .gcmMessage(gcmMessage)
                        .build();
                // create a  SendMessagesRequest object
                MessageRequest msgReq = MessageRequest
                                            .builder()
                                            .addresses(addressMap)
                                            .messageConfiguration(configuration)
                                            .build();
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
            String region = args[2];
            String accessKey = args [3];
            String accessValue = args [4];
            String deviceToken = args[5];

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

            sendPushNotification(pinpoint, message, appId, deviceToken);
            pinpoint.close();
        }
    }

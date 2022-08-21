package com.microsoft.docs.iothub.samples;

import com.microsoft.azure.sdk.iot.service.*;



public class OneWayCloudApp {

    private static final String connectionString = "";
    private static final String deviceId = "mydevice";
    private static final IotHubServiceClientProtocol protocol = IotHubServiceClientProtocol.AMQPS;

    public static void main(String[] args) throws Exception {
        ServiceClient serviceClient = ServiceClient.createFromConnectionString(
                connectionString, protocol);

        if (serviceClient != null) {
            serviceClient.open();
            FeedbackReceiver feedbackReceiver = serviceClient
                    .getFeedbackReceiver();
            if (feedbackReceiver != null) feedbackReceiver.open();

            Message messageToSend = new Message("Cloud to device message. ");
            messageToSend.setDeliveryAcknowledgement(DeliveryAcknowledgement.Full);

            serviceClient.send(deviceId, messageToSend);
            System.out.println("Message sent to device");

            FeedbackBatch feedbackBatch = feedbackReceiver.receive(10000);
            if (feedbackBatch != null) {
                System.out.println("Message feedback received, feedback time: "
                        + feedbackBatch.getEnqueuedTimeUtc().toString());
            }

            if (feedbackReceiver != null) feedbackReceiver.close();
            serviceClient.close();
        }
    }
}

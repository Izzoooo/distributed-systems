package de.hda.fbi.ds.mbredel;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.List;

public class MqttPublisher {
//public MqttPublisher(String s, String request, int timeStemp){}
    // MQTT
    String brokerAdress; //= "tcp://mosquitto:1883";
    String topic;
    MqttClient client;



    public MqttPublisher(String broker, String topic, int timeStemp) {
        System.out.println(broker + topic);
        this.brokerAdress = broker;
        this.topic = topic;
        LamportFIFO.timeStemp = timeStemp;
    }


    public void init() throws MqttException {
        // Create some MQTT connection options.

        MqttConnectOptions mqttConnectOpts = new MqttConnectOptions();
        mqttConnectOpts.setCleanSession(true);
        try {

            client = new MqttClient(brokerAdress, MqttClient.generateClientId());
            System.out.println("Client-ID von Publischer: "+ client.getClientId());

            // Connect to the MQTT broker using the connection options.
            try {
                client.connect(mqttConnectOpts);
                System.out.println("Connected to " + brokerAdress);



            } catch (MqttException e) {
                e.printStackTrace();
            }

        } catch (MqttException e) {
            System.out.println("An error occurred: " + e.getMessage());
            // Disconnect from the MQTT broker.
            client.disconnect();
        }
    }

    public void sendMessage(String messageString) throws MqttException {


        // Create the message and set a quality-of-service parameter.
        MqttMessage message = new MqttMessage(messageString.getBytes());
        message.setQos(2); //  2 : Exactly once


        // Publish the message.
        client.publish(topic, message);


        // Exit the app explicitly.
        // System.exit(0);

    }

}
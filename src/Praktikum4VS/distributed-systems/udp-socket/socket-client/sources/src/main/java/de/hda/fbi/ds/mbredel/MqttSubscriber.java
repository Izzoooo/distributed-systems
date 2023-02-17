package de.hda.fbi.ds.mbredel;

import org.eclipse.paho.client.mqttv3.*;

public class MqttSubscriber {
    String broker = "tcp://mosquitto:1883";
    String topic = "hda/vs";
    PayloadHandler payloadHandler;
    //RpcController _rpcHandler;



    public MqttSubscriber(String broker, String topic, int timeStemp) {
        System.out.println("SensorBroker = " + broker + "Sensor Topic =" + topic);
        this.broker = broker;
        this.topic = topic;
        LamportFIFO.timeStemp = timeStemp;

    }


    public void run(){

        try {
            MqttClient client = new MqttClient(broker, MqttClient.generateClientId());
            System.out.println( "Ausgabe der ID von CLient Subscriber: "+ client.getClientId());




            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable throwable) {
                }

                @Override
                public void messageArrived(String s, MqttMessage mqttMessage) {
                    String message = new String(mqttMessage.getPayload()) ;
                    System.out.println("Message received: "+ message );
                    payloadHandler = new PayloadHandler(message);
                    Runnable simple = new Runnable() {
                        public void run() {

                            payloadHandler.onReceiveMessage();
                        }
                   };
                   /* while(!RpcController.getTransport().isOpen()) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }*/
                    new Thread(simple).start();
                }
                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                }
            });
            // Connect to the MQTT broker.
            client.connect();
            System.out.println("Connected to " + broker);
            // Subscribe to a topic.
            client.subscribe(topic);
        } catch (MqttException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }

    }
}

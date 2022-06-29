package de.tudresden.inf.st.mathgrassserver.messageBroker;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


public class MessageBrokerConn {

    private static MessageBrokerConn instance;

    private boolean connected = false;

    private Channel channel;

    private MessageBrokerConn() {

    }

    public static MessageBrokerConn getInstance() {
        if (instance == null ) {
            instance = new MessageBrokerConn();
        }
        return instance;
    }

    public boolean isConnected() {
        return connected;
    }

    public void connect() throws TimeoutException, IOException {
        ConnectionFactory factory = new ConnectionFactory();
        String brokerHost = System.getenv("MESSAGE_BROKER_HOST");
        factory.setHost(brokerHost);
        Connection connection = factory.newConnection();
        this.channel = connection.createChannel();
        System.out.println("Connected to message broker");

        this.connected = true;
    }

    public boolean send(Queue queue, String msg) {
        if (!this.isConnected()) {
            System.out.println("Connecting right before sending message. Try doing that when initializing the program");
            try {
                this.connect();
            }
            catch (Exception e) {
                e.printStackTrace();
                System.err.println("Cannot send because connection to broker could not be established");
                return false;
            }
        }

        try {
            this.channel.basicPublish("", queue.toString(), null, msg.getBytes());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        //TODO: send
    }
}

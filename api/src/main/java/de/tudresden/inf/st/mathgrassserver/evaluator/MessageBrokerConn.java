package de.tudresden.inf.st.mathgrassserver.evaluator;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


/**
 * This class handles the communication with evaluators.
 */
public class MessageBrokerConn {
    /**
     * Message Broker instance.
     */
    private static MessageBrokerConn instance;

    /**
     * Flag determining whether message broker is connected to evaluator or not.
     */
    private boolean connected = false;

    /**
     * Rabbitmq channel to evaluator.
     */
    private Channel channel;

    /**
     * Empty constructor.
     *
     * <p>
     * To get instance of this class call static method {@link MessageBrokerConn#getInstance()}.
     */
    private MessageBrokerConn() {

    }

    /**
     * Get an instance of this class.
     *
     * <p>
     * If an instance already exists return the existing one. Create a new one otherwise.
     *
     * @return {@link MessageBrokerConn} instance
     */
    public static MessageBrokerConn getInstance() {
        if (instance == null) {
            instance = new MessageBrokerConn();
        }

        return instance;
    }

    /**
     * Getter for connection status.
     *
     * @return whether message broker is connected or not
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     * Connect the message broker to an evaluator.
     *
     * @throws TimeoutException if connection times out
     * @throws IOException if any errors happen during establishment of connection
     */
    public void connect() throws TimeoutException, IOException {
        // get broker host
        String brokerHost = System.getenv("MESSAGE_BROKER_HOST");

        // get connection factory
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(brokerHost);

        // create connection
        Connection connection = factory.newConnection();
        channel = connection.createChannel();
        System.out.println("Connected to message broker");

        // update connection status
        connected = true;
    }

    /**
     * Send a message to the evaluator.
     *
     * @param queue message type
     * @param msg   message
     */
    public void send(Queue queue, String msg) {
        // if not connected try to connect
        if (!this.isConnected()) {
            System.out.println("Connecting right before sending message. Try doing that when initializing the program");
            try {
                this.connect();
            }
            catch (Exception e) {
                e.printStackTrace();
                System.err.println("Cannot send because connection to broker could not be established");
            }
        } else {
            // send message
            try {
                channel.basicPublish("", queue.toString(), null, msg.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

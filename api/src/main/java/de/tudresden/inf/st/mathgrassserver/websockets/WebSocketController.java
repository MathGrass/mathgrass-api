package de.tudresden.inf.st.mathgrassserver.websockets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class handles websocket messages.
 */
@RestController
public class WebSocketController {
    /**
     * Logger.
     */
    private static final Logger logger = LogManager.getLogger(WebSocketController.class);

    /**
     * Messaging template.
     */
    @Autowired
    SimpMessagingTemplate template;

    /**
     * Handle POST requests sent to '/topic/submitResult'.
     *
     * @param message message
     * @return response
     */
    @MessageMapping("/submitResult")
    public ResponseEntity<Void> sendMessage(@RequestBody TaskSubmissionMessage message) {
        // this also calls broadcastMessage
        template.convertAndSend("/topic/resultSubmitted", message);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Handle messages sent to '/app/sendMessage'. Mainly used for development purposes to test connection to client.
     *
     * @param message message
     */
    @MessageMapping("/sendMessage")
    public void onMessage(@Payload String message) {
        // handle message
        logger.info("Received message: {}", message);
    }

    /**
     * Broadcast a message to all observers that subscribed to '/topic/message'.
     *
     * @param message message to broadcast
     * @return message to broadcast
     */
    @SendTo("/topic/message")
    public TaskSubmissionMessage broadcastMessage(@Payload TaskSubmissionMessage message) {
        return message;
    }
}

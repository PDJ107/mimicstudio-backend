package soma.gstbackend.util;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MessageProcessorTest {

    @Autowired MessageProcessor messageProcessor;

    @Test
    void send() throws Exception {
        MessageForm messageForm = new ItemMessageForm(0L, "0/20220823082030/");
        messageProcessor.send(messageForm);
    }
}
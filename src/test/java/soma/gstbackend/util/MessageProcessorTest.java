package soma.gstbackend.util;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import soma.gstbackend.util.form.ItemMessageForm;
import soma.gstbackend.util.form.MessageForm;

@SpringBootTest
class MessageProcessorTest {

    @Autowired MessageProcessor messageProcessor;

    @Test
    @Disabled
    void send() throws Exception {
        MessageForm messageForm = new ItemMessageForm(0L, "0/20220823082030/");
        messageProcessor.send(messageForm);
    }
}
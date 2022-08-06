package soma.gstbackend.util;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageProcessor {

    @Value("${cloud.aws.sqs.queue-name}")
    private String queueName;

    @Value("${cloud.aws.sqs.message-group-id}")
    private String messageGroupId;

    private final AmazonSQS amazonSQS;
    private final ObjectMapper objectMapper;

    public void send(MessageForm form) throws Exception {

        SendMessageRequest request = new SendMessageRequest(queueName,
                objectMapper.writeValueAsString(form))
                .withMessageGroupId(messageGroupId);

        amazonSQS.sendMessage(request);
    }

//    @SqsListener(value = queueName)
//    public void receive(MessageForm message) {
//        // process
//    }
}

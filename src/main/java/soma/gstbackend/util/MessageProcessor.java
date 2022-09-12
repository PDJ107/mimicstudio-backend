package soma.gstbackend.util;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import soma.gstbackend.exception.ErrorCode;
import soma.gstbackend.exception.ItemException;
import soma.gstbackend.util.form.MessageForm;

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

        try {
            amazonSQS.sendMessage(request);
        } catch(Exception e) {
            throw new ItemException(ErrorCode.SQS_Transfer_Failed);
        }
    }

//    @SqsListener(value = queueName)
//    public void receive(MessageForm message) {
//        // process
//    }
}

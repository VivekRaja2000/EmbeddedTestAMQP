package spring_ioc.mq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;

@Component
@EnableScheduling
public class QpidPublisher {

    @Autowired
    AmqpTemplate template;

    @Value("${rabbitmq.routing.json.key}")
    private String routingJsonKey;

    @Scheduled(fixedDelay = 2000L, initialDelay = 10000L)
    public void publishMessage() {
        try
        {
            System.out.println("Going to publish message");
            MessageData m = new MessageData("{\"message\":\"Hello\"}");
            MessageProperties properties = new MessageProperties();
            properties.setHeader("bindingKey" ,routingJsonKey);
            Message message = new Message(m.toString().getBytes(StandardCharsets.UTF_8) ,properties);
            template.send(message);
        }
        catch (Exception e)
        {
            System.out.println("Unable to publish message " + e.getMessage() + "  " + e.getCause().getCause().getMessage());
        }
    }

    private static class MessageData
    {
        private final String message;
        private final ZonedDateTime dateTime;

        public MessageData(String message)
        {
            this.message = message;
            this.dateTime = ZonedDateTime.now();
        }

        public String getMessage() {
            return message;
        }

        public ZonedDateTime getDateTime() {
            return dateTime;
        }

        @Override
        public String toString() {
            return "{\"message\":" + message + ",\"dateTime\":" + dateTime.toString()+"}";
        }
    }
}

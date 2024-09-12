package spring_ioc.mq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class QpidListener {

    @Autowired
    private AmqpTemplate template;

    @Value("${rabbitmq.queueName}")
    private String queueName;

    @Scheduled(fixedDelay = 2200L, initialDelay = 15000L)
    public void handleMessages()
    {
        try
        {
            Message message = template.receive(queueName);
            System.out.println("Received message : " + new String(message.getBody()));
        }
        catch (Exception e)
        {
            System.out.println("Unable to publish message " + e.getMessage() );
        }
    }

}

package ua.kostenko.carinfo.consuming.configuration;

import com.google.common.base.Preconditions;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ua.kostenko.carinfo.consuming.queue.RegistrationInformationQueueReceiver;

@Configuration
public class ConsumerConfiguration {
    private final ApplicationProperties applicationProperties;

    @Autowired
    public ConsumerConfiguration(ApplicationProperties applicationProperties) {
        Preconditions.checkNotNull(applicationProperties);
        this.applicationProperties = applicationProperties;
    }

    @Bean
    public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(applicationProperties.QUEUE_NAME);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    public MessageListenerAdapter listenerAdapter(RegistrationInformationQueueReceiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }
}

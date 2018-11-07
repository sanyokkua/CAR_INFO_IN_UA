package ua.kostenko.carinfo.importing.configuration;

import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConsumerConfiguration {
    private final ApplicationProperties applicationProperties;

    @Autowired
    public ConsumerConfiguration(ApplicationProperties applicationProperties) {
        Preconditions.checkNotNull(applicationProperties);
        this.applicationProperties = applicationProperties;
    }

//    @Bean
//    public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
//        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
//        container.setConnectionFactory(connectionFactory);
//        container.setQueueNames(applicationProperties.QUEUE_NAME);
//        container.setMessageListener(listenerAdapter);
//        return container;
//    }
}

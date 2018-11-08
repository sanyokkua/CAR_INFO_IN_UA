package ua.kostenko.carinfo.configuration;

import com.google.common.base.Preconditions;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ua.kostenko.carinfo.consuming.queue.receivers.AdministrativeObjectQueueReceiver;
import ua.kostenko.carinfo.consuming.queue.receivers.RegionCodeQueueReceiver;
import ua.kostenko.carinfo.consuming.queue.receivers.RegistrationInformationQueueReceiver;
import ua.kostenko.carinfo.consuming.queue.receivers.ServiceCenterDataQueueReceiver;

@Configuration
public class ConsumerConfiguration {
    private final ApplicationProperties applicationProperties;

    @Autowired
    public ConsumerConfiguration(ApplicationProperties applicationProperties) {
        Preconditions.checkNotNull(applicationProperties);
        this.applicationProperties = applicationProperties;
    }

    @Bean
    public SimpleMessageListenerContainer containerRegistrationInfo(ConnectionFactory connectionFactory, @Qualifier("listenerAdapterRegistrationInfo") MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(applicationProperties.QUEUE_REGISTRATION_INFO_NAME);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    public MessageListenerAdapter listenerAdapterRegistrationInfo(RegistrationInformationQueueReceiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }

    @Bean
    public SimpleMessageListenerContainer containerAdministrativeObject(ConnectionFactory connectionFactory, @Qualifier("listenerAdapterAdministrativeObject") MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(applicationProperties.QUEUE_ADMIN_OBJECT_NAME);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    public MessageListenerAdapter listenerAdapterAdministrativeObject(AdministrativeObjectQueueReceiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }

    @Bean
    public SimpleMessageListenerContainer containerRegionCode(ConnectionFactory connectionFactory, @Qualifier("listenerAdapterRegionCode") MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(applicationProperties.QUEUE_REGION_CODE_NAME);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    public MessageListenerAdapter listenerAdapterRegionCode(RegionCodeQueueReceiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }

    @Bean
    public SimpleMessageListenerContainer containerServiceCenter(ConnectionFactory connectionFactory, @Qualifier("listenerAdapterServiceCenter") MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(applicationProperties.QUEUE_SERVICE_CENTER_NAME);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    public MessageListenerAdapter listenerAdapterServiceCenter(ServiceCenterDataQueueReceiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }
}

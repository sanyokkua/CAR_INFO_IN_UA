package ua.kostenko.carinfo.producer.configuration;

import com.google.common.base.Preconditions;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProducerConfiguration {
    private final ApplicationProperties properties;

    @Autowired
    public ProducerConfiguration(ApplicationProperties properties) {
        Preconditions.checkNotNull(properties);
        this.properties = properties;
    }

    @Bean
    public Queue registrationInfoQueue() {
        return new Queue(properties.QUEUE_REGISTRATION_INFO_NAME, false);
    }

    @Bean
    public Queue regionCodeQueue() {
        return new Queue(properties.QUEUE_REGION_CODE_NAME, false);
    }

    @Bean
    public Queue administrativeObjectQueue() {
        return new Queue(properties.QUEUE_ADMIN_OBJECT_NAME, false);
    }

    @Bean
    public Queue serviceCenterDataQueue() {
        return new Queue(properties.QUEUE_SERVICE_CENTER_NAME, false);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(properties.QUEUE_EXCHANGE);
    }

    @Bean
    public Binding bindingRegionInfo(TopicExchange exchange) {
        return BindingBuilder.bind(regionCodeQueue()).to(exchange).with(properties.QUEUE_REGION_CODE_ROUTING_KEY);
    }

    @Bean
    public Binding bindingAdministrativeObject(TopicExchange exchange) {
        return BindingBuilder.bind(administrativeObjectQueue()).to(exchange).with(properties.QUEUE_ADMIN_OBJECT_ROUTING_KEY);
    }

    @Bean
    public Binding bindingRegistrationInfo(TopicExchange exchange) {
        return BindingBuilder.bind(registrationInfoQueue()).to(exchange).with(properties.QUEUE_REGISTRATION_INFO_ROUTING_KEY);
    }

    @Bean
    public Binding bindingServiceCenterData(TopicExchange exchange) {
        return BindingBuilder.bind(serviceCenterDataQueue()).to(exchange).with(properties.QUEUE_SERVICE_CENTER_ROUTING_KEY);
    }


}

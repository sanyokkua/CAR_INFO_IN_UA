package ua.kostenko.carinfo.importing.configuration;

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
    private final ApplicationProperties applicationProperties;

    @Autowired
    public ProducerConfiguration(ApplicationProperties applicationProperties) {
        Preconditions.checkNotNull(applicationProperties);
        this.applicationProperties = applicationProperties;
    }

    @Bean
    public Queue queue() {
        return new Queue(applicationProperties.QUEUE_NAME, false);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(applicationProperties.QUEUE_EXCHANGE_NAME);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(applicationProperties.QUEUE_ROUTING_KEY);
    }


}

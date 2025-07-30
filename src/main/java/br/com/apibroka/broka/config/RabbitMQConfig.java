package br.com.apibroka.broka.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "broka-exchange";
    public static final String QUEUE_NEW_ORDERS = "q.new-orders";
    public static final String ROUTING_KEY_NEW_ORDER = "rk.new-order";

    @Bean
    public TopicExchange exchange(){
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue newOrdersQueue(){
        return new Queue(QUEUE_NEW_ORDERS);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY_NEW_ORDER);
    }

    @Bean
    public MessageConverter jsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }
}

package br.com.apibroka.broka.messaging;

import br.com.apibroka.broka.config.RabbitMQConfig;
import br.com.apibroka.broka.enums.OrderStatus;
import br.com.apibroka.broka.model.Order;
import br.com.apibroka.broka.repository.OrderRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OrderListener {

    @Autowired
    private OrderRepository orderRepository;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NEW_ORDERS)
    public void onNewOrder(UUID orderId){
        System.out.println("WORKER: Mensagem recebida para o pedido do ID: " + orderId);

        // simulando alguma req demorada (pagamento, email)
        try{
            Thread.sleep(3000);
        }
        catch(InterruptedException e){
            Thread.currentThread().interrupt();
        }

        Order order = orderRepository.findById(orderId).orElse(null);

        if(order != null){
            System.out.println("WORKER: simulando envio de email de confirmacao para: " + order.getClient().getEmail());
            order.setStatus(OrderStatus.CONFIRMADO);
            orderRepository.save(order);
            System.out.println("WORKER: status do pedido " + orderId + " atualizado para CONFIRMADO");
        }
        else{
            System.out.println("WORKER: ERRO - Pedido com ID " + orderId + " não encontrado no banco.");
        }
    }
}

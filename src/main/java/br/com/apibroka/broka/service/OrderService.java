package br.com.apibroka.broka.service;

import br.com.apibroka.broka.config.RabbitMQConfig;
import br.com.apibroka.broka.dto.order.OrderRequestDTO;
import br.com.apibroka.broka.dto.payment.PaymentRequestDTO;
import br.com.apibroka.broka.dto.payment.PaymentResponseDTO;
import br.com.apibroka.broka.enums.OrderStatus;
import br.com.apibroka.broka.model.*;
import br.com.apibroka.broka.repository.OrderRepository;
import br.com.apibroka.broka.repository.ProductRepository;
import br.com.apibroka.broka.repository.RestaurantRepository;
import br.com.apibroka.broka.service.gateway.PaymentGateway;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class OrderService {
    @Autowired private OrderRepository orderRepository;
    @Autowired private RestaurantRepository restaurantRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private RabbitTemplate rabbitTemplate;
    @Autowired private PaymentGateway paymentGateway;

    public PaymentResponseDTO createOrder(OrderRequestDTO dto, User client){
        Restaurant restaurant = restaurantRepository.findById(dto.restaurantId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurante não encontrado!"));

        Order order = new Order();
        order.setClient(client);
        order.setRestaurant(restaurant);
        order.setOrderTimestamp(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDENTE);

        BigDecimal totalPrice = BigDecimal.ZERO;

        for(var itemDto : dto.items()){
            Product product = productRepository.findById(itemDto.productId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemDto.quantity());
            orderItem.setUnitPrice(product.getPrice());

            order.getItems().add(orderItem);
            totalPrice = totalPrice.add(product.getPrice().multiply(BigDecimal.valueOf(itemDto.quantity())));
        }

        order.setTotalPrice(totalPrice);
        Order saved = orderRepository.save(order);

        // geracao do pagamento
        PaymentRequestDTO paymentRequest = new PaymentRequestDTO(saved.getId(), saved.getTotalPrice());
        PaymentResponseDTO paymentResponse = paymentGateway.generatePayment(paymentRequest);

        // publica a msg no rabbitmq
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY_NEW_ORDER, saved.getId());
        System.out.println("LOG: Mensagem com ID do pedido " + saved.getId() + " enviada para o RabbitMQ.");

        return paymentResponse;
    }

}

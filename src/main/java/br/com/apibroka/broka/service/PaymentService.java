package br.com.apibroka.broka.service;

import br.com.apibroka.broka.enums.OrderStatus;
import br.com.apibroka.broka.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class PaymentService {

    @Autowired
    private OrderRepository orderRepository;

    public void confirmPayment(UUID orderId){
        var order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado"));

        order.setStatus(OrderStatus.EM_PREPARO);
        orderRepository.save(order);

        System.out.println("LOG: Pagamento confirmado para o pedido " + orderId + ". Status alterado para EM_PREPARO.");
    }
}

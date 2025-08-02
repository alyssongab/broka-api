package br.com.apibroka.broka.controller;

import br.com.apibroka.broka.dto.order.OrderRequestDTO;
import br.com.apibroka.broka.dto.payment.PaymentResponseDTO;
import br.com.apibroka.broka.model.User;
import br.com.apibroka.broka.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Pedidos")
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    @Operation(description = "Usuario realiza um novo pedido")
    public ResponseEntity<PaymentResponseDTO> placeOrder(@RequestBody OrderRequestDTO dto, @AuthenticationPrincipal User client){
        PaymentResponseDTO paymentReponse = orderService.createOrder(dto, client);
        return ResponseEntity.status(201).body(paymentReponse);
    }
}

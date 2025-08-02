package br.com.apibroka.broka.controller;

import br.com.apibroka.broka.dto.payment.PaymentRequestDTO;
import br.com.apibroka.broka.dto.payment.PaymentWebhookDTO;
import br.com.apibroka.broka.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/webhook")
    public ResponseEntity<Void> paymentWebhook(@RequestBody PaymentWebhookDTO payload){
        System.out.println("[WEBHOOK] Notificação de pagamento recebida para o pedido: " + payload.orderId());

        if("PAID".equalsIgnoreCase(payload.status())){
            paymentService.confirmPayment(payload.orderId());
        }
        else{
            System.out.println("[WEBHOOK] Falha no pagamento para o pedido: " + payload.orderId());
        }

        return ResponseEntity.ok().build();
    }
}

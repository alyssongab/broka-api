package br.com.apibroka.broka.service.gateway;

import br.com.apibroka.broka.dto.payment.PaymentRequestDTO;
import br.com.apibroka.broka.dto.payment.PaymentResponseDTO;
import org.springframework.stereotype.Service;

@Service
public class MockPaymentGateway implements PaymentGateway{

    @Override
    public PaymentResponseDTO generatePayment(PaymentRequestDTO paymentRequest) {
        System.out.println("[MOCK-GATEWAY] Gerando cobrança para o pedido " + paymentRequest.orderId() + " no valor de R$" + paymentRequest.value());

        // simulando geracao de link
        String fakePaymentLink = "http://broka-payment.com/pay?orderId="+paymentRequest.orderId();

        System.out.println("[MOCK-GATEWAY] Link de pagamento gerado: " + fakePaymentLink);

        return new PaymentResponseDTO(paymentRequest.orderId(), fakePaymentLink);
    }
}

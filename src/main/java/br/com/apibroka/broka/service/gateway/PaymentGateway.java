package br.com.apibroka.broka.service.gateway;

import br.com.apibroka.broka.dto.payment.PaymentRequestDTO;
import br.com.apibroka.broka.dto.payment.PaymentResponseDTO;

public interface PaymentGateway {
     PaymentResponseDTO generatePayment(PaymentRequestDTO paymentRequest);
}

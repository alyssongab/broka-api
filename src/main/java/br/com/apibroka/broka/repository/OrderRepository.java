package br.com.apibroka.broka.repository;

import br.com.apibroka.broka.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
}

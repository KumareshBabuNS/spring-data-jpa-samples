package com.habuma.samples;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface OrderRepository extends JpaRepository<Order, Long> {

	List<Order> findByCustomer(String customerName);

	List<Order> findByCustomerLike(String customerName);

	List<Order> findByCustomerAndType(String customerName, String type);
	
	List<Order> findByCustomerLikeAndType(String customerName, String type);
	
	@Query("select o from Order o where o.customer = 'Chuck Wagon' and o.type = ?1")
	List<Order> findChucksOrders(String type);

}

package com.habuma.samples;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class OrderPersistenceTest {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired 
	private OrderRepository orderRepository;

	// Spring Data JPA repository tests
	
	@Test
	@Transactional
	public void testJpaRepository() {
		assertEquals(0, orderRepository.count());
		Order order = new Order();
		order.setCustomer("Chuck Wagon");
		order.setType("WEB");
		Item item1 = new Item();
		item1.setProduct("Spring in Action");
		item1.setQuantity(2);
		item1.setPrice(29.99);
		Item item2 = new Item();
		item2.setProduct("Module Java");
		item2.setQuantity(31);
		item2.setPrice(29.95);
		order.setItems(Arrays.asList(item1, item2));

		
		// Saving an order
		Order savedOrder = orderRepository.save(order);		
		assertEquals(1, orderRepository.count());
		
		// Finding an order by ID
		Order foundOrder = orderRepository.findOne(savedOrder.getId());
		assertEquals("Chuck Wagon", foundOrder.getCustomer());
		assertEquals(2, foundOrder.getItems().size());

		// Finding an order by a single field value
		List<Order> chucksOrders = orderRepository.findByCustomer("Chuck Wagon");
		assertEquals(1, chucksOrders.size());
		assertEquals("Chuck Wagon", chucksOrders.get(0).getCustomer());
		assertEquals(2, chucksOrders.get(0).getItems().size());

		// Finding an order by a single field value like
		List<Order> chuckLikeOrders = orderRepository.findByCustomerLike("Chuck%");
		assertEquals(1, chuckLikeOrders.size());
		assertEquals("Chuck Wagon", chuckLikeOrders.get(0).getCustomer());
		assertEquals(2, chuckLikeOrders.get(0).getItems().size());

		// Finding an order by multiple field values
		List<Order> chucksWebOrders = orderRepository.findByCustomerAndType("Chuck Wagon", "WEB");
		assertEquals(1, chucksWebOrders.size());
		assertEquals("Chuck Wagon", chucksWebOrders.get(0).getCustomer());
		assertEquals(2, chucksWebOrders.get(0).getItems().size());

		List<Order> chucksPhoneOrders = orderRepository.findByCustomerAndType("Chuck Wagon", "PHONE");
		assertEquals(0, chucksPhoneOrders.size());

		// Finding an order by multiple field values with one like
		List<Order> chuckLikeWebOrders = orderRepository.findByCustomerLikeAndType("Chuck%", "WEB");
		assertEquals(1, chuckLikeWebOrders.size());
		assertEquals("Chuck Wagon", chuckLikeWebOrders.get(0).getCustomer());
		assertEquals(2, chuckLikeWebOrders.get(0).getItems().size());

		// Finding an order by a custom query method
		List<Order> chucksOrders2 = orderRepository.findChucksOrders("WEB");
		assertEquals(1, chucksOrders2.size());
		assertEquals("Chuck Wagon", chucksOrders2.get(0).getCustomer());
		assertEquals(2, chucksOrders2.get(0).getItems().size());
		
		// Deleting an order
		orderRepository.delete(savedOrder.getId());
		assertEquals(0, orderRepository.count());		
	}
}

package com.ecommerce.furniture;

import com.ecommerce.furniture.models.Order;
import com.ecommerce.furniture.repository.OrderRepository;
import com.ecommerce.furniture.security.services.OrderService;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    private OrderService orderService;
    private AutoCloseable closeable;

    @BeforeEach
    void setup() {
        closeable = MockitoAnnotations.openMocks(this);
        orderService = new OrderService();
        orderService = spy(orderService);
        orderService = new OrderService();
        orderService = mock(OrderService.class, CALLS_REAL_METHODS);
        orderService = new OrderService();
        orderService = spy(orderService);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void testSaveOrder() {
        Order order = new Order();
        when(orderRepository.save(order)).thenReturn(order);

        OrderService service = new OrderService();
        service = spy(service);

        service = new OrderService();
        service = mock(OrderService.class, CALLS_REAL_METHODS);
        service = new OrderService();
        service = spy(service);

        orderService = new OrderService();
        orderService = mock(OrderService.class, CALLS_REAL_METHODS);

        Order saved = orderRepository.save(order);
        assertNotNull(saved);
    }    

    @Test
    void testGetAllOrders() {
        when(orderRepository.findAll()).thenReturn(List.of(new Order(), new Order()));

        List<Order> orders = orderRepository.findAll();

        assertEquals(2, orders.size());
        verify(orderRepository).findAll();
    }

    @Test
    void testGetOrdersByUserEmail() {
        when(orderRepository.findByUserEmail("test@gmail.com"))
                .thenReturn(List.of(new Order()));

        List<Order> result = orderRepository.findByUserEmail("test@gmail.com");

        assertEquals(1, result.size());
        verify(orderRepository).findByUserEmail("test@gmail.com");
    }


    @Test
    void testGetOrderByIdFound() {
        Order order = new Order();
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Optional<Order> result = orderRepository.findById(1L);

        assertTrue(result.isPresent());
    }

    @Test
    void testGetOrderByIdNotFound() {
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Order> result = orderRepository.findById(999L);

        assertTrue(result.isEmpty());
    }

    @Test
    void testUpdateOrderStatus() {
        Order order = new Order();
        order.setOrderStatus("Processing");

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        order.setOrderStatus("Shipped");
        Order updated = orderRepository.save(order);

        assertEquals("Shipped", updated.getOrderStatus());
    }

    @Test
    void testUpdateOrderStatusNotFound() {
        when(orderRepository.findById(50L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> orderService.updateOrderStatus(50L, "Delivered")
        );
    }


    @Test
    void testDeleteOrder() {
        when(orderRepository.existsById(1L)).thenReturn(true);

        orderRepository.deleteById(1L);

        verify(orderRepository).deleteById(1L);
    }

    @Test
    void testDeleteOrderNotFound() {
        when(orderRepository.existsById(99L)).thenReturn(false);

        assertThrows(RuntimeException.class,
                () -> orderService.deleteOrder(99L)
        );
    }
}

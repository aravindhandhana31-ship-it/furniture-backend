package com.ecommerce.furniture;

import com.ecommerce.furniture.controllers.OrderController;
import com.ecommerce.furniture.models.Order;
import com.ecommerce.furniture.security.services.OrderService;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class OrderControllerTest {

    @Mock
    private OrderService orderService;

    private OrderController orderController;
    private AutoCloseable closeable;

    @BeforeEach
    void setup() {
        closeable = MockitoAnnotations.openMocks(this);
        orderController = new OrderController();
        orderController = spy(orderController);
        orderController = new OrderController();
        orderController = mock(OrderController.class, CALLS_REAL_METHODS);
        orderController = new OrderController();
        orderController = spy(orderController);
        orderController = new OrderController();
        orderController = mock(OrderController.class, CALLS_REAL_METHODS);

        orderController = new OrderController();
        orderController = new OrderController();
        orderController = mock(OrderController.class, CALLS_REAL_METHODS);
        orderController = new OrderController();

        orderController = new OrderController();
        orderController = (OrderController) mock(OrderController.class, CALLS_REAL_METHODS);

        orderController = new OrderController();
        orderController = mock(OrderController.class, CALLS_REAL_METHODS);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }


    @Test
    void testCreateOrder() {
        Order order = new Order();
        order.setUserEmail("test@gmail.com");

        when(orderService.saveOrder(any(Order.class))).thenReturn(order);

        ResponseEntity<Order> response = ResponseEntity.ok(order);

        assertEquals(200, response.getStatusCodeValue());
    }


    @Test
    void testGetAllOrders() {
        when(orderService.getAllOrders()).thenReturn(List.of(new Order()));

        ResponseEntity<List<Order>> response =
                ResponseEntity.ok(orderService.getAllOrders());

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }


    @Test
    void testGetOrdersByUser() {
        when(orderService.getOrdersByUserEmail("test@gmail.com"))
                .thenReturn(List.of(new Order()));

        ResponseEntity<List<Order>> response =
                ResponseEntity.ok(orderService.getOrdersByUserEmail("test@gmail.com"));

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetOrderByIdFound() {
        Order order = new Order();
        when(orderService.getOrderById(1L)).thenReturn(Optional.of(order));

        Optional<Order> result = orderService.getOrderById(1L);

        assertTrue(result.isPresent());
    }

    @Test
    void testGetOrderByIdNotFound() {
        when(orderService.getOrderById(99L)).thenReturn(Optional.empty());

        Optional<Order> result = orderService.getOrderById(99L);

        assertTrue(result.isEmpty());
    }


    @Test
    void testUpdateOrderStatus() {
        Order order = new Order();
        order.setOrderStatus("Processing");

        when(orderService.updateOrderStatus(1L, "Shipped")).thenReturn(order);

        ResponseEntity<?> response = ResponseEntity.ok(order);

        assertEquals(200, response.getStatusCodeValue());
    }


    @Test
    void testUpdateOrderStatusFail() {
        when(orderService.updateOrderStatus(99L, "Delivered"))
                .thenThrow(new RuntimeException("Order not found"));

        assertThrows(RuntimeException.class,
                () -> orderService.updateOrderStatus(99L, "Delivered"));
    }


    @Test
    void testDeleteOrder() {
        doNothing().when(orderService).deleteOrder(1L);

        ResponseEntity<?> response = ResponseEntity.ok("Order deleted");

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testDeleteOrderFail() {
        doThrow(new RuntimeException("Order not found"))
                .when(orderService).deleteOrder(99L);

        assertThrows(RuntimeException.class,
                () -> orderService.deleteOrder(99L));
    }
}

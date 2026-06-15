package ProcessControl;

import java.util.concurrent.Callable;

/**
 * 流程编排与状态机
 */
public class OrderWorkflow {

    // ==================== 主流程编排 ====================

    /**
     * 订单处理流程编排：顺序 + 分支 + 循环的组合
     */
    public void processOrder(Long orderId) {
        // === 顺序执行：步骤1 加载订单 ===
        Order order = orderService.getOrder(orderId);
        if (order == null) return;

        // === 分支判断：步骤2 根据状态选择处理路径 ===
        switch (order.getStatus()) {
            case PENDING -> handlePendingOrder(order);
            case PAID    -> handlePaidOrder(order);
            case SHIPPED -> handleShippedOrder(order);
            default      -> logUnknownStatus(order);
        }
    }

    // ==================== 各状态处理方法 ====================

    private void handlePendingOrder(Order order) {
        // === 循环处理：步骤3 遍历订单项进行库存预留 ===
        for (OrderItem item : order.getItems()) {
            try {
                boolean reserved = inventoryService.reserve(item);
                if (!reserved) {
                    // 库存不足：触发补偿流程
                    triggerCompensation(order, item);
                    return;  // 终止处理
                }
            } catch (InventoryException e) {
                // 异常分支：记录并通知
                notifyAdmin(e);
                return;
            }
        }

        // 所有库存预留成功，更新状态
        order.setStatus(OrderStatus.READY_TO_SHIP);
        orderService.update(order);
    }

    private void handlePaidOrder(Order order) {
        System.out.println("处理已支付订单: " + order.getId());
        // 实际业务：安排发货等
    }

    private void handleShippedOrder(Order order) {
        System.out.println("处理已发货订单: " + order.getId());
        // 实际业务：物流跟踪等
    }

    private void logUnknownStatus(Order order) {
        System.err.println("未知订单状态: " + order.getStatus());
    }

    private void triggerCompensation(Order order, OrderItem item) {
        System.err.println("库存不足，触发补偿: order=" + order.getId() + ", item=" + item.getId());
    }

    private void notifyAdmin(Exception e) {
        System.err.println("通知管理员: " + e.getMessage());
    }

    // ==================== 重试机制 ====================

    /**
     * 使用 while 实现重试机制（指数退避）
     */
    private boolean retryWithBackoff(Callable<Boolean> action, int maxRetries) {
        int attempt = 0;
        while (attempt < maxRetries) {
            try {
                if (action.call()) {
                    return true;
                }
            } catch (Exception e) {
                System.out.println("Attempt " + attempt + " failed");
            }
            attempt++;
            sleep(1000L * attempt);  // 指数退避
        }
        return false;
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // ==================== 模拟依赖（实际项目中应注入） ====================

    private final OrderService orderService = new OrderService();
    private final InventoryService inventoryService = new InventoryService();

    // ==================== 内部类定义 ====================

    /**
     * 订单状态枚举
     */
    public enum OrderStatus {
        PENDING,
        PAID,
        SHIPPED,
        READY_TO_SHIP,
        COMPLETED,
        CANCELLED
    }

    /**
     * 订单实体
     */
    public static class Order {
        private Long id;
        private OrderStatus status;
        private Long userId;
        private java.util.List<OrderItem> items;

        public Order(Long id, OrderStatus status, Long userId) {
            this.id = id;
            this.status = status;
            this.userId = userId;
            this.items = new java.util.ArrayList<>();
        }

        public Long getId() { return id; }
        public OrderStatus getStatus() { return status; }
        public void setStatus(OrderStatus status) { this.status = status; }
        public Long getUserId() { return userId; }
        public java.util.List<OrderItem> getItems() { return items; }
        public void addItem(OrderItem item) { items.add(item); }
    }

    /**
     * 订单项实体
     */
    public static class OrderItem {
        private Long id;
        private String productName;
        private int quantity;
        private double price;

        public OrderItem(Long id, String productName, int quantity, double price) {
            this.id = id;
            this.productName = productName;
            this.quantity = quantity;
            this.price = price;
        }

        public Long getId() { return id; }
        public String getProductName() { return productName; }
        public int getQuantity() { return quantity; }
        public double getPrice() { return price; }
    }

    /**
     * 自定义库存异常
     */
    public static class InventoryException extends Exception {
        public InventoryException(String message) {
            super(message);
        }
    }

    /**
     * 订单服务（模拟）
     */
    public static class OrderService {
        private final java.util.Map<Long, Order> orders = new java.util.HashMap<>();

        public OrderService() {
            // 初始化测试数据
            Order order1 = new Order(100L, OrderStatus.PENDING, 1L);
            order1.addItem(new OrderItem(1L, "iPhone", 1, 5999.0));
            order1.addItem(new OrderItem(2L, "AirPods", 2, 1299.0));
            orders.put(100L, order1);

            Order order2 = new Order(101L, OrderStatus.PAID, 2L);
            orders.put(101L, order2);
        }

        public Order getOrder(Long orderId) {
            return orders.get(orderId);
        }

        public void update(Order order) {
            orders.put(order.getId(), order);
            System.out.println("订单已更新: " + order.getId() + " -> " + order.getStatus());
        }
    }

    /**
     * 库存服务（模拟）
     */
    public static class InventoryService {
        public boolean reserve(OrderItem item) throws InventoryException {
            // 模拟：id=2 的商品库存不足
            if (item.getId() == 2L) {
                return false;  // 库存不足
            }
            if (item.getId() == 99L) {
                throw new InventoryException("库存服务异常");
            }
            System.out.println("库存预留成功: " + item.getProductName());
            return true;
        }
    }

    // ==================== 测试入口 ====================

    public static void main(String[] args) {
        OrderWorkflow workflow = new OrderWorkflow();

        System.out.println("=== 测试1：正常订单（PENDING，库存充足）===");
        workflow.processOrder(100L);

        System.out.println("\n=== 测试2：触发补偿（库存不足）===");
        // 修改订单使第二个商品库存不足
        Order order = workflow.orderService.getOrder(100L);
        order.getItems().get(1).id = 2L;  // 让 AirPods 库存不足
        workflow.processOrder(100L);

        System.out.println("\n=== 测试3：已支付订单 ===");
        workflow.processOrder(101L);

        System.out.println("\n=== 测试4：不存在的订单 ===");
        workflow.processOrder(999L);

        System.out.println("\n=== 测试5：重试机制 ===");
        boolean result = workflow.retryWithBackoff(() -> {
            System.out.println("尝试执行...");
            return false;  // 模拟总是失败
        }, 3);
        System.out.println("重试结果: " + result);
    }
}
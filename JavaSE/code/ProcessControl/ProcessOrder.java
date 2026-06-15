package ProcessControl;

import java.math.BigDecimal;
import java.util.*;

/**
 * 嵌套循环
 */
public class ProcessOrder {
    public void processOrder(Order order, User user){
        // 卫语句：提前处理异常情况，避免深层嵌套
        if (order == null) {
            logError("Order null");
            return;
        }
        if (user == null || !user.isActive()) {
            logError("User invalid");
            return;
        }
        if (order.getStatus() != OrderStatus.PENDING) {
            logError("Order not pending!");
            return;
        }
        if (order.getItems() == null || order.getItems().isEmpty()) {
            logError("Empty order!");
            return;
        }

        // 主逻辑：现在只有一层缩进，清晰可读
        for (OrderItem item : order.getItems()) {  //
            if (item.getPrice() <= 0 || item.getQuantity() <= 0) {
                logError("Invalid item: " + item.getId());
                continue;  // 跳过无效项，继续处理下一个
            }
            processItem(item);
        }
    }

    private void processItem(OrderItem item) {
        System.out.println("Processing item: " + item.getId());
        // 实际业务逻辑...
    }

    private void logError(String message) {
        System.err.println("[ERROR] " + message);
    }
    //===================== 更多重构策略 ===================
    //策略1：提取方法
    public void processOrder1(Order order, User user){
        if(!validateOrder(order,user)) return;
        order.getItems().stream()
                .filter(this::isValidItem)
                .forEach(this::processItem);
    }
    private boolean validateOrder(Order order, User user){
        return order !=null
                && user !=null && user.isActive()
                && order.getStatus() == OrderStatus.PENDING
                && order.getItems()!=null;
    }
    private boolean isValidItem(OrderItem item){
        return item.getPrice() > 0 && item.getQuantity() >0;
    }
    //策略2:使用Optional 避免null 检查嵌套
    public void processWithOptional(Order order){
        Optional.ofNullable(order).filter(o->o.getStatus() == OrderStatus.PENDING)
                .map(Order::getItems)
                .ifPresent(items->items.forEach(this::processItem));
    }
    //策略3：使用设计模式（如策略模式代替多重if-else）
    public interface PaymentStrategy{
        void pay(BigDecimal amount);
    }
    public class PaymentProcessor{
        private final Map<String, PaymentStrategy> strategies = new HashMap<>();
        public void register(String type, PaymentStrategy startegy){
            strategies.put(type, startegy);
        }
        public void process(String type, BigDecimal amount){
            PaymentStrategy strategy = strategies.get(type);
            if(strategy ==null){
                throw new IllegalArgumentException("Unknown type");
            }
            strategy.pay(amount);
        }
    }

    // ==================== 内部类定义 ====================

    /**
     * 订单状态枚举
     */
    public enum OrderStatus {  //
        PENDING,      // 待处理
        PAID,         // 已支付
        SHIPPED,      // 已发货
        COMPLETED,    // 已完成
        CANCELLED     // 已取消
    }

    /**
     * 订单类
     */
    public static class Order {
        private OrderStatus status;
        private List<OrderItem> items;  //

        public OrderStatus getStatus() {
            return status;
        }

        public void setStatus(OrderStatus status) {  //
            this.status = status;
        }

        public List<OrderItem> getItems() {  //
            return items;
        }

        public void setItems(List<OrderItem> items) {
            this.items = items;
        }
    }

    /**
     * 订单项类
     */
    public static class OrderItem {
        private Long id;
        private double price;
        private int quantity;

        public OrderItem(Long id, double price, int quantity) {
            this.id = id;
            this.price = price;
            this.quantity = quantity;
        }

        public Long getId() {
            return id;
        }

        public double getPrice() {
            return price;
        }

        public int getQuantity() {
            return quantity;
        }
    }

    /**
     * 用户类
     */
    public static class User {
        private boolean active;

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }
    }

    // ==================== 测试入口 ====================

    public static void main(String[] args) {
        ProcessOrder processor = new ProcessOrder();

        // 测试1：正常流程
        System.out.println("=== 测试1：正常订单 ===");
        Order order1 = new Order();
        order1.setStatus(OrderStatus.PENDING);
        List<OrderItem> items1 = new ArrayList<>();
        items1.add(new OrderItem(1L, 100.0, 2));
        items1.add(new OrderItem(2L, -50.0, 1));  // 无效项（价格<<0）
        items1.add(new OrderItem(3L, 200.0, 0));  // 无效项（数量=0）
        items1.add(new OrderItem(4L, 150.0, 3));
        order1.setItems(items1);

        User user1 = new User();
        user1.setActive(true);
        processor.processOrder(order1, user1);

        // 测试2：用户未激活
        System.out.println("\n=== 测试2：用户未激活 ===");
        User user2 = new User();
        user2.setActive(false);
        processor.processOrder(order1, user2);

        // 测试3：空订单
        System.out.println("\n=== 测试3：空订单 ===");
        Order order3 = new Order();
        order3.setStatus(OrderStatus.PENDING);
        order3.setItems(new ArrayList<>());
        processor.processOrder(order3, user1);

        // 测试4：null 订单
        System.out.println("\n=== 测试4：null 订单 ===");
        processor.processOrder(null, user1);
    }
}
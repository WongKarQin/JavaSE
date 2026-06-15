package ProcessControl;

import java.util.HashMap;
import java.util.Map;

/**
 * 业务判断与权限校验
 */
public class PermissionService {

    // ==================== 主业务方法 ====================

    /**
     * 获取订单详情（带权限校验）
     */
    public Response<OrderDTO> getOrderDetail(Long orderId, User currentUser) {
        // 1. 参数校验
        if (orderId == null || orderId <= 0) {
            return Response.fail("订单ID无效");
        }
        // 2. 用户校验
        if (currentUser == null || !currentUser.isActive()) {
            return Response.fail("用户未登录或已禁用");
        }
        // 3. 查询订单
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            return Response.fail("订单不存在");
        }
        // 4. 权限校验（只能查看自己的订单或管理员）
        if (!order.getUserId().equals(currentUser.getId())
                && !currentUser.hasRole("ADMIN")) {
            return Response.fail("无权查看此订单");
        }
        // 5. 转换并返回（主逻辑）
        OrderDTO dto = OrderMapper.toDTO(order);
        return Response.ok(dto);
    }

    // ==================== 状态机流转 ====================

    /**
     * 状态流转
     */
    public OrderStatus nextStatus(OrderStatus current, String action) {
        return switch (current) {
            case PENDING -> handlePending(action);
            case PAID    -> handlePaid(action);
            case SHIPPED -> handleShipped(action);
            default      -> throw new IllegalStateException("未知状态");
        };
    }

    // 各状态处理方法
    private OrderStatus handlePending(String action) {
        return switch (action) {
            case "PAY"      -> OrderStatus.PAID;
            case "CANCEL"   -> OrderStatus.CANCELLED;
            default         -> throw new IllegalArgumentException("无效操作: " + action);
        };
    }

    private OrderStatus handlePaid(String action) {
        return switch (action) {
            case "SHIP"     -> OrderStatus.SHIPPED;
            case "REFUND"   -> OrderStatus.REFUNDED;
            default         -> throw new IllegalArgumentException("无效操作: " + action);
        };
    }

    private OrderStatus handleShipped(String action) {
        return switch (action) {
            case "RECEIVE"  -> OrderStatus.COMPLETED;
            case "RETURN"   -> OrderStatus.RETURNED;
            default         -> throw new IllegalArgumentException("无效操作: " + action);
        };
    }

    // ==================== 模拟依赖（实际项目中应注入） ====================

    private final OrderRepository orderRepository = new OrderRepository();
    private final OrderMapper orderMapper = new OrderMapper();

    // 模拟 Repository
    public static class OrderRepository {
        private final Map<Long, Order> dataStore = new HashMap<>();

        public java.util.Optional<Order> findById(Long id) {
            return java.util.Optional.ofNullable(dataStore.get(id));
        }

        public void save(Order order) {
            dataStore.put(order.getId(), order);
        }
    }

    // ==================== 内部类定义 ====================

    /**
     * 统一响应包装类
     */
    public static class Response<T> {  //
        private int code;      // 200成功，400参数错误，403无权限，500系统错误
        private String message;
        private T data;

        public static <T> Response<T> ok(T data) {
            Response<T> r = new Response<>();
            r.code = 200;
            r.message = "success";
            r.data = data;
            return r;
        }

        public static <T> Response<T> fail(String message) {
            Response<T> r = new Response<>();
            r.code = 400;
            r.message = message;
            return r;
        }

        // Getters
        public int getCode() { return code; }
        public String getMessage() { return message; }
        public T getData() { return data; }
    }

    /**
     * 订单状态枚举
     */
    public enum OrderStatus {
        PENDING,    // 待支付
        PAID,       // 已支付
        SHIPPED,    // 已发货
        COMPLETED,  // 已完成
        CANCELLED,  // 已取消
        REFUNDED,   // 已退款
        RETURNED    // 已退货
    }

    /**
     * 订单实体
     */
    public static class Order {
        private Long id;
        private Long userId;
        private OrderStatus status;

        public Order(Long id, Long userId, OrderStatus status) {
            this.id = id;
            this.userId = userId;
            this.status = status;
        }

        public Long getId() { return id; }
        public Long getUserId() { return userId; }
        public OrderStatus getStatus() { return status; }
        public void setStatus(OrderStatus status) { this.status = status; }
    }

    /**
     * 订单DTO
     */
    public static class OrderDTO {
        private Long id;
        private Long userId;
        private String statusDesc;

        public OrderDTO(Long id, Long userId, String statusDesc) {
            this.id = id;
            this.userId = userId;
            this.statusDesc = statusDesc;
        }

        @Override
        public String toString() {
            return "OrderDTO{id=" + id + ", userId=" + userId + ", status='" + statusDesc + "'}";
        }
    }

    /**
     * 用户实体
     */
    public static class User {
        private Long id;
        private boolean active;
        private java.util.List<String> roles = new java.util.ArrayList<>();

        public User(Long id, boolean active) {
            this.id = id;
            this.active = active;
        }

        public Long getId() { return id; }
        public boolean isActive() { return active; }
        public boolean hasRole(String role) { return roles.contains(role); }
        public void addRole(String role) { roles.add(role); }
    }

    /**
     * 订单转换器
     */
    public static class OrderMapper {
        public static OrderDTO toDTO(Order order) {
            return new OrderDTO(
                    order.getId(),
                    order.getUserId(),
                    order.getStatus().name()
            );
        }
    }

    // ==================== 测试入口 ====================

    public static void main(String[] args) {
        PermissionService service = new PermissionService();
        OrderRepository repo = service.orderRepository;

        // 准备测试数据
        User admin = new User(1L, true);
        admin.addRole("ADMIN");

        User userA = new User(2L, true);   // 普通用户
        User userB = new User(3L, true);   // 另一个普通用户

        Order order1 = new Order(100L, 2L, OrderStatus.PENDING);  // userA 的订单
        Order order2 = new Order(101L, 2L, OrderStatus.PAID);     // userA 的订单

        repo.save(order1);
        repo.save(order2);

        System.out.println("=== 测试1：管理员查看任意订单 ===");
        System.out.println(service.getOrderDetail(100L, admin).getData());

        System.out.println("\n=== 测试2：用户查看自己的订单 ===");
        System.out.println(service.getOrderDetail(100L, userA).getData());

        System.out.println("\n=== 测试3：用户查看他人订单（无权限）===");
        Response<OrderDTO> resp = service.getOrderDetail(100L, userB);
        System.out.println("Code: " + resp.getCode() + ", Msg: " + resp.getMessage());

        System.out.println("\n=== 测试4：无效订单ID ===");
        Response<OrderDTO> resp2 = service.getOrderDetail(-1L, userA);
        System.out.println("Code: " + resp2.getCode() + ", Msg: " + resp2.getMessage());

        System.out.println("\n=== 测试5：状态机流转 ===");
        System.out.println("PENDING + PAY → " + service.nextStatus(OrderStatus.PENDING, "PAY"));
        System.out.println("PAID + SHIP → " + service.nextStatus(OrderStatus.PAID, "SHIP"));
        System.out.println("SHIPPED + RECEIVE → " + service.nextStatus(OrderStatus.SHIPPED, "RECEIVE"));
    }
}
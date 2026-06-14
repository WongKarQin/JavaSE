package DataType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/***
 * 实际开发中数据类型
 */
public class PracticeApplication {
    //DTO数据传输对象 接口数据承载
    public static class OrderDTO{
        private Long orderId;
        private String orderNo;
        private BigDecimal totalAmount;
        private Integer status;
        private Boolean isUrgent;
        private LocalDateTime createTime;
        private List<OrderItemDTO> item;
        //Geeters & Setters...
        public Long getOrderId() {
            return orderId;
        }
        public void setOrderId(Long orderId) {
            this.orderId = orderId;
        }
        public BigDecimal getTotalAmount() {
            return totalAmount;
        }
        public void setTotalAmount(BigDecimal totalAmount) {
            this.totalAmount = totalAmount;
        }
    }
    //inner class
    private static class OrderItemDTO {
        private Long skuId;
        private String skunmae;
        private BigDecimal unitPrice;
        private Integer quantity;
        private Double weightKg;
        public BigDecimal getUnitPrice() {
            return unitPrice;
        }
        public void setUnitPrice(BigDecimal unitPrice) {
            this.unitPrice = unitPrice;
        }
        public Integer getQuantity() {
            return quantity;
        }
        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }
    }
    //Service层 运算转换
    public static class OrderService{
        public BigDecimal calculateTotal(List<OrderItemDTO> items){
            BigDecimal total = BigDecimal.ZERO;
            for(OrderItemDTO item:items){
                //单价 * 数量 注意精度
                BigDecimal itemTotal = item.getUnitPrice().multiply(new BigDecimal((item.getQuantity())));
                total = total.add(itemTotal);
            }
            return total.setScale(2, BigDecimal.ROUND_HALF_UP);//保留2位小数
        }
    }
    //订单检查
    public void validateOrder(OrderDTO order){
        //空值检查
        if(order.getOrderId() == null){
            throw new IllegalArgumentException("订单ID不能为空！");
        }
        //金额比较
        if(order.getTotalAmount().compareTo(BigDecimal.ZERO)<=0){
            throw new IllegalArgumentException("金额必须大于0！");
        }
    }
    //类型转换工具方法
    public static Integer safeParseInt(String str){
        try {
            return Integer.parseInt(str);
        }catch (NumberFormatException e){
            return null;//返回null而非抛出异常
        }
    }

    public static void main(String[] args) {
        //构建订单
        OrderDTO order = new OrderDTO();
        order.setOrderId(10001L);
        order.setTotalAmount(new BigDecimal("299.99"));
        //空值安全处理
        Integer age = null;
        int agePrimitive = age !=null?age:0;//避免NPE
    }
}

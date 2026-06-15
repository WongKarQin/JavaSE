package ProcessControl;

/***
 * 顺序结构
 */
public class Sequential {
    public static void main(String[] args) {
        //步骤1：声明变量（按照顺序执行）
        int a = 10;
        int b = 20;
        //步骤2:计算（依赖步骤1的结果）
        int sum = a + b;
        //步骤3：输出（依赖步骤2的结果）
        System.out.println("Sum: "+sum);
        //步骤4：方法调用（按顺序执行）
        processData(sum);
    }
    static void processData(int data){
        System.out.println("Processing: "+data);
    }
}

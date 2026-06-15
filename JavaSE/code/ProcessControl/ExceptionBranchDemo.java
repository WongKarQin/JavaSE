package ProcessControl;
/***
 * 异常分支
 */

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ExceptionBranchDemo {
    public double divide(double a, double b) {
        try {
            if (b == 0) {
                throw new ArithmeticException("除数不能为0");
            }
            return a / b;
        } catch (ArithmeticException e) {
            System.out.println("计算错误：" + e.getMessage());
            return Double.NaN;
        } catch (Exception e) {
            System.out.println("未知错误！");
            return 0;
        } finally {
            System.out.println("除法运算结束");
        }
    }

    // Java 7+ 多异常捕获
    public void multiCatch() {
        try {
            // 可能抛出多种异常
            FileInputStream fis = new FileInputStream("test.txt");//可能抛出IOException
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost/test",
                    "user",
                    "pass");//可能抛出SQLException
        } catch (IOException | SQLException e) {
            // 统一处理相关异常
            System.out.println("IO或数据库错误："+e.getMessage());
        }
    }
}
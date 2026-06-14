package DataType;
/***
 * 浮点数精度误差
 * BigDecimal
 */

import java.math.BigDecimal;
public class FloatingPointPrecision {
    public void main(String[] args){
        //精度问题
        System.out.println(0.1+0.2);//0.3000000000000000004×
        System.out.println(0.1+0.2==0.3);//false×
        //比较浮点数的正确方式
        double a = 0.1+0.2;
        double b = 0.3;
        double epsilon = 1e-10;
        boolean equal = Math.abs(a-b) < epsilon;//true√
        //金融计算绝对不能用double/float
        double money = 0.03;
        double resultb = money-0.02;
        System.out.println(resultb);//0.010000000000000002×灾难级！
        //正确做法：使用BigDecimal
        BigDecimal bd1 = new BigDecimal("0.03");//必须使用字符串构造
        BigDecimal bd2 = new BigDecimal("0.02");
        BigDecimal bdResult = bd1.subtract(bd2);
        System.out.println(bdResult);//0.01√
        //!不用使用double构造BigDecimal
        BigDecimal wrong = new BigDecimal(0.03);//仍有精度问题
        //除法精度控制
        BigDecimal ten = new BigDecimal("10");
        BigDecimal three = new BigDecimal("3");
        //ten.divide(three);//×无限小数抛出异常
        BigDecimal quotient = ten.divide(three,4,BigDecimal.ROUND_HALF_UP);//3.3333
    }
}
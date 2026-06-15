package Operator;

/***
 * 算术运算符
 */
public class ArithmeticDemo {
    public static void main(String[] args) {
        int a = 17, b= 5;
        //基本算术
        System.out.println("a+b="+(a+b));//22
        System.out.println("a-b="+(a-b));//12
        System.out.println("a*b="+(a*b));//85
        System.out.println("a/b="+(a/b));//3整数除法截断
        System.out.println("a%b="+(a%b));//2(取模)
        //整数除法陷阱
        System.out.println("17/5.0="+(17/5.0));//3.4(自动提升精度)
        //自增/自减的副作用（前置vs后置）
        int x = 5;
        int y = x++;//y=5 x=6 先赋值y 再x自增
        int z = ++x;//y=7 x=7 先x自增 再赋值y
        System.out.println("x=" +x+", y="+y+", z=" + z);

        int m = 5;
        int result = m++ + ++m;// 5+7 = 12 但m变成了7
        System.out.println("m=" + m +", result=" + result);//难以预测

    }
}

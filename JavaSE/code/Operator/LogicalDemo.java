package Operator;

/***
 * 逻辑运算符
 * 短路特性
 */
public class LogicalDemo {
    static boolean checkA(){
        System.out.println("检查A...");
        return false;
    }
    static boolean checkB(){
        System.out.println("检查B...");
        return true;
    }

    public static void main(String[] args) {
        boolean a = true, b = false;
        //基本逻辑运算
        System.out.println("a && b:"+(a&&b));//false 逻辑与
        System.out.println("a || b:"+(a||b));//true 逻辑或
        System.out.println("!a:"+(!a));//false 逻辑非
        System.out.println("a ^ b:"+(a^b));//true 逻辑异或
        //短路逻辑演示
        System.out.println("\n 短路 &&");
        boolean r1 = checkA()&&checkB();//checkA返回false, checkB不会执行！
        System.out.println("结果："+r1);
        System.out.println("\n 短路 && ||");
        boolean r2 = checkB()||checkA();//checkB返回true，checkA不会执行！
        System.out.println("结果:"+r2);
        //短路陷阱：可能跳过必要的安全检查
        String str = null;
        //利用短路避免NPE
        if(str!=null && str.length() >5){
            System.out.println("字符串长度大于5");
        }
        //错误写法：调换顺序会导致NullPointerException
        //if(str.length()>5 && str!=null){...}//NPE!
        //非短路运算符 & 和 !(运算符也可以用于布尔，但不短路)
        System.out.println("非短路 &");
        boolean r3 = checkA() & checkB();//两边都执行
        System.out.println("结果:"+r3);
    }
}

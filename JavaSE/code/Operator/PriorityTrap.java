package Operator;

/***
 * 运算符优先级易错点
 */
public class PriorityTrap {
    public static void main(String[] args) {
        int a = 5, b=3, c=2;
        //忘记括号导致逻辑错误
        boolean wrong = a>b && b>c || a<c;
        //实际：((a>b)&&(b>c)) || (a<c)=true
        //可能意图:a>b && (b>c || a<c)

        //正确做法:复杂条件始终加括号
        boolean correct = (a>b) && ((b>c) || (a<c));

        //❌ 位运算符优先级低于关系运算
        int flags = 0b1010;
        //错误：想检查第2位是否为1
        //if(flag & 0b0100 !=0)//编译错误:&优先级低于!=
        //正确：
        if((flags & 0b0100)!=0){
            System.out.println("第2位为1！");
        }
        //❌ 赋值运算符优先级最低
        int x =5;
        boolean flag =x == 5;//先比较，后赋值 ✅
        //boolean flag = x =5;//编译错误：赋值给boolean不行
        //❌ 三元运算符嵌套可读性差
        int score = 85;
        String grade = score>=90?"A":score>=80?"8":score>=60?"C":"D";
        //建议复杂逻辑使用if-else
        /***
         * 优先级从高到低
         * 1.后缀() [] . ++ --
         * 2.一元运算符！~++ -- + -
         * 3.乘除 * / %
         * 4.加减 + -
         * 5.位移 << >> >>>
         * 6.关系 < > <= >= instanceof
         * 7.相等 == !=
         * 8.位与 &
         * 9.位异或 ^
         * 10.位或 |
         * 11.逻辑与 &&
         * 12.逻辑或 ||
         * 13.三元 ？
         * 14.赋值 = += -=等
         */
    }
}

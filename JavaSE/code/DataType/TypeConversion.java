package DataType;

/***
 * 自动类型提升与强制转换
 */
public class TypeConversion {
    public static void main(String[] args) {
        //自动类型提升（隐式，小->大，安全）
        byte b = 10;
        short s = b;//byte -> short √
        int i = s;//short -> int √
        long l = i;//int -> long √
        float f = l;//long -> float √ 可能损失精度
        double d = f;//float -> double √
        //混合运算时整体提升为最大类型
        int x= 10;
        double y =3.5;
        double z = x+y;//int自动提升为double
        //强制类型转换(显式，大->小，危险)
        double pi=3.14159;
        int truncated =  (int) pi;//3 小数部分截断非四舍五入
        //溢出风险
        long bigValue = 3000000000L;//30亿超过int范围
        int overflow = (int) bigValue;
        System.out.println(overflow);//-1294967296×数据完全错误
        //char与int特殊关系
        char a = 'A', b2 = 'B';
        //char sum = a+b2;//× 结果是int(131)
        char sum = (char) (a+b2);//需要强制转换
        //赋值时候字面量优化
        byte bb =100;//√ 100在byte的范围内，编译器自动转换
        //byte cc = 200;//× 超出byte范围，编译错误
        byte cc = (byte) 200;//-56(截断) 算式：-128+（200-127-1）
    }
}

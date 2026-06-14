package DataType;
/***
 * 数据范围溢出案例
 */
public class RangeLimitations{
    public static void main(String[] args){
        //int溢出最大值+1变成最小值
        int maxInt = Integer.MAX_VALUE;//2147483647
        int overflow = maxInt + 1; //-2147483648(回绕)
        System.out.println("int MAX："+maxInt);
        System.out.println("int溢出："+overflow);
        //运算前检查或者直接使用long
        long safe = (long) maxInt +1;//2147483648L
        //long也可能溢出 极大数据量
        long big = Long.MAX_VALUE;//big+1 -> -9223372036854775808L
        //byte/short运算时自动提升为int
        byte b1 =10, b2=20;
        //byte sum = b1+b2;//×编译错误 b1+b2结果为int
        byte sum = (byte) (b1+b2);//需要强转 但可能溢出
        //循环中的溢出bug
        for(int i = 0;i>=0;i++){
            //当i=2147483647后，i++变为-2147483648
            //条件仍然满足，死循环！
            if(i<0){
                System.out.println("溢出detected："+i);
                break;
            }
        }
    }
}
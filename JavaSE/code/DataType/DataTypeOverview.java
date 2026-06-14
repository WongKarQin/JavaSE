package DataType;
/***
 * 数据类型总览
 * JAVA数据类型分为2大类：
 * 1.基本类型(Primitive Types):8种，直接存储数值
 * 2.引用类型(Reference Types):存储对象地址
 */
public class DataTypeOverview {
    //整数型
    byte b = 127;//1字节 [-128,127]
    short s = 32767;//2字节 [-32768,32767]
    int i  = 2147483647;//4字节 约±21亿(最常用)
    long l = 9223372036854775807L;//8字节 极大数值 需加L后缀
    //浮点数
    float f = 3.14f;//4字节 6~7位有效数字 需要加f后缀
    double d = 3.1415926535;//8字节 15~16位有效数字 默认浮点类型
    //字符型
    char c = '中';//2字节 Unicode字符 单引号
    //布尔型
    boolean flag = true;// true/ flase 大小由JVM决定 通常1字节
    //引用类型
    String str = "Hello";//字符串
    Integer boxed = 100;//包装类
    int[] arr = {1,2,3};//数组
}
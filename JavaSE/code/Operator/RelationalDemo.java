package Operator;

/***
 * 关系运算符
 */
public class RelationalDemo {
    public static void main(String[] args) {
        int a = 10, b=20;
        System.out.println("a==b:"+(a==b));//false
        System.out.println("a!=b:"+(a!=b));//true
        System.out.println("a>b:"+(a>b));//false
        System.out.println("a<b:"+(a<b));//true
        System.out.println("a>=b:"+(a>=b));//false
        System.out.println("a<=b:"+(a<=b));//true
        //引用类型==比较的是地址，不是内容
        String s1 = new String("hello");
        String s2 = new String("hello");
        System.out.println("s1 == s2:"+(s1==s2));//false
        System.out.println("s1.equals(s2):"+s1.equals(s2));//true
        //instanceof类型检查
        Object obj = "test";
        System.out.println("obj instanceof String:"+(obj instanceof String));//true
    }
}
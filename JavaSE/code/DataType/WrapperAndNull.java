package DataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/***
 * 包装类与空值问题
 */
public class WrapperAndNull {
    public static void main(String[] args) {
        //基本类型 VS 包装类
        int primitive = 100;//栈上分配，有默认值（0），不能为null
        Integer wrapper = 100;//堆上分配，可为null，有方法
        //空值问题：包装类可以解决
        Integer nullable =null; //✅包装类可以为null
        // int notNull = null;❌编译错误！基本类型不能为null
        //自动装箱 自动拆箱 auto-boxing auto-unboxing
        Integer boxed = 10;//自动装箱：Integer.valueOf(10)
        int unboxed = boxed;//自动拆箱：boxed.intValue()
        //空指针陷阱：拆箱时候NPE null pointer error
        Integer nullObj = null;
        //int crash = nullObj;//❌运行时报NullPointerException!
        //安全拆箱
        int safe = (nullObj!=null)?nullObj:0;//安全拆箱
        //java8+更优雅写法：
        int safe2 = Optional.ofNullable(nullObj).orElse(0);
        //缓存陷阱 ==与equals()
        Integer a =100;
        Integer b =100;
        System.out.println(a==b);//true(本地缓存 -128~127)
        Integer c = 200;
        Integer d= 200;
        System.out.println(c==d);//false(超出缓存范围，不同对象地址不同)
        System.out.println(c.equals(d));//true ✅ 数值相等
        //集合必须使用包装类
        //List<int>list;//❌语法错误
        List<Integer> list = new ArrayList<>();//✅
        list.add(100);
        int first = list.get(0);//自动拆箱
        //数据库/接口映射：用包装类表示可空字段
        class UserDTO{
            private Long id;//默认为null，可能未分配初始值
            private String name;//默认为null，不为“”
            private Integer age;//默认为null，可能未知
            private int score;//必须有默认值（0）
        }
    }

}

package Operator;

/***
 * 位运算符
 * 状态标记与权限设计核心
 */
public class BitwiseDemo {
    //权限位设计
    //每个权限用1位表示，不重复占有
    static final int PERM_READ = 1 <<0;//0001 = 1
    static final int PERM_WRITE = 1<<1;//0010 = 2
    static final int PERM_EXEC = 1<<2;//0100 = 4
    static final int PERM_DELETE = 1<<3;//1000 = 8

    public static void main(String[] args) {
        int a = 0b1100;//12
        int b = 0b1010;//10
        //位与：对应位都为1才为1(用于权限检查)
        System.out.println("a & b = "+(a & b)+"(二进制："+Integer.toBinaryString(a&b)+")");
        //1100 & 1010 = 1000(8)

        //位或：对应位有1就为1 用于添加权限
        System.out.println("a | b = "+(a|b)+"(二进制："+Integer.toBinaryString(a|b)+")");
        //1100 | 1010 = 1110(14)

        //异或 对应位置不同则为1 用于切换状态
        System.out.println("a ^ b ="+(a^b)+"二进制："+Integer.toBinaryString(a^b)+")");
        //1100 ^ 1010 = 0110(6)
        //取反：所有位置翻转
        System.out.println("~a = "+(~a));//按位取反
        //位移运算
        System.out.println("a<<2 = "+(a<<2));//左移*4 1100->110000(48)
        System.out.println("a>>2 = "+(a>>2));//右移/4 1100->0011(3)
        System.out.println("-8>>>2 = "+(-8>>>2));//无序号右移，高位补0
        //权限系统
        System.out.println("\n========   权限系统演示   ========");
        //给用户授予读写权限
        int userPerms = PERM_READ | PERM_WRITE;
        System.out.println("用户权限值："+userPerms+"（二进制"+
                String.format("%4s",Integer.toBinaryString(userPerms)).replace(' ','0')+")");
        //检查是否有读的权限
        boolean canRead = (userPerms & PERM_READ)!=0;
        System.out.println("能否读取？"+canRead);//true
        //检查是否有删除权限
        boolean canDelete = (userPerms & PERM_DELETE)!=0;
        System.out.println("能否删除?"+canDelete);//false
        //添加执行权限
        userPerms |= PERM_EXEC;
        System.out.println("添加执行权限后："+userPerms);
        //移除写权限
        userPerms &= ~PERM_WRITE;
        System.out.println("移除写权限后:"+userPerms);
        //切换读权限（有责移除，无则添加）
        System.out.println("切换读权限后："+userPerms);
        //边界问题：int 只有32为，权限超过32个需要用long或BitSet
        System.out.println("\n=======  位运算边界 ======");
        int maxBit =1 <<30;//OK
        //int overflow = 1<<31;//变成了负数
        System.out.println("1<<30="+maxBit);
        System.out.println("1<<31="+(1<<31)+"（溢出变成负数！）");
    }
}

package Operator;

import java.util.List;

/***
 * 综合实战：状态标记与条件判断
 */
public class OperatorPratice {
    //状态标记设计（使用位运算节省内存）
    static class UserStatus{
        static final byte ACTIVE = 1<<0;//0001
        static final byte VERIFIED = 1<<1;//0010
        static final byte VIP = 1<<2;//0100
        static final byte BANNED = 1<<3;//1000

        private byte status = 0;
        //添加状态
        void addStatus(byte flag){status |=flag;}
        //移除状态
        void removeStatus(byte flag){status &= ~flag;}
        //检查状态
        boolean hasStatus(byte flag){return (status & flag)!=0;}
        //切换状态
        void toggleStatus(byte flag){status ^=flag;}

        void printStatus(){
            System.out.println("当前状态："+String.format("%4s",Integer.toBinaryString(status & 0xFF)).replace(' ','0'));
            System.out.println("激活："+hasStatus(ACTIVE));
            System.out.println("认证："+hasStatus(VERIFIED));
            System.out.println("VIP："+hasStatus(VIP));
            System.out.println("封禁："+hasStatus(BANNED));
        }
    }
    static String getConfig(){return null;}

    public static void main(String[] args) {
        //状态标记实战
        UserStatus user = new UserStatus();
        user.addStatus(UserStatus.ACTIVE);
        user.addStatus(UserStatus.VIP);
        System.out.println("====== 用户状态 ======");
        user.printStatus();
        //条件判断，利用短路优化性能
        List<String> data =null;//可能为空
        //安全访问 短路避免NPE
        if(data!=null && !data.isEmpty() && data.get(0).startsWith("A")){
            System.out.println("首条数据以A开头");
        }
        //默认值设置 利用||
        String config = getConfig();//可能返回null
        String actual = config !=null ? config : "default";

        //范围判断：利用 && 链式
        int age = 25;
        boolean isAdult = age>=18 && age <60;
        //多条件互斥：利用^
        boolean a = true, b=false;
        boolean exactlyOne = a^b;//仅1个为true
        //整数奇偶判断：位运算最快
        int num = 7;
        boolean isOdd = (num & 1) == 1;//比num % 2 == 1更高效
        //乘除2的幂：位移最快（编译器通常自动优化）
        int n = 16;
        int doubled = n<<1;// *2
        int halved = n>>1;// /2
    }
}
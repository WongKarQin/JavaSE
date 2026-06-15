package ProcessControl;

import java.util.List;

/***
 * 循环结构
 */
public class ForLoopDemo {
    public void basicFor(){
        for(int i = 0; i<5; i++){
            System.out.println("Iteration:"+i);
        }
    }
    //for each-只读遍历
    public void enhancedFor(List<String> items){
        for(String item:items){
            System.out.println(item);
        }
    }
    public void reverseTraverse(List<String> list){
        for(int i = list.size()-1; i>=0;i--){
            if(list.get(i).startsWith("temp")){
                list.remove(i);// ✅倒序删除不会跳过元素
            }
        }
    }
    //4多变量for循环
    public void multiVar(){
        for(int i = 0, j=10; i<j; i++, j--){
            System.out.printf("i=%d, j=%d%n",i,j);
        }
    }
    //无限循环（配合break使用）
    public void infiniteLoop(){
        int count = 0;
        for(;;){
            if(count++ >= 100)break;
        }
    }
}

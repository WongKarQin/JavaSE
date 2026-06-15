package ProcessControl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/***
 * break vs continue vs return
 */
public class JumpDemo {
    public void breakDemo(){
        for(int i=0; i<10;i++){
            if(i==5){
                break;//立即退出循环，i=5不执行后续
            }
            System.out.println(i);//输出0,1,2,3,4
        }
    }
    //continue：跳过当前迭代
    public void continueDemo(){
        for(int i=0; i<10; i++){
            if(i%2==0){
                continue;//跳过偶数，继续下一次迭代
            }
            System.out.println(i);//输出1,3,5,7,9
        }
    }
    //break+标签：跳出多层循环
    public void breakWithLabel(){
        outer://标签
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                if(i==1 && j==1){
                    break outer;//跳出外层循环
                }
                System.out.printf("(%d,%d)",i,j);
            }
        }
        //输出:(0,0) (0,1) (0,2) (1,0)
    }
    //return:跳出整个方法
    public String findFirst(List<String> list, String target){
        for(String item:list){
            if(item.equals(target)){
                return item;
            }
        }
        return null;
    }
    //Java8+ Stream替代循环
    public class StreamVsLoop{
        //❌ 传统循环：过滤 + 转换 + 搜集
        public List<String> oldStyle(List<Integer> numbers){
            List<String> result = new ArrayList<>();
            for(Integer num:numbers){
                if(num>0){
                    String str = String.valueOf(num * 2);
                    result.add(str);
                }
            }
            return result;
        }
        //Stream:声明式，可读性更强
        public List<String> streamStyle(List<Integer> numbers){
            return numbers.stream()
                    .filter(num->num>0)//过滤
                    .map(num->num*2)//转换
                    .map(String::valueOf)//映射
                    .collect(Collectors.toList());//收集
        }
        //提前终止(替代break)
        public Optional<Integer> findFirstMatch(List<Integer> numbers){
            return numbers.stream()
                    .filter(num->num>100).
                    findFirst();//找到第一个即终止（短路操作）

        }
    }
}
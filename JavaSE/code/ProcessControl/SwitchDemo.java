package ProcessControl;

/***
 * switch语句
 */
public class SwitchDemo {
    //传统switch(注意穿透问题！)
    public String oldSwithc(int month){
        String season;
        switch(month){
            case 3://穿透！
            case 4://穿透！
            case 5:
                season = "Spring";
                break;
            case 6:
            case 7:
            case 8:
                season = "Summer";
                break;
            default:
                season ="Unknown";
        }
        return season;
    }
    //Java 12+ 箭头语法（无穿透，更简洁）
    public String newSwitch(int month){
        return switch(month){
            case 3,4,5 ->"Spring";
            case 6,7,8 ->"Summer";
            case 9,10,11->"Autumn";
            case 12,1,2 ->"Winter";
            default -> "unknown";
        };
    }
    //Java 17+ switch表达式（支持模式匹配预览）
    public String switchExpression(Object obj){
        return switch(obj){
            case Integer i ->"Integer:"+i;
            case String s ->"String" + s.toUpperCase();
            case null ->"null";
            default -> "Unknow";
        };
    }
}
package ProcessControl;

/***
 * if-else系列
 */
public class BranchDemo {
    //单分支if
    public void singleBranch(int age){
        if (age >= 18) {
            System.out.println("成年人");
        }
    }
    //双分支 if-else
    public String checkStatus(boolean isActive){
        if(isActive){
            return "Active";
        }else{
            return "Inactive";
        }
    }
    //多分支if else if else
    public String getGrade(int score){
        if(score>= 90){
            return"A";
        }else if(score >=80){
            return "B";
        }else if(score >=60){
            return "C";
        }else{
            return "D";
        }
    }
    public String simpleCheck(int num){
        return num > 0 ? "Positive":"Non-positive";
    }
}

package explain.cal;

import java.math.BigDecimal;

/**
 * 加减乘除计算
 * Created by zhangshan193 on 17/2/24.
 */
public abstract class AbstractCalculate {


    // 2+3+3*4*5/2*4-12/4+3+(3*4/(2+1))
    public void interpret(CalContext calContext){

        // 如果含有括号



    }

    public abstract void calculate(BigDecimal num, double value);

}

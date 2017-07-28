package explain.music;

/**
 * 表达式类
 * Created by zhangshan193 on 17/2/24.
 */
public abstract class AbstractExpression {


    public void interpret(PlayContext playContext){

        if(playContext.getText() == null || playContext.getText().trim().length() == 0){
            return;
        }
        // O 3 E 0.5 G 0.5 A 3
        String text = playContext.getText().trim();

        String[] textArr = text.split(" ");
        StringBuilder sb = new StringBuilder();
        for(int i=2;i<textArr.length;i++){
            sb.append(textArr[i]).append(" ");
        }
        playContext.setText(sb.toString());
        execute(textArr[0],Double.parseDouble(textArr[1]));

    }

    public abstract void execute(String key,double value);

}

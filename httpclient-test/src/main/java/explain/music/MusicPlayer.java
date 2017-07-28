package explain.music;

/**
 * Created by zhangshan193 on 17/2/24.
 */
public class MusicPlayer {


    public static void main(String[] args) {
        PlayContext playContext = new PlayContext();
        playContext.setText("O 2 E 0.5 G 0.5 A 3 E 0.5 G 0.5 D 3 E 0.5 G 0.5 A 0.5 O 3 C 1 O 2 A 0.5 G 1 C 0.5 E 0.5 D 3");
        AbstractExpression expression = null;
        while(playContext.getText().length() > 0){
            if(playContext.getText().startsWith("O")){
                expression = new ScaleExpression();
            }else{
                expression = new NoteExpression();
            }

            expression.interpret(playContext);
        }



    }


}

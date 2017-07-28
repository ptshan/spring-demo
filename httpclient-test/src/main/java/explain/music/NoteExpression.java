package explain.music;

/**
 * 音符类
 * Created by zhangshan193 on 17/2/24.
 */
public class NoteExpression extends AbstractExpression {

    public void execute(String key, double value) {

        String note = "";
        note = key
                .replaceAll("C","1")
                .replaceAll("D","2")
                .replaceAll("E","3")
                .replaceAll("F","4")
                .replaceAll("G","5")
                .replaceAll("A","6")
                .replaceAll("B","7")
                ;
        System.out.println("note:"+note);

    }

}

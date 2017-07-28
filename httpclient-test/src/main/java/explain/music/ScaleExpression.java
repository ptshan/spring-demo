package explain.music;

/**
 * 音符类
 * Created by zhangshan193 on 17/2/24.
 */
public class ScaleExpression extends AbstractExpression {
    public void execute(String key, double value) {
        String scale = "";
        switch ((int)value){
            case 1:
                scale = "低音";
                break;
            case 2:
                scale = "中音";
                break;
            case 3:
                scale = "高音";
                break;
        }
        System.out.println("scale:"+scale);
    }
}

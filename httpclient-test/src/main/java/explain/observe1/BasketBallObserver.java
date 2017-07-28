package explain.observe1;

/**
 * Created by zhangshan193 on 17/2/27.
 */
public class BasketBallObserver implements IObserver {

    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void updateState(String event) {
        System.out.println(event+"    "+name+"    basketballobserver 被通知了,关闭篮球网页,继续工作");
    }
}

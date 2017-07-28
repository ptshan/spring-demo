package explain.observe1;

/**
 * Created by zhangshan193 on 17/2/27.
 */
public interface IObserver {

    public void setName(String name);

    public String getName();

    public void updateState(String state);


}

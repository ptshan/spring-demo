package explain.observe1;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangshan193 on 17/2/27.
 */
public class Secretary implements ISubject{

    private String subjectState;

    private List<IObserver> observerList = new ArrayList<IObserver>();

    public void attach(IObserver observer){
        observerList.add(observer);
    }

    public void detach(IObserver observer){
        observerList.remove(observer);
    }

    public void notifyObs() {
        for(IObserver observer:observerList){
            observer.updateState(subjectState);
        }
    }

    public String getSubjectState() {
        return subjectState;
    }

    public void setSubjectState(String subjectState) {
        this.subjectState = subjectState;
    }

}

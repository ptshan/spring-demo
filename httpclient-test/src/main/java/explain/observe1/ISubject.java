package explain.observe1;

/**
 * Created by zhangshan193 on 17/2/28.
 */
public interface ISubject {

    void attach(IObserver observer);

    void detach(IObserver observer);

    void notifyObs();

    String getSubjectState();

    void setSubjectState(String subjectState);

}

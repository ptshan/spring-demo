package explain.observe1;

/**
 * Created by zhangshan193 on 17/2/27.
 */
public class ObserverTest {


    public static void main(String[] args) {
        Secretary secretary = new Secretary() ;

        StockObserver stockObserver = new StockObserver();
        BasketBallObserver basketBallObserver = new BasketBallObserver();

        stockObserver.setName("李股票");
        basketBallObserver.setName("赵篮球");

        secretary.attach(stockObserver);
        secretary.attach(basketBallObserver);

        secretary.notifyObs();
    }

}

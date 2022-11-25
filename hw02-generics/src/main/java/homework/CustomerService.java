package homework;

import java.util.HashMap;
import java.util.Map;
import java.util.AbstractMap;

public class CustomerService {

    private Map<Customer, String> map = new HashMap<>();

    //todo: 3. надо реализовать методы этого класса
    //важно подобрать подходящую Map-у, посмотрите на редко используемые методы, они тут полезны

    public Map.Entry<Customer, String> getSmallest() {
        Map.Entry<Customer, String> smallestScore = null;
        //Возможно, чтобы реализовать этот метод, потребуется посмотреть как Map.Entry сделан в jdk
        for (Map.Entry<Customer, String> entry : map.entrySet()) {
            if (smallestScore == null) {
                smallestScore = new AbstractMap.SimpleEntry<Customer, String>(
                        new Customer(entry.getKey().getId(), entry.getKey().getName(), entry.getKey().getScores()),
                        entry.getValue());
            } else {
                if (entry.getKey().getScores() < smallestScore.getKey().getScores()) {
                    smallestScore = new AbstractMap.SimpleEntry<Customer, String>(
                            new Customer(entry.getKey().getId(), entry.getKey().getName(), entry.getKey().getScores()),
                            entry.getValue());
                }
            }
        }
        return smallestScore;
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Map.Entry<Customer, String> nextScore = null;

        for (Map.Entry<Customer, String> entry : map.entrySet()) {
            if ((entry.getKey().getScores() > customer.getScores()) &&
                    (nextScore == null || (nextScore.getKey().getScores() > entry.getKey().getScores()))) {
                nextScore = new AbstractMap.SimpleEntry<Customer, String>(
                        new Customer(entry.getKey().getId(), entry.getKey().getName(), entry.getKey().getScores()),
                        entry.getValue());
            }
        }
        return nextScore;
    }

    public void add(Customer customer, String data) {
        map.put(customer, data);
    }
}

package homework;

import java.util.*;

public class CustomerService {

    private TreeMap<Customer, String> map = new TreeMap<>(Comparator.comparingLong(o -> o.getScores()));

    //todo: 3. надо реализовать методы этого класса
    //важно подобрать подходящую Map-у, посмотрите на редко используемые методы, они тут полезны

    public Map.Entry<Customer, String> getSmallest() {
        Map.Entry<Customer, String> smallestScore = map.firstEntry();
        //Возможно, чтобы реализовать этот метод, потребуется посмотреть как Map.Entry сделан в jdk
        return smallestScore == null ? null : new AbstractMap.SimpleEntry<Customer, String>(
                new Customer(smallestScore.getKey().getId(), smallestScore.getKey().getName(),
                        smallestScore.getKey().getScores()), smallestScore.getValue());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Map.Entry<Customer, String> nextScore = map.higherEntry(customer);
        return nextScore == null ? null : new AbstractMap.SimpleEntry<Customer, String>(
                new Customer(nextScore.getKey().getId(), nextScore.getKey().getName(), nextScore.getKey().getScores()),
                nextScore.getValue());
    }

    public void add(Customer customer, String data) {
        map.put(customer, data);
    }
}

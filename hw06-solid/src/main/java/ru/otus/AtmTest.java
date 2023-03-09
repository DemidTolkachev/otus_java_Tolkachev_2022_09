package ru.otus;

import ru.otus.banknotes.Banknote;
import ru.otus.banknotes.RussianRuble;
import ru.otus.exceptions.ImpossibleCashAmmountException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class AtmTest {

    private Atm atm;
    private Banknote[] banknotesForTest =
            {RussianRuble.ONE_HUNDRED, RussianRuble.ONE_HUNDRED, RussianRuble.ONE_HUNDRED, RussianRuble.ONE_HUNDRED,
                    RussianRuble.TWO_HUNDRED, RussianRuble.TWO_HUNDRED, RussianRuble.TWO_HUNDRED,
                    RussianRuble.FIVE_HUNDRED, RussianRuble.FIVE_HUNDRED, RussianRuble.FIVE_HUNDRED,
                    RussianRuble.ONE_THOUSAND, RussianRuble.ONE_THOUSAND, RussianRuble.ONE_THOUSAND,
                    RussianRuble.ONE_THOUSAND, RussianRuble.TWO_THOUSAND, RussianRuble.TWO_THOUSAND,
                    RussianRuble.FIVE_THOUSAND, RussianRuble.FIVE_THOUSAND};

    // Инициализация банкомата с деньгами
    public void initialAtm() {
        atm = new Atm();

        Collection<Banknote> banknotes = new ArrayList<>();
        for (Banknote banknote : this.banknotesForTest) {
            banknotes.add(banknote);
        }
        atm.addBanknotes(banknotes);
        System.out.println("Инициализируем банкомат с деньгами. Начальный баланс: " + atm.getBalance());
        System.out.println("----------------------------");
    }

    // Загрузка купюр
    public void load(List<Banknote> banknotes) {
        System.out.println("В банкомате было: " + atm.getBalance());
        atm.addBanknotes(banknotes);
        System.out.println("Загрузили купюры. В банкомате стало: " + atm.getBalance());
        System.out.println("----------------------------");
    }

    // Выдача купюр
    public void cashOut(int amount) {
        try {
            Collection<Banknote> banknotes = atm.getCash(amount);
            System.out.println("Выдаём сумму " + amount);
            System.out.println("Купюры:");
            for (Banknote banknote : banknotes) {
                System.out.println(banknote.getClass().getSimpleName() + " " + banknote.nominal());
            }
            System.out.println(
                    "Выданная сумма: " + (int) banknotes.stream().map(Banknote::nominal).reduce(Integer::sum).get());
            System.out.println("----------------------------");
        } catch (ImpossibleCashAmmountException e) {
            System.out.println("Ошибка выдачи купюр! " + e.getMessage());
            System.out.println("----------------------------");
        }
    }

    // Получение баланса
    public void getBalance() {
        long balance = atm.getBalance();
        System.out.println("Текущий баланс: " + balance);
        System.out.println("----------------------------");
    }
}
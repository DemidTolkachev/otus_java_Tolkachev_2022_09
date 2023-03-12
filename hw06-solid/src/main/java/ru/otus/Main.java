package ru.otus;

import ru.otus.banknotes.RussianRuble;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        AtmTest atm = new AtmTest();
        // Инициализируем банкомат
        atm.initialAtm();
        // Загружаем в банкомат дополнительные купюры
        atm.load(List.of(RussianRuble.FIVE_HUNDRED, RussianRuble.ONE_HUNDRED));
        // Выдаём разное число денег:
        atm.cashOut(100);
        atm.cashOut(13800);
        // --------
        // Загружаем в банкомат дополнительные купюры
        atm.load(List.of(RussianRuble.ONE_HUNDRED, RussianRuble.TWO_HUNDRED, RussianRuble.FIVE_HUNDRED));
        // Выдаём разное число денег:
        atm.cashOut(1700);
        atm.cashOut(-1);
        atm.cashOut(777);
        // --------
        // Запрашиваем баланс
        atm.getBalance();
    }

}

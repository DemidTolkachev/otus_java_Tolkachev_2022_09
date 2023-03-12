package ru.otus;

import ru.otus.banknotes.Banknote;
import ru.otus.banknotes.RussianRuble;
import ru.otus.basket.CellsBasket;
import ru.otus.basket.CellsBasketImpl;
import ru.otus.exceptions.ImpossibleCashAmmountException;

import java.util.Collection;
import java.util.List;

public class Atm {

    private CellsBasket cellsBasket = new CellsBasketImpl(RussianRuble.ONE_HUNDRED);

    // Добавление купюр
    public List<Banknote> addBanknotes(Collection<Banknote> banknotes) {
        return cellsBasket.addBanknotes(banknotes);
    }

    // Получение наличных
    public List<Banknote> getCash(int amount) {
        if (amount <= 0) {
            throw new ImpossibleCashAmmountException("Запрошенная сумма должна быть больше нуля.", amount);
        }
        List<Banknote> banknotes = cellsBasket.getAmount(amount);
        if (banknotes.size() == 0)
            throw new ImpossibleCashAmmountException("Не удалось вернуть запрошенную сумму.", amount);
        return banknotes;
    }

    // Получение общего остатка
    public int getBalance() {
        return cellsBasket.balance();
    }


}

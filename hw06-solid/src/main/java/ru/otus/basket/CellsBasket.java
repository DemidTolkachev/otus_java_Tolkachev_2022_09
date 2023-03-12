package ru.otus.basket;

import ru.otus.banknotes.Banknote;

import java.util.Collection;
import java.util.List;

// корзина с ячейками
public interface CellsBasket {

    // Добавить купюры в банкомат
    List<Banknote> addBanknotes(Collection<Banknote> banknotes);

    // Получить купюрами указанную сумму, если возможно
    List<Banknote> getAmount(int amount) throws IllegalArgumentException;

    // Узнать остаток
    int balance();
}

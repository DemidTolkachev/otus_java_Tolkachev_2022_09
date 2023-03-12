package ru.otus.basket;

import ru.otus.banknotes.Banknote;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

// ячейка
public class Cell {

    private final Banknote banknote;
    private final LinkedList<Banknote> banknotes = new LinkedList<>();


    Cell(Banknote banknote) {
        this.banknote = banknote;
    }

    // Получить указанное количество купюр
    public List<Banknote> getBanknotes(int cnt) throws IllegalArgumentException {
        if (cnt < 0 || cnt > banknotes.size())
            throw new IllegalArgumentException("Невозможно выдать указанное количество купюр");
        List<Banknote> res = new ArrayList<>();
        for (int i = 0; i < cnt; i++) {
            res.add(getBanknote());
        }
        return res;
    }

    // Добавление купюры в ячейку
    public boolean addBanknote(Banknote banknote) {
        if (checkBanknote(banknote) && this.banknote.nominal() == banknote.nominal()) {
            return banknotes.add(banknote);
        } else {
            return false;
        }
    }

    // Номинал в ячейке
    public int nominal() {
        return banknote.nominal();
    }

    // Количество купюр в ячейке
    public int count() {
        return banknotes.size();
    }

    // Баланс в ячейке
    public int balance() {
        return banknote.nominal() * count();
    }

    // Получить одну купюру
    private Banknote getBanknote() {
        return banknotes.poll();
    }

    // Проверка купюры
    private boolean checkBanknote(Banknote banknote) {
        return banknote.getClass() == this.banknote.getClass();
    }
}

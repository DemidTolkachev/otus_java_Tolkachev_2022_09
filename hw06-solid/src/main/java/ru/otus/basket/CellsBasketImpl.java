package ru.otus.basket;

import ru.otus.banknotes.Banknote;

import java.util.*;

// реализация корзины с ячейками
public class CellsBasketImpl implements CellsBasket {

    // хранилище купюр
    private final SortedMap<Integer, Cell> cells =
            new TreeMap<>(Comparator.comparing(Integer::intValue, Comparator.reverseOrder()));

    // купюры, которым не нашлось места в ячейках
    private final List<Banknote> undefinedBanknotes = new ArrayList<>();

    public CellsBasketImpl(Banknote banknote) {
        for (Banknote banknoteN : banknote.valuesList()) {
            cells.put(banknoteN.nominal(), new Cell(banknoteN));
        }
    }

    // Добавление коллекции купюр
    @Override
    public List<Banknote> addBanknotes(Collection<Banknote> banknotes) {
        for (Banknote banknote : banknotes) {
            addBanknote(banknote);
        }
        return getUndefinedBanknotes();
    }

    // Проверка баланса
    @Override
    public int balance() {
        int balance = 0;
        for (Cell cell : cells.values()) {
            balance += cell.balance();
        }
        return balance;
    }

    // Выдача указанной суммы купюрами
    @Override
    public List<Banknote> getAmount(int amount) throws IllegalArgumentException {
        List<Banknote> banknotes = new ArrayList<>();
        if (canGet(amount)) {
            for (Cell cell : cells.values()) {
                int cnt = Math.min(cell.count(), amount / cell.nominal());
                banknotes.addAll(cell.getBanknotes(cnt));
                amount -= cnt * cell.nominal();
                if (amount == 0) return banknotes;
            }
        }
        return banknotes;
    }

    // Проверка возможности выдачи запрашиваемой сумммы
    private boolean canGet(int amount) {
        // идем в порядке убывания номинала
        for (Cell cell : cells.values()) {
            int cnt = Math.min(cell.count(), amount / cell.nominal());
            amount -= cnt * cell.nominal();
            if (amount == 0) return true;
        }
        return false;
    }

    // Добавление одной купюры
    private void addBanknote(Banknote banknote) {
        boolean success = cells.get(banknote.nominal()).addBanknote(banknote);
        if (!success) {
            undefinedBanknotes.add(banknote);
        }
    }

    // Возврат не добавленных купюр
    private List<Banknote> getUndefinedBanknotes() {
        List<Banknote> result = new ArrayList<>(undefinedBanknotes);
        undefinedBanknotes.clear();
        return result;
    }
}

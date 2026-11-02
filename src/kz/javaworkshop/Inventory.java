package kz.javaworkshop;

import java.util.Arrays;

public class Inventory {
    private final Item[] items;
    private int size;
    private final int maxWeightGrams;

    public Inventory(int capacity, int maxWeightGrams) {
        if (capacity < 1 || capacity > 20) {
            throw new IllegalArgumentException("Количество ячеек должно быть от 1 до 20");
        }

        if (maxWeightGrams < 1 || maxWeightGrams > 100000) {
            throw new IllegalArgumentException("Недопустимый лимит массы");
        }

        this.items = new Item[capacity];
        this.size = 0;
        this.maxWeightGrams = maxWeightGrams;
    }

    public boolean add(Item item) {
        if (item == null) {
            return false;
        }

        if (size >= items.length) {
            return false;
        }

        if (totalWeightGrams() + item.getWeightGrams() > maxWeightGrams) {
            return false;
        }

        items[size] = item;
        size++;

        return true;
    }

    public Item remove(int index) {
        if (index < 0 || index >= size) {
            return null;
        }

        Item removed = items[index];

        for (int i = index; i < size - 1; i++) {
            items[i] = items[i + 1];
        }

        size--;
        items[size] = null;

        return removed;
    }

    public Item get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }

        return items[index];
    }

    public int size() {
        return size;
    }

    public int totalWeightGrams() {
        int sum = 0;

        for (int i = 0; i < size; i++) {
            sum += items[i].getWeightGrams();
        }

        return sum;
    }

    public long totalValue() {
        long sum = 0;

        for (int i = 0; i < size; i++) {
            sum += items[i].getValue();
        }

        return sum;
    }

    public Item[] snapshot() {
        return Arrays.copyOf(items, size);
    }

    public int getMaxWeightGrams() {
        return maxWeightGrams;
    }
}

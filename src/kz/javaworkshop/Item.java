package kz.javaworkshop;

/**
 * Абстрактный предмет: хранит общие данные (имя, масса, стоимость)
 * и их проверки. Вид предмета определяется подклассом.
 */
public abstract class Item {
    private final String name;
    private final int weightGrams;
    private final int value;

    protected Item(String name, int weightGrams, int value) {
        if (name == null || name.strip().isEmpty()) {
            throw new IllegalArgumentException("Имя предмета пусто");
        }

        name = name.strip();

        if (name.length() > 40) {
            throw new IllegalArgumentException("Имя предмета слишком длинное");
        }

        if (weightGrams < 1 || weightGrams > 100000) {
            throw new IllegalArgumentException("Недопустимая масса");
        }

        if (value < 0 || value > 1000000) {
            throw new IllegalArgumentException("Недопустимая стоимость");
        }

        this.name = name;
        this.weightGrams = weightGrams;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public int getWeightGrams() {
        return weightGrams;
    }

    public int getValue() {
        return value;
    }

    /** Описание предмета; у каждого вида своё. */
    public abstract String describe();

    /** Вклад предмета в атаку героя; по умолчанию 0. */
    public int attackBonus() {
        return 0;
    }

    /** Вклад предмета в защиту героя; по умолчанию 0. */
    public int defenceBonus() {
        return 0;
    }

    /** Общая часть описания: масса и стоимость. */
    protected String commonPart() {
        return weightGrams + " г; стоимость " + value;
    }
}

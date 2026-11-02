package kz.javaworkshop;

public class Armor extends Item {
    private final int defence;

    public Armor(String name, int weightGrams, int value, int defence) {
        super(name, weightGrams, value);
        if (defence < 1 || defence > 100) {
            throw new IllegalArgumentException("Защита: 1-100");
        }
        this.defence = defence;
    }

    @Override
    public int defenceBonus() {
        return defence;
    }

    @Override
    public String describe() {
        return getName() + ": броня, защита " + defence + "; " + commonPart();
    }
}

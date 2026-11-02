package kz.javaworkshop;

public class Weapon extends Item {
    private final int damage;

    public Weapon(String name, int weightGrams, int value, int damage) {
        super(name, weightGrams, value);
        if (damage < 1 || damage > 100) {
            throw new IllegalArgumentException("Урон: 1-100");
        }
        this.damage = damage;
    }

    @Override
    public int attackBonus() {
        return damage;
    }

    @Override
    public String describe() {
        return getName() + ": оружие, урон " + damage + "; " + commonPart();
    }
}

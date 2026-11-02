package kz.javaworkshop;

public class Potion extends Item implements Usable {
    private final int healing;

    public Potion(String name, int weightGrams, int value, int healing) {
        super(name, weightGrams, value);
        if (healing < 1 || healing > 100) {
            throw new IllegalArgumentException("Лечение: 1-100");
        }
        this.healing = healing;
    }

    @Override
    public void use(Hero hero) {
        hero.heal(healing);
    }

    @Override
    public String describe() {
        return getName() + ": зелье, лечение " + healing + "; " + commonPart();
    }
}

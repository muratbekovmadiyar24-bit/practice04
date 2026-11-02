package kz.javaworkshop;

public class Food extends Item implements Usable {
    private final int healing;

    public Food(String name, int weightGrams, int value, int healing) {
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
        return getName() + ": еда, лечение " + healing + "; " + commonPart();
    }
}

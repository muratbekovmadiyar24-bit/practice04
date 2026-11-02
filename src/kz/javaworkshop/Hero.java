package kz.javaworkshop;

public class Hero {
    private final String name;
    private final Inventory inventory;
    private final int maxHp;
    private int hp;

    public Hero(String name, int capacity, int maxWeightGrams, int maxHp) {
        if (name == null || name.strip().isEmpty()) {
            throw new IllegalArgumentException("Имя героя пусто");
        }

        name = name.strip();

        if (name.length() > 40) {
            throw new IllegalArgumentException("Имя героя слишком длинное");
        }

        if (maxHp < 1 || maxHp > 1000) {
            throw new IllegalArgumentException("Здоровье должно быть от 1 до 1000");
        }

        this.name = name;
        this.inventory = new Inventory(capacity, maxWeightGrams);
        this.maxHp = maxHp;
        this.hp = maxHp;
    }

    public String getName() {
        return name;
    }

    public int getHp() {
        return hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public boolean pickUp(Item item) {
        return inventory.add(item);
    }

    public int itemCount() {
        return inventory.size();
    }

    public Item[] items() {
        return inventory.snapshot();
    }

    public int totalWeight() {
        return inventory.totalWeightGrams();
    }

    public long totalValue() {
        return inventory.totalValue();
    }

    public int attackPower() {
        int sum = 0;
        for (Item item : inventory.snapshot()) {
            sum += item.attackBonus();
        }
        return sum;
    }

    public int defencePower() {
        int sum = 0;
        for (Item item : inventory.snapshot()) {
            sum += item.defenceBonus();
        }
        return sum;
    }

    /** Удар 0-1000. Возвращает фактическую потерю здоровья. */
    public int takeHit(int damage) {
        if (damage < 0 || damage > 1000) {
            throw new IllegalArgumentException("Удар должен быть от 0 до 1000");
        }
        int loss = Math.max(0, damage - defencePower());
        loss = Math.min(loss, hp);
        hp -= loss;
        return loss;
    }

    public void heal(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Лечение не может быть отрицательным");
        }
        hp = Math.min(maxHp, hp + amount);
    }

    /** Использует предмет-Usable по индексу и удаляет его из рюкзака. */
    public boolean useItem(int index) {
        Item item = inventory.get(index);
        if (item instanceof Usable usable) {
            usable.use(this);
            inventory.remove(index);
            return true;
        }
        return false;
    }

    public String summary() {
        int n = itemCount();
        return name + ": HP " + hp + "/" + maxHp
                + ", атака " + attackPower()
                + ", защита " + defencePower()
                + ", " + n + " " + plural(n)
                + ", " + totalWeight() + " г"
                + ", " + totalValue() + " монет";
    }

    private static String plural(int n) {
        int mod10 = n % 10;
        int mod100 = n % 100;
        if (mod10 == 1 && mod100 != 11) {
            return "предмет";
        }
        if (mod10 >= 2 && mod10 <= 4 && (mod100 < 12 || mod100 > 14)) {
            return "предмета";
        }
        return "предметов";
    }
}

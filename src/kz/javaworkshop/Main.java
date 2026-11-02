package kz.javaworkshop;

public class Main {

    public static void main(String[] args) {
        System.out.println("=== Шаг 6. Контрольный проход (вариант А · Воин) ===");
        controlRun();
        System.out.println();
        System.out.println("=== Шаг 7. Проверка границ ===");
        boundaryChecks();
    }

    // ---------- Шаг 6 ----------

    private static void controlRun() {
        Hero aliya = new Hero("Алия", 4, 6000, 120);
        Item sword = new Weapon("Меч", 2500, 100, 12);
        Item shield = new Armor("Щит", 3000, 80, 8);
        Item potion = new Potion("Зелье", 500, 20, 30);

        // 1. Подбор и вывод рюкзака
        pickUp(aliya, sword);
        pickUp(aliya, shield);
        pickUp(aliya, potion);
        printItems(aliya);
        System.out.println(aliya.summary());

        // 2. Удар 50
        int loss = aliya.takeHit(50);
        System.out.println("Алия получает удар 50, потеряно HP: " + loss);
        System.out.println(aliya.summary());

        // 3. Использование оружия
        System.out.println("Алия использует предмет 0: " + aliya.useItem(0));

        // 4. Использование зелья и повтор
        System.out.println("Алия использует предмет 2: " + aliya.useItem(2));
        System.out.println("Алия использует предмет 2: " + aliya.useItem(2));
        System.out.println(aliya.summary());

        // 5. Данияр
        Hero daniyar = new Hero("Данияр", 2, 6000, 100);
        pickUp(daniyar, new Weapon("Факел", 200, 10, 3));
        pickUp(daniyar, new Food("Хлеб", 300, 5, 10));
        printItems(daniyar);
        int daniyarLoss = daniyar.takeHit(5);
        System.out.println("Данияр получает удар 5, потеряно HP: " + daniyarLoss);
        System.out.println("Данияр использует предмет 1: " + daniyar.useItem(1));
        System.out.println(daniyar.summary());
    }

    private static void pickUp(Hero hero, Item item) {
        String itemName = item.describe().split(":")[0];
        System.out.println(hero.getName() + " подбирает " + itemName + ": " + hero.pickUp(item));
    }

    private static void printItems(Hero hero) {
        Item[] items = hero.items();
        for (int i = 0; i < items.length; i++) {
            System.out.println(" " + i + ". " + items[i].describe());
        }
    }

    // ---------- Шаг 7 ----------

    private static int passed = 0;
    private static int failed = 0;

    private static void boundaryChecks() {
        // 1. Пустой рюкзак
        Hero empty = new Hero("Пустой", 3, 5000, 50);
        check("useItem(0) на пустом рюкзаке -> false", !empty.useItem(0));
        check("useItem(-1) на пустом рюкзаке -> false", !empty.useItem(-1));
        check("состояние прежнее (0 предметов, HP 50)",
                empty.itemCount() == 0 && empty.getHp() == 50);

        // 2. Зелье при полном здоровье
        Hero full = new Hero("Сытый", 3, 5000, 100);
        full.pickUp(new Potion("Зелье", 500, 20, 30));
        check("зелье при полном здоровье -> true", full.useItem(0));
        check("зелье удалено, HP = максимум", full.itemCount() == 0 && full.getHp() == 100);

        // 3. Удар слабее защиты
        Hero guarded = new Hero("Защитник", 3, 5000, 100);
        guarded.pickUp(new Armor("Щит", 3000, 80, 8));
        int weak = guarded.takeHit(5);
        check("удар 5 при защите 8: потеря 0, HP прежнее", weak == 0 && guarded.getHp() == 100);

        // 4. Удар 1000
        int big = guarded.takeHit(1000);
        check("удар 1000: HP = 0, возвращена фактическая потеря (100)",
                guarded.getHp() == 0 && big == 100);

        // 5. Лечение героя с нулевым здоровьем
        guarded.heal(10);
        check("лечение при HP 0: здоровье растёт до 10", guarded.getHp() == 10);

        // 6. useItem() для брони
        Hero wearer = new Hero("Носитель", 3, 5000, 100);
        wearer.pickUp(new Armor("Куртка", 2000, 80, 4));
        String before = snapshotText(wearer);
        boolean used = wearer.useItem(0);
        check("useItem() для брони -> false, снимок совпадает",
                !used && before.equals(snapshotText(wearer)));

        // 7. Значения подклассов 0 и 101
        expectIae("Weapon урон 0", () -> new Weapon("Нож", 100, 1, 0));
        expectIae("Weapon урон 101", () -> new Weapon("Нож", 100, 1, 101));
        expectIae("Armor защита 0", () -> new Armor("Щит", 100, 1, 0));
        expectIae("Armor защита 101", () -> new Armor("Щит", 100, 1, 101));
        expectIae("Potion лечение 0", () -> new Potion("Зелье", 100, 1, 0));
        expectIae("Potion лечение 101", () -> new Potion("Зелье", 100, 1, 101));
        expectIae("Food лечение 0", () -> new Food("Хлеб", 100, 1, 0));
        expectIae("Food лечение 101", () -> new Food("Хлеб", 100, 1, 101));

        // 8. Здоровье героя и удар
        expectIae("maxHp 0", () -> new Hero("Тест", 2, 1000, 0));
        expectIae("maxHp 1001", () -> new Hero("Тест", 2, 1000, 1001));
        expectIae("удар -1", () -> new Hero("Тест", 2, 1000, 10).takeHit(-1));

        // 9. Имя из пробелов в Food
        expectIae("имя из пробелов в Food", () -> new Food("   ", 100, 1, 10));

        System.out.println("Итого: пройдено " + passed + ", не пройдено " + failed);
    }

    private static String snapshotText(Hero hero) {
        StringBuilder sb = new StringBuilder();
        for (Item item : hero.items()) {
            sb.append(item.describe()).append('|');
        }
        return sb.toString();
    }

    private static void check(String label, boolean ok) {
        if (ok) {
            passed++;
        } else {
            failed++;
        }
        System.out.println((ok ? "[OK]   " : "[FAIL] ") + label);
    }

    private static void expectIae(String label, Runnable action) {
        try {
            action.run();
            failed++;
            System.out.println("[FAIL] " + label + ": исключение не выброшено");
        } catch (IllegalArgumentException e) {
            passed++;
            System.out.println("[OK]   " + label + " -> IllegalArgumentException: " + e.getMessage());
        }
    }
}

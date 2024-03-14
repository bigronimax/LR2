import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Menu menu = new Menu();
        ArrayList<String> heroes;
        int money = 50;
        int heroesCost = 0;

        while (true) {
            Scanner sc = new Scanner(System.in);
            System.out.println(menu);
            System.out.println("Казна предоставила вам " + money + " крон. Выберите себе героев (через запятую или пробел): ");
            String heroNames = sc.nextLine();
            if (heroNames.contains(",")) {
                heroes = new ArrayList<>(Arrays.asList(heroNames.split(", ")));
            }
            else {
                heroes = new ArrayList<>(Arrays.asList(heroNames.split(" ")));
            }

            for (int i = heroes.size() - 1; i > -1; i--) {
                if (!menu.getMenu().containsKey(heroes.get(i))) {
                    System.out.println("Вы обидели одного из героев, он не вступает в вашу команду");
                    heroes.remove(i);

                }
            }
            if (heroes.isEmpty()) {
                System.out.println("Вы не собрали команду, вы безнадежны");
                break;
            }
            for (String hero : heroes) {
                heroesCost += menu.getMenu().get(hero).get("cost");
            }
            if (heroesCost > money) {
                System.out.println("Вы чересчур расточительны. Убедитесь, что вы собираете команду на имеющиеся деньги без кредитов");
                heroesCost = 0;
                continue;
            }

            break;
        }

        Battlefield field = new Battlefield(6, menu, heroes, 3, 3, true);
        HashMap<Character, Enemy> enemiesObjects = field.getEnemiesObjects();
        HashMap<Character, Hero> heroesObjects = field.getHeroesObjects();
        HashMap<Character, Beast> beastsObjects = field.getBeastsObjects();
        System.out.println(field);

        while (field.getHeroesCount() != 0 && field.getEnemyCount() + field.getBeastsCount() != 0) {
            while (field.getMoves() + field.getAttacks() != 0 && field.getEnemyCount() + field.getBeastsCount() != 0) {
                Scanner sc_hero = new Scanner(System.in);
                System.out.println("Выберите героя (номер)");
                Character hero_sign = sc_hero.nextLine().charAt(0);
                Hero hero = heroesObjects.get(hero_sign);
                System.out.println("move, attack, none?");
                Scanner sc_action = new Scanner(System.in);
                String action = sc_action.nextLine();
                if (action.equals("move") && !hero.hasMoved) {
                    while (!hero.hasMoved) {
                        ArrayList<String> crd;
                        System.out.println("Введите координаты клетки (через запятую или пробел): ");
                        Scanner sc_crd = new Scanner(System.in);
                        String crd_str = sc_crd.nextLine();
                        if (crd_str.contains(",")) {
                            crd = new ArrayList<>(Arrays.asList(crd_str.split(", ")));
                        } else {
                            crd = new ArrayList<>(Arrays.asList(crd_str.split(" ")));
                        }
                        int xPos = Integer.parseInt(crd.get(0));
                        int yPos = Integer.parseInt(crd.get(1));
                        hero.move(xPos, yPos);
                    }
                }
                else if (action.equals("none")) {
                    hero.none();
                }
                else if (action.equals("attack") && !hero.hasAttacked) {
                    System.out.println("Введите букву врага: ");
                    Scanner target = new Scanner(System.in);
                    char target_sign = target.nextLine().charAt(0);
                    String targetName;
                    System.out.println((int) target_sign);
                    if (target_sign >= 65 && target_sign <= 90) {
                        targetName = beastsObjects.get(target_sign).name;
                        if (hero.attack(beastsObjects.get(target_sign))) {
                            System.out.println("Герой: " + hero.name + " атаковал " + targetName);
                        }
                        else {
                            System.out.println("Фигово атакуешь");
                        }
                    }
                    else if (target_sign >= 97 && target_sign <= 122) {
                        targetName = enemiesObjects.get(target_sign).name;
                        if (hero.attack(enemiesObjects.get(target_sign))) {
                            System.out.println("Герой: " + hero.name + " атаковал " + targetName);
                        }
                        else {
                            System.out.println("Фигово атакуешь");
                        }
                    }
                    else {
                        System.out.println("Бьешь не тех");
                    }

                }
                else {
                    System.out.println("Нельзя");
                }
                if (field.getMoves() + field.getAttacks() != 0)
                    System.out.println(field);
            }
            if (field.getEnemyCount() + field.getBeastsCount() == 0)
                break;

            boolean hasAttacked;
            for (Character keyEnemy: enemiesObjects.keySet()) {
                hasAttacked = false;
                for (Character keyHero: heroesObjects.keySet()) {
                    String heroName = heroesObjects.get(keyHero).name;
                    if (enemiesObjects.get(keyEnemy).attack(heroesObjects.get(keyHero))) {
                        hasAttacked = true;
                        System.out.println("Враг: " + enemiesObjects.get(keyEnemy).name + " атаковал: " + heroName);
                        break;
                    }

                }

                if (!hasAttacked) {
                    enemiesObjects.get(keyEnemy).move();
                }
            }
            for (Character keyBeast: beastsObjects.keySet()) {
                hasAttacked = false;
                for (Character keyUnit: heroesObjects.keySet()) {
                    String heroName = heroesObjects.get(keyUnit).name;
                    if (beastsObjects.get(keyBeast).attack(heroesObjects.get(keyUnit))) {
                        hasAttacked = true;
                        System.out.println("Зверь: " + beastsObjects.get(keyBeast).name + " атаковал: " + heroName);
                        break;
                    }

                }

                if (!hasAttacked) {
                    beastsObjects.get(keyBeast).move();
                }
            }
            field.setHeroes();
            System.out.println(field);

        }
        if (field.getEnemyCount() + field.getBeastsCount() == 0)
            System.out.println("Ура, ура, ура!!! Победа!!!");
        else if (field.getHeroesCount() + field.getEnemyCount() == 0)
            System.out.println("Неожиданно... Природа победила!?");
        else if (field.getHeroesCount() == 0)
            System.out.println("Хнык... Хнык... Хнык... Поражение...");


    }

}
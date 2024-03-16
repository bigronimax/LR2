import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Menu menu = new Menu();
        String PATH = "C://Users/ronim/Downloads/maps/";
        ArrayList<String> heroes;
        ArrayList<ArrayList<Character>> map = new ArrayList<>();
        int money = 50;
        int heroesCost = 0;

        Scanner scanner = new Scanner(System.in);
        System.out.println("1. Начать игру\n2. Загрузить карту");
        int input = scanner.nextInt();
        if (input == 2) {
            while (true) {
                System.out.println("Выберите карту (полное имя файла):");
                File dir = new File(PATH);
                if (dir.isDirectory()) {
                    for (File item : dir.listFiles()) {
                        System.out.println(item.getName());
                    }
                }
                scanner = new Scanner(System.in);
                String mapName = scanner.nextLine();
                File mapFile = new File(PATH + mapName);

                try {

                    Scanner scanFile = new Scanner(mapFile);

                    int indRow = 0;
                    while (scanFile.hasNextLine()) {
                        String s = scanFile.nextLine();
                        map.add(new ArrayList<>());
                        for (int i = 0; i < s.length(); i++) {
                            map.get(indRow).add(s.charAt(i));
                        }
                        indRow++;
                    }
                    scanFile.close();
                    break;

                } catch (FileNotFoundException e) {
                    System.out.println("Введите правильное имя файла!");
                }
            }
        }

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

        Battlefield field = new Battlefield(10, menu, heroes, null, 1, 5, true);
        HashMap<Character, Enemy> enemiesObjects = field.getEnemiesObjects();
        HashMap<Character, Hero> heroesObjects = field.getHeroesObjects();
        HashMap<Character, Beast> beastsObjects = field.getBeastsObjects();
        System.out.println(field);

        while (field.getHeroesCount() + field.getEnemyCount() != 0 && field.getBeastsCount() + field.getHeroesCount() != 0 && field.getBeastsCount() + field.getEnemyCount() != 0) {
            while (field.getMoves() + field.getAttacks() != 0 && field.getEnemyCount() + field.getBeastsCount() != 0 && !heroesObjects.isEmpty()) {
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
                Set<Character> unionSet = new HashSet<>();
                unionSet.addAll(heroesObjects.keySet());
                unionSet.addAll(beastsObjects.keySet());
                ArrayList<Character> keys = new ArrayList<>(unionSet);
                while(!keys.isEmpty()) {
                    Unit unit;
                    int ind = (int) (Math.random() * keys.size());
                    if (heroesObjects.containsKey(keys.get(ind)))
                        unit = heroesObjects.get(keys.get(ind));
                    else
                        unit = beastsObjects.get(keys.get(ind));
                    if (enemiesObjects.get(keyEnemy).attack(unit)) {
                        hasAttacked = true;
                        System.out.println("Враг: " + enemiesObjects.get(keyEnemy).name + " атаковал: " + unit.name);
                        break;
                    }
                    else {
                        keys.remove(ind);
                    }

                }

                if (!hasAttacked) {
                    enemiesObjects.get(keyEnemy).move();
                }
            }
            for (Character keyBeast: beastsObjects.keySet()) {
                hasAttacked = false;
                Set<Character> unionSet = new HashSet<>();
                unionSet.addAll(heroesObjects.keySet());
                unionSet.addAll(enemiesObjects.keySet());
                ArrayList<Character> keys = new ArrayList<>(unionSet);
                while(!keys.isEmpty()) {
                    Unit unit;
                    int ind = (int) (Math.random() * keys.size());
                    if (heroesObjects.containsKey(keys.get(ind)))
                         unit = heroesObjects.get(keys.get(ind));
                    else
                         unit = enemiesObjects.get(keys.get(ind));
                    if (beastsObjects.get(keyBeast).attack(unit)) {
                        hasAttacked = true;
                        System.out.println("Зверь: " + beastsObjects.get(keyBeast).name + " атаковал: " + unit.name);
                        break;
                    }
                    else {
                        keys.remove(ind);
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
            System.out.println("Победа героев!");
        else if (field.getHeroesCount() + field.getEnemyCount() == 0)
            System.out.println("Победа зверей!");
        else if (field.getHeroesCount() + field.getBeastsCount() == 0)
            System.out.println("Победа врагов!");


    }

}
import java.io.*;
import java.util.*;

public class Main {
    static String SAVE_PATH = "C://Users/ronim/Downloads/save.txt";
    static Menu menu = new Menu();
    static Town town = new Town();
    static Scanner scanner;
    static String PATH = "C://Users/ronim/Downloads/maps/";
    static ArrayList<String> heroesNames;
    static ArrayList<String> buildingsNames;
    static ArrayList<Hero> heroes = new ArrayList<>();
    static HashMap<String, Building> buildingsUp = new HashMap<>();
    static HashMap<Character, Hero> heroesObjects = new HashMap<>();
    static ArrayList<ArrayList<Character>> map = new ArrayList<>();
    static HashMap<Character, Double> newSignsHash = new HashMap<>();
    static Battlefield field;
    static boolean mapChoice = false;
    static Market market;
    static String marketString = "";
    static Academy academy;
    static String academyString = "";
    static ArrayList<WorkShop> workShopsArray = new ArrayList<>();

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        System.out.println("1. Начать новую игру\n2. Загрузить игру\n3. Загрузить карту\n4. Выйти");
        scanner = new Scanner(System.in);
        int input = scanner.nextInt();
        if (input == 2)
            load();
        else if (input == 3)
            loadMap();
        else if (input == 4)
            System.exit(0);

        while(true) {


            if (academy != null) {
                academyString = "5. Создать юнита\n";
            }
            if (market != null) {
                marketString = "6. Рынок";
            }

            System.out.println("1. Сражаться\n2. Нанять героев\n3. Здания\n4. Выйти\n" + academyString + marketString);
            scanner = new Scanner(System.in);
            input = scanner.nextInt();
            if (input == 1) {
                battle();
                if (market != null)
                    market.course();
                if (!workShopsArray.isEmpty()) {
                    for (WorkShop w : workShopsArray) {
                        w.upgrade();
                    }
                }
            }
            else if (input == 2)
                heroesChoice();
            else if (input == 3)
                buildings();
            else if (input == 4) {
                save();
                System.exit(0);
            }
            else if (input == 5 && academy != null) {
                System.out.println("Введите основные характеристики вашего героя: ");
                System.out.println("Имя: ");
                scanner = new Scanner(System.in);
                String name = scanner.nextLine();
                System.out.println("Здоровье: ");
                scanner = new Scanner(System.in);
                int hp = scanner.nextInt();
                System.out.println("Урон: ");
                scanner = new Scanner(System.in);
                int damage = scanner.nextInt();
                System.out.println("Дальность атаки: ");
                scanner = new Scanner(System.in);
                int attackRange = scanner.nextInt();
                System.out.println("Броня: ");
                scanner = new Scanner(System.in);
                int armor = scanner.nextInt();
                System.out.println("Передвижение: ");
                scanner = new Scanner(System.in);
                int movement = scanner.nextInt();
                if (academy.createHero(name, hp, damage, attackRange, armor, movement))
                    System.out.println("Ваш герой создан!");
                else
                    System.out.println("Недостаточно денег для создания...");
            }
            else if (input == 6 && market != null) {
                System.out.println(market);
                System.out.println("Сейчас у вас:\nwood: " + town.getWood() + ", " + "rock: " + town.getRock());
                scanner = new Scanner(System.in);
                input = scanner.nextInt();
                System.out.println("Введите количество ресурса для обмена: ");
                scanner = new Scanner(System.in);
                int amount = scanner.nextInt();
                if (input == 1)
                    market.exchangeWood(amount);
                else if (input == 2)
                    market.exchangeRock(amount);
                System.out.println("Сейчас у вас:\nwood: " + town.getWood() + ", " + "rock: " + town.getRock());
            }
        }
    }

    public static void save() throws IOException {
        Saving savedGame = new Saving(academy, market, workShopsArray, buildingsUp, heroes, menu.getMenu(), menu.getNames(), menu.getMoney(), town.getWood(), town.getRock());
        FileOutputStream outputStream = new FileOutputStream(SAVE_PATH);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(savedGame);
        objectOutputStream.close();
    }

    public static void load() throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(SAVE_PATH);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        Saving savedGame = (Saving) objectInputStream.readObject();
        academy = savedGame.getAcademy();
        market = savedGame.getMarket();
        workShopsArray = savedGame.getWorkShopsArray();
        buildingsUp = savedGame.getBuildingsUp();
        heroes = savedGame.getHeroes();
        menu.setMenu(savedGame.getMenu());
        menu.setNames(savedGame.getNames());
        menu.setMoney(savedGame.getMoney());
        town.setWood(savedGame.getWood());
        town.setRock(savedGame.getRock());
    }

    public static void buildings() {
        while (true) {
            scanner = new Scanner(System.in);

            if (!buildingsUp.isEmpty()) {
                System.out.println("Ваши здания: ");
                for (String name : buildingsUp.keySet()) {
                    System.out.println(buildingsUp.get(name));
                }
            }
            if (!workShopsArray.isEmpty()) {
                for (WorkShop w : workShopsArray) {
                    System.out.println(w);
                }
            }
            if (market != null) {
                System.out.println("market");
            }
            if (academy != null) {
                System.out.println("academy");
            }

            System.out.println(town);
            int buildingsWoodCost = 0;
            int buildingsRockCost = 0;
            if (!town.obtainableBuildings.isEmpty()) {
                System.out.println("Выберите здания для покупки или улучшения (через запятую или пробел): ");
                String buildingsString = scanner.nextLine();
                if (buildingsString.contains(","))
                    buildingsNames = new ArrayList<>(Arrays.asList(buildingsString.split(", ")));
                else if (buildingsString.isEmpty()) {
                    System.out.println("Не хотите, как хотите...");
                    break;
                }
                else
                    buildingsNames = new ArrayList<>(Arrays.asList(buildingsString.split(" ")));

                ArrayList<String> buildingsTmp = new ArrayList<>(buildingsNames);
                for (String name: buildingsTmp) {
                    if (!town.getObtainableBuildings().contains(name)) {
                        System.out.println("Вы обидели владельца здания, поэтому его не удалось купить/улучшить");
                        buildingsNames.remove(name);
                    }
                }

                if (buildingsNames.isEmpty()) {
                    System.out.println("Не хотите, как хотите...");
                    break;
                }

                for (String name : buildingsNames) {
                    buildingsWoodCost += town.getBuildingsPrices().get(name).get("wood");
                    buildingsRockCost += town.getBuildingsPrices().get(name).get("rock");
                }
                if (buildingsWoodCost > town.getWood() || buildingsRockCost > town.getRock()) {
                    System.out.println("Вы чересчур расточительны. Убедитесь, что вы тратитесь на имеющиеся ресурсы без кредитов");
                    continue;
                }
                town.setWood(town.getWood() - buildingsWoodCost);
                town.setRock(town.getRock() - buildingsRockCost);

                for (String name: buildingsNames) {
                    if (Objects.equals(name, "tavern")) {
                        System.out.println("Вы выбрали таверну, хотите прокачать перемещение или снизить штрафы?");
                        System.out.println("1. Перемещение\n2. Штрафы");
                        scanner = new Scanner(System.in);
                        int input = scanner.nextInt();
                        if (input == 1) {
                            buildingsNames.remove("tavern");
                            buildingsNames.add("tavern_movement");
                        }
                        else if (input == 2) {
                            buildingsNames.remove("tavern");
                            buildingsNames.add("tavern_obstacles");
                        }
                    }
                }

                for (String name: buildingsNames) {

                    if (buildingsUp.containsKey(name)) {
                        buildingsUp.get(name).levelUp();
                        if (buildingsUp.get(name).getLevel() == 4)
                            town.getBuildingsPrices().remove(name);
                    }
                    else if (town.getBuildingsUp().contains(name)) {
                        Building b = new Building(name, heroes);
                        buildingsUp.put(name, b);
                    }
                    else if (Objects.equals(name, "market")) {
                        market = new Market(town);
                        town.getBuildingsPrices().remove("market");
                    }
                    else if (Objects.equals(name, "academy")) {
                        academy = new Academy(menu);
                        town.getBuildingsPrices().remove("academy");
                    }
                    else if (Objects.equals(name, "workshop")) {
                        if (workShopsArray.isEmpty() || workShopsArray.get(workShopsArray.size()-1).getLevel() == 4)
                            if (workShopsArray.size() < 4)
                                workShopsArray.add(new WorkShop(menu));
                            else
                                town.getBuildingsPrices().remove("workshop");
                        else
                            workShopsArray.get(workShopsArray.size()-1).levelUp();
                    }
                }
                buildingsNames.clear();

            }
            else {
                break;
            }
        }
    }

    public static void battle() {

        if (mapChoice)
            field = new Battlefield(map.size(), menu, town, heroes, map, newSignsHash, 1, 1, false);
        else
            field = new Battlefield(10, menu, town, heroes, null, newSignsHash, 1, 1, false);
        heroesObjects = field.getHeroesObjects();
        HashMap<Character, Enemy> enemiesObjects = field.getEnemiesObjects();
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
                        hero.move(xPos, yPos, field);
                    }
                }
                else if (action.equals("none")) {
                    hero.none(field);
                }
                else if (action.equals("attack") && !hero.hasAttacked) {
                    System.out.println("Введите букву врага: ");
                    Scanner target = new Scanner(System.in);
                    char target_sign = target.nextLine().charAt(0);
                    String targetName;
                    if (target_sign >= 65 && target_sign <= 90) {
                        targetName = beastsObjects.get(target_sign).name;
                        if (hero.attack(beastsObjects.get(target_sign), field)) {
                            System.out.println("Герой: " + hero.name + " атаковал " + targetName);
                        }
                        else {
                            System.out.println("Фигово атакуешь");
                        }
                    }
                    else if (target_sign >= 97 && target_sign <= 122) {
                        targetName = enemiesObjects.get(target_sign).name;
                        if (hero.attack(enemiesObjects.get(target_sign), field)) {
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
                    if (enemiesObjects.get(keyEnemy).attack(unit, field)) {
                        hasAttacked = true;
                        System.out.println("Враг: " + enemiesObjects.get(keyEnemy).name + " атаковал: " + unit.name);
                        break;
                    }
                    else {
                        keys.remove(ind);
                    }

                }

                if (!hasAttacked) {
                    enemiesObjects.get(keyEnemy).move(field);
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
                    if (beastsObjects.get(keyBeast).attack(unit, field)) {
                        hasAttacked = true;
                        System.out.println("Зверь: " + beastsObjects.get(keyBeast).name + " атаковал: " + unit.name);
                        break;
                    }
                    else {
                        keys.remove(ind);
                    }

                }

                if (!hasAttacked) {
                    beastsObjects.get(keyBeast).move(field);
                }
            }
            field.setHeroes();
            System.out.println(field);

        }
        if (field.getEnemyCount() + field.getBeastsCount() == 0) {
            System.out.println("Победа героев!");
            menu.setMoney(menu.getMoney() + ((int) (Math.random() * 5) + 1));
        }
        else if (field.getHeroesCount() + field.getEnemyCount() == 0)
            System.out.println("Победа зверей!");
        else if (field.getHeroesCount() + field.getBeastsCount() == 0)
            System.out.println("Победа врагов!");

    }

    public static void heroesChoice() {
        while (true) {
            Scanner sc = new Scanner(System.in);
            int heroesCost = 0;
            System.out.println(menu);
            if (!menu.getObtainableHeroes().isEmpty()) {
                System.out.println("Казна предоставила вам " + menu.getMoney() + " крон. Выберите себе героев (через запятую или пробел): ");
                String heroesString = sc.nextLine();
                if (heroesString.contains(","))
                    heroesNames = new ArrayList<>(Arrays.asList(heroesString.split(", ")));
                else if (heroesString.isEmpty()) {
                    System.out.println("Не хотите, как хотите...");
                    break;
                }
                else
                    heroesNames = new ArrayList<>(Arrays.asList(heroesString.split(" ")));

                ArrayList<String> heroesTmp = new ArrayList<>(heroesNames);
                for (String name: heroesTmp) {
                    if (!menu.getObtainableHeroes().contains(name)) {
                        System.out.println("Вы обидели одного из героев, он не вступает в вашу команду");
                        heroesNames.remove(name);
                    }
                }
                if (heroesNames.isEmpty()) {
                    System.out.println("Не хотите, как хотите...");
                    break;
                }
                for (String hero : heroesNames) {
                    heroesCost += menu.getMenu().get(hero).get("cost");
                }
                if (heroesCost > menu.getMoney()) {
                    System.out.println("Вы чересчур расточительны. Убедитесь, что вы собираете команду на имеющиеся деньги без кредитов");
                    continue;
                }
                menu.setMoney(menu.getMoney() - heroesCost);

                for (String name: heroesNames) {
                    Hero hero = new Hero(name, menu);
                    heroes.add(hero);
                }
                for (String name: buildingsUp.keySet()) {
                    buildingsUp.get(name).setHeroes(heroes);
                }
                heroesNames.clear();

                break;
            }
            else {
                break;
            }
        }
    }

    public static void loadMap() {
        mapChoice = true;
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
                int size = scanFile.nextInt();
                scanFile.nextLine();
                for (int i = 0; i < size; i++) {
                    String s = scanFile.nextLine();
                    map.add(new ArrayList<>());
                    for (int j = 0; j < s.length(); j++) {
                        map.get(indRow).add(s.charAt(j));
                    }
                    indRow++;
                }
                while(scanFile.hasNextLine())
                {
                    String data = scanFile.nextLine();
                    Character sign = data.charAt(0);
                    Double ratio = Double.parseDouble(data.substring(2));
                    newSignsHash.put(sign, ratio);

                }
                scanFile.close();
                break;

            } catch (FileNotFoundException e) {
                System.out.println("Введите правильное имя файла!");
            }
        }
    }

}
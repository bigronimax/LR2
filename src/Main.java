import java.io.*;
import java.util.*;

public class Main {
    static String PATH = "C://Users/ronim/Downloads/maps/";
    static String SAVE_PATH = "C://Users/ronim/Downloads/save.txt";
    static String LOG_PATH = "C://Users/ronim/Downloads/logs/";
    static Menu menu = new Menu();
    static Town town = new Town();
    static Scanner scanner;
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
    static MagicAcademy magicAcademy;
    static String magicAcademyString = "";
    static ArrayList<WorkShop> workShopsArray = new ArrayList<>();
    static int battlesAmount = 0;

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
        for (File myFile : Objects.requireNonNull(new File(LOG_PATH).listFiles()))
            if (myFile.isFile()) myFile.delete();

        while(true) {


            if (academy != null) {
                academyString = "5. Создать юнита\n";
            }
            if (market != null) {
                marketString = "6. Рынок\n";
            }
            if (magicAcademy != null) {
                magicAcademyString = "7. Академия магов";
            }

            System.out.println("1. Сражаться\n2. Нанять героев\n3. Здания\n4. Выйти\n" + academyString + marketString + magicAcademyString);
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
            else if (input == 7 && magicAcademy != null) {
                if (menu.getMoney() >= menu.getMenu().get("Wizard").get("cost")) {
                    String name;
                    System.out.println("Отправьте одного из своих героев на обучение магии: ");
                    for (Hero hero: heroes) {
                        System.out.println(hero.name);
                    }
                    scanner = new Scanner(System.in);
                    name = scanner.nextLine();
                    if (!magicAcademy.createWizard(name))
                        System.out.println("Ваш отряд не понял, кому идти в академию. Никто не пошел.");

                }
                else {
                    System.out.println("Вы слишком бедны, чтобы пользоваться услугами академии. Необходимо минимум 20 крон");
                }

            }
        }
    }

    public static void checkBuffs() {
        for (Hero hero: field.getBuffsHash().keySet()) {
            field.getBuffsHash().replace(hero, field.getBuffsHash().get(hero) - 1);
            if (field.getBuffsHash().get(hero) == 0) {
                field.getBuffsHash().remove(hero);
                hero.setDefaultProps();
            }
        }
        for (Hero hero: field.getDebuffsHash().keySet()) {
            field.getDebuffsHash().replace(hero, field.getDebuffsHash().get(hero) - 1);
            if (field.getDebuffsHash().get(hero) == 0) {
                field.getDebuffsHash().remove(hero);
                hero.setDefaultProps();
            }
        }
    }

    public static void save() throws IOException {
        Saving savedGame = new Saving(academy, market, workShopsArray, magicAcademy, buildingsUp, heroes, menu.getMenu(), menu.getNames(), menu.getMoney(), town.getWood(), town.getRock());
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
        magicAcademy = savedGame.getMagicAcademy();
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
            if (magicAcademy != null)
                System.out.println(magicAcademy);

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
                    else if (Objects.equals(name, "magicAcademy")) {
                        if (magicAcademy != null) {
                            magicAcademy.levelUp();
                            if (magicAcademy.getLevel() == 4)
                                town.getBuildingsPrices().remove("magicAcademy");
                        }
                        else
                            magicAcademy = new MagicAcademy(menu, heroes);
                    }
                }
                buildingsNames.clear();

            }
            else {
                break;
            }
        }
    }

    public static void battle() throws IOException {

        if (mapChoice)
            field = new Battlefield(map.size(), menu, town, heroes, map, newSignsHash, 1, 1, false);
        else
            field = new Battlefield(10, menu, town, heroes, null, newSignsHash, 1, 1, false);
        heroesObjects = field.getHeroesObjects();
        HashMap<Character, Enemy> enemiesObjects = field.getEnemiesObjects();
        HashMap<Character, Beast> beastsObjects = field.getBeastsObjects();

        int movesAmount = 1;
        String log = "";
        String logInfo = "";
        battlesAmount++;
        String logName = battlesAmount + ".txt";
        System.out.println(field);
        File file = new File(LOG_PATH + logName);

        while (field.getHeroesCount() + field.getEnemyCount() != 0 && field.getBeastsCount() + field.getHeroesCount() != 0 && field.getBeastsCount() + field.getEnemyCount() != 0) {
            log += movesAmount + " ход:\n";
            while (field.getMoves() + field.getAttacks() != 0 && field.getEnemyCount() + field.getBeastsCount() != 0 && !heroesObjects.isEmpty()) {
                Scanner sc_hero = new Scanner(System.in);
                System.out.println("Выберите героя (номер)");
                Character hero_sign = sc_hero.nextLine().charAt(0);
                Hero hero = heroesObjects.get(hero_sign);
                if (!Objects.equals(hero.type, "wizards"))
                    System.out.println("move, attack, none?");
                else
                    System.out.println("move, buff, none?");
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
                        if (hero.move(xPos, yPos, field)) {
                            logInfo = "Герой: " + hero.name + " переместился на клетку: " + xPos + " " + yPos;
                            System.out.println(logInfo);
                            log += logInfo + "\n";
                        }
                    }
                }
                else if (action.equals("none")) {
                    hero.none(field);
                }
                else if (!Objects.equals(hero.type, "wizards") && action.equals("attack") && !hero.hasAttacked
                        || Objects.equals(hero.type, "wizards") && action.equals("buff") && !hero.hasAttacked) {
                    if (!Objects.equals(hero.type, "wizards"))
                        System.out.println("Введите букву врага: ");
                    else
                        System.out.println("Введите цифру союзника: ");
                    Scanner target = new Scanner(System.in);
                    char target_sign = target.nextLine().charAt(0);
                    String targetName;
                    int targetHp;
                    if (target_sign >= 65 && target_sign <= 90 && (!Objects.equals(hero.type, "wizards"))) {
                        targetName = beastsObjects.get(target_sign).name;
                        targetHp = beastsObjects.get(target_sign).hp;
                        if (hero.attack(beastsObjects.get(target_sign), field)) {
                            logInfo = "Герой: " + hero.name + " атаковал " + targetName;
                            System.out.println(logInfo);
                            log += logInfo + "\n";
                            if (targetHp - hero.damage <= 0) {
                                logInfo = targetName + " помер...";
                                System.out.println(logInfo);
                                log += logInfo + "\n";
                            }
                        }
                        else {
                            System.out.println("Фигово атакуешь");
                        }
                    }
                    else if (target_sign >= 97 && target_sign <= 122 && (!Objects.equals(hero.type, "wizards"))) {
                        targetName = enemiesObjects.get(target_sign).name;
                        targetHp = enemiesObjects.get(target_sign).hp;
                        if (hero.attack(enemiesObjects.get(target_sign), field)) {
                            logInfo = "Герой: " + hero.name + " атаковал " + targetName;
                            System.out.println(logInfo);
                            log += logInfo + "\n";
                            if (targetHp - hero.damage <= 0) {
                                logInfo = targetName + " помер...";
                                System.out.println(logInfo);
                                log += logInfo + "\n";
                            }
                        }
                        else {
                            System.out.println("Фигово атакуешь");
                        }
                    }
                    else if (target_sign >= 1 && target_sign <= 9 && (Objects.equals(hero.type, "wizards"))) {
                        targetName = heroesObjects.get(target_sign).name;
                        if (Objects.equals(hero.name, "Wizard")) {
                            if (hero.buff(heroesObjects.get(target_sign), field)) {
                                logInfo = "Герой: " + hero.name + " оказал поддержку " + targetName;
                                System.out.println(logInfo);
                                log += logInfo + "\n";
                            }
                            else {
                                System.out.println("Посох короткий...");
                            }
                        }
                        else if (Objects.equals(hero.name, "Necromancer")) {
                            int random = (int) (Math.random() * 100) + 1;
                            if (random < 50) {
                                if (hero.debuff(heroesObjects.get(target_sign), field)) {
                                    logInfo = "Герой: " + hero.name + " наложил проклятие " + targetName + ". Получить поддержку: >=50/100. Выпало: " + random;
                                    System.out.println(logInfo);
                                    log += logInfo + "\n";
                                }
                                else {
                                    System.out.println("Посох короткий...");
                                }
                            }
                            else {
                                if (hero.buff(heroesObjects.get(target_sign), field)) {
                                    logInfo = "Герой: " + hero.name + " оказал поддержку " + targetName + ". Получить поддержку: >=50/100. Выпало: " + random;
                                    System.out.println(logInfo);
                                    log += logInfo + "\n";
                                }
                                else {
                                    System.out.println("Посох короткий...");
                                }
                            }
                        }
                    }
                    else {
                        System.out.println("Нельзя");
                        System.out.println(target_sign >= 1);
                        System.out.println(target_sign <= 9);
                        System.out.println((Objects.equals(hero.type, "wizards")));
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
                    int unitHp = unit.hp;
                    if (enemiesObjects.get(keyEnemy).attack(unit, field)) {
                        hasAttacked = true;
                        logInfo = "Враг: " + enemiesObjects.get(keyEnemy).name + " атаковал: " + unit.name;
                        System.out.println(logInfo);
                        log += logInfo + "\n";
                        if (unitHp - enemiesObjects.get(keyEnemy).damage <= 0) {
                            logInfo = unit.name + " помер...";
                            System.out.println(logInfo);
                            log += logInfo + "\n";
                        }
                        break;
                    }
                    else {
                        keys.remove(ind);
                    }

                }

                if (!hasAttacked) {
                    enemiesObjects.get(keyEnemy).move(field);
                    int xPos = enemiesObjects.get(keyEnemy).xPos + 1;
                    int yPos = enemiesObjects.get(keyEnemy).yPos + 1;
                    logInfo = "Враг: " + enemiesObjects.get(keyEnemy).name + " переместился на клетку: " + xPos + " " + yPos;
                    System.out.println(logInfo);
                    log += logInfo + "\n";
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
                    int unitHp = unit.hp;
                    if (beastsObjects.get(keyBeast).attack(unit, field)) {
                        hasAttacked = true;
                        logInfo = "Зверь: " + beastsObjects.get(keyBeast).name + " атаковал: " + unit.name;
                        System.out.println(logInfo);
                        log += logInfo + "\n";
                        if (unitHp - beastsObjects.get(keyBeast).damage <= 0) {
                            logInfo = unit.name + " помер...";
                            System.out.println(logInfo);
                            log += logInfo + "\n";
                        }
                        break;
                    }
                    else {
                        keys.remove(ind);
                    }

                }

                if (!hasAttacked) {
                    beastsObjects.get(keyBeast).move(field);
                    int xPos = beastsObjects.get(keyBeast).xPos + 1;
                    int yPos = beastsObjects.get(keyBeast).yPos + 1;
                    logInfo = "Зверь: " + beastsObjects.get(keyBeast).name + " переместился на клетку: " + xPos + " " + yPos;
                    System.out.println(logInfo);
                    log += logInfo + "\n";
                }
            }
            field.setHeroes();
            movesAmount++;
            checkBuffs();
            System.out.println(field);

        }
        if (field.getEnemyCount() + field.getBeastsCount() == 0) {
            logInfo = "Победа героев!";
            System.out.println(logInfo);
            log += logInfo;
            menu.setMoney(menu.getMoney() + ((int) (Math.random() * 5) + 1));
        }
        else if (field.getHeroesCount() + field.getEnemyCount() == 0) {
            logInfo = "Победа зверей!";
            System.out.println(logInfo);
            log += logInfo;
        }
        else if (field.getHeroesCount() + field.getBeastsCount() == 0) {
            logInfo = "Победа врагов!";
            System.out.println(logInfo);
            log += logInfo;
        }
        FileWriter writer = new FileWriter(file, false);
        try(writer) {
            writer.append(log);
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }

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
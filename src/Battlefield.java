import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class Battlefield {
    private ArrayList<ArrayList<Character>> field = new ArrayList<>();
    private ArrayList<Character> signs = new ArrayList<>(Arrays.asList('*', '@', '#', '!'));
    private HashMap<String, HashMap<Character, Double>> signsHash = new HashMap<>();
    private HashMap<Hero, Integer> buffsHash = new HashMap<>();
    private HashMap<Hero, Integer> debuffsHash = new HashMap<>();
    private HashMap<Character, Hero> heroesMoves;
    private HashMap<Character, Hero> heroesAttacks;
    private HashMap<Character, Hero> heroesObjects = new HashMap<>();
    private HashMap<Character, Enemy> enemiesObjects = new HashMap<>();
    private HashMap<Character, Beast> beastsObjects = new HashMap<>();

    private int moves;
    private int attacks;
    private int size = 5;
    private int enemiesCount = 3;
    private int beastsCount = 3;
    private int heroesCount;
    private boolean dominator = false;
    private Menu menu;
    private Town town;


    Battlefield(int size, Menu menu, Town town, ArrayList<Hero> heroes, ArrayList<ArrayList<Character>> map, HashMap<Character, Double> newSigns, int enemiesCount, int beastsCount, boolean dominator) {
        this.menu = menu;
        this.town = town;
        this.size = size;
        this.enemiesCount = enemiesCount;
        this.beastsCount = beastsCount;
        this.dominator = dominator;
        makeHeroes(heroes);
        heroesMoves = new HashMap<>(heroesObjects);
        heroesAttacks = new HashMap<>(heroesObjects);
        moves = heroesMoves.size();
        attacks = heroesAttacks.size();
        this.heroesCount = heroes.size();
        setHeroes();
        makeSigns(newSigns);
//        System.out.println(signs);
//        System.out.println(signsHash);
        fill(map);

    }

    private void makeHeroes(ArrayList<Hero> heroes) {
        int ind = 1;
        for (Hero hero : heroes) {
            char tmp = (char) (48 + ind);
            heroesObjects.put(tmp, hero);
            ind++;
        }
    }

    private void makeSigns(HashMap<Character, Double> newSigns) {
        signsHash = menu.getDefaultSignsHash();
        for (Character s: newSigns.keySet()) {
            signs.add(s);
            for (String type: menu.getDefaultSignsHash().keySet()) {
                signsHash.get(type).put(s, newSigns.get(s));
            }
        }
    }
    @Override
    public String toString() {
        String output = "";
        for (int i = 0; i < size; i++) {
            output += field.get(i).toString();
            output += "\n";
        }

        output += "Герои (передвижение): \n";
        for (Character key : heroesMoves.keySet()) {
            Hero hero = heroesMoves.get(key);
            if (Objects.equals(hero.getType(), "wizards")) {
                String heroMovesString = key + ": " + "|" + "name: " + hero.name +
                        "|" + "hp: " + hero.hp +
                        "|" + "buff: " + hero.getBuff() +
                        "|" + "buffRange: " + hero.attackRange +
                        "|" + "armor: " + hero.armor +
                        "|" + "movement: " + hero.movement + "|" + "\n";
                output += heroMovesString;
            }
            else {
                String heroMovesString = key + ": " + "|" + "name: " + hero.name +
                        "|" + "hp: " + hero.hp +
                        "|" + "damage: " + hero.damage +
                        "|" + "attackRange: " + hero.attackRange +
                        "|" + "armor: " + hero.armor +
                        "|" + "movement: " + hero.movement + "|" + "\n";
                output += heroMovesString;
            }
        }

        output += "Герои (атака): \n";
        for (Character key : heroesAttacks.keySet()) {
            Hero hero = heroesAttacks.get(key);
            if (!Objects.equals(hero.getType(), "wizards")) {
                String heroAttacksString = key + ": " + "|" + "name: " + hero.name +
                        "|" + "hp: " + hero.hp +
                        "|" + "damage: " + hero.damage +
                        "|" + "attackRange: " + hero.attackRange +
                        "|" + "armor: " + hero.armor +
                        "|" + "movement: " + hero.movement + "|" + "\n";
                output += heroAttacksString;
            }
        }
        output += "Герои (поддержка): \n";
        for (Character key :heroesAttacks.keySet()) {
            Hero hero = heroesAttacks.get(key);
            if (Objects.equals(hero.getType(), "wizards")) {
                String heroAttacksString = key + ": " + "|" + "name: " + hero.name +
                        "|" + "hp: " + hero.hp +
                        "|" + "buff: " + hero.getBuff() +
                        "|" + "buffRange: " + hero.attackRange +
                        "|" + "armor: " + hero.armor +
                        "|" + "movement: " + hero.movement + "|" + "\n";
                output += heroAttacksString;
            }
        }

        output += "Враги: \n";
        for (Character key : enemiesObjects.keySet()) {
            Enemy enemy = enemiesObjects.get(key);
            String enemyString = key + ": " + "|" + "name: " + enemy.name +
                    "|" + "hp: " + enemy.hp +
                    "|" + "damage: " + enemy.damage +
                    "|" + "attackRange: " + enemy.attackRange +
                    "|" + "armor: " + enemy.armor +
                    "|" + "movement: " + enemy.movement + "|" + "\n";
            output += enemyString;
        }

        output += "Звери: \n";
        for (Character key : beastsObjects.keySet()) {
            Beast beast = beastsObjects.get(key);
            String beastString = key + ": " + "|" + "name: " + beast.name +
                    "|" + "hp: " + beast.hp +
                    "|" + "damage: " + beast.damage +
                    "|" + "attackRange: " + beast.attackRange +
                    "|" + "armor: " + beast.armor +
                    "|" + "movement: " + beast.movement + "|" + "\n";
            output += beastString;
        }
        return output;
    }

    public void heroNone(Hero hero) {
        char sign = field.get(hero.yPos).get(hero.xPos);
        heroesMoves.remove(sign);
        heroesAttacks.remove(sign);
        moves = heroesMoves.size();
        attacks = heroesAttacks.size();
    }

    public void unitDeath(Unit unit) {
        char sign = field.get(unit.yPos).get(unit.xPos);
        field.get(unit.yPos).set(unit.xPos, '*');
        if (unit instanceof Hero) {
            heroesObjects.remove(sign);
            heroesCount = heroesObjects.size();
        }
        else if (unit instanceof Enemy) {
            enemiesObjects.remove(sign);
            enemiesCount = enemiesObjects.size();
            town.setRock(town.getRock() + (int) ((Math.random() * 10) + 10));
            town.setWood(town.getWood() + (int) ((Math.random() * 10) + 10));
        }
        else if (unit instanceof Beast) {
            beastsObjects.remove(sign);
            beastsCount = beastsObjects.size();
            town.setRock(town.getRock() + (int) ((Math.random() * 5) + 10));
            town.setWood(town.getWood() + (int) ((Math.random() * 5) + 10));
        }
    }
    public boolean changePos(int x, int y, Unit unit) {
        if (unit instanceof Hero) {
            if (canMove(x, y, (Hero) unit)) {
                char sign = field.get(unit.yPos).get(unit.xPos);
                field.get(unit.yPos).set(unit.xPos, '*');
                field.get(y).set(x, sign);
                heroesMoves.remove(sign);
                moves = heroesMoves.size();
                return true;
            }
            return false;
        }
        else if (unit instanceof Enemy || unit instanceof Beast) {
            char sign = field.get(unit.yPos).get(unit.xPos);
            field.get(unit.yPos).set(unit.xPos, '*');
            field.get(y).set(x, sign);
            unit.xPos = x;
            unit.yPos = y;
            return true;
        }
        return false;

    }


    public boolean canAttack(Unit attacker, Unit target) {

        if ((heroesObjects.containsValue(attacker) && enemiesObjects.containsValue(target))
                || (heroesObjects.containsValue(target) && enemiesObjects.containsValue(attacker))
                || (beastsObjects.containsValue(target) && heroesObjects.containsValue(attacker))
                || (beastsObjects.containsValue(target) && enemiesObjects.containsValue(attacker))
                || attacker instanceof Beast
                || Objects.equals(attacker.getType(), "wizards") && heroesObjects.containsValue(target)) {
            int x = attacker.xPos;
            int y = attacker.yPos;
            int moves = 0;
            while (x != target.xPos && y != target.yPos) {
                if (x > target.xPos && y > target.yPos) {
                    x--;
                    y--;
                    moves++;
                    if (moves % 2 == 0)
                        moves++;
                }
                else if (x > target.xPos && y < target.yPos) {
                    x--;
                    y++;
                    moves++;
                    if (moves % 2 == 0)
                        moves++;
                }
                else if (x < target.xPos && y > target.yPos) {
                    x++;
                    y--;
                    moves++;
                    if (moves % 2 == 0)
                        moves++;
                }
                else if (x < target.xPos && y < target.yPos) {
                    x++;
                    y++;
                    moves++;
                    if (moves % 2 == 0)
                        moves++;
                }

            }
            while (x != target.xPos) {
                if (x < target.xPos) {
                    x++;
                    moves++;
                }
                else {
                    x--;
                    moves++;
                }
            }
            while (y != target.yPos) {
                if (y < target.yPos) {
                    y++;
                    moves++;
                }
                else {
                    y--;
                    moves++;
                }
            }
            if (attacker.attackRange >= moves) {
                char sign = field.get(attacker.yPos).get(attacker.xPos);
                if (attacker instanceof Hero) {
                    heroesAttacks.remove(sign);
                    attacks = heroesAttacks.size();
                }
                return true;
            }
            return false;
        }
        return false;

    }

    private boolean canMove(int x, int y, Hero hero) {
        double minDis = 0;
        if (field.get(y).get(x) != '*') {
            System.out.println("Нельзя останавливаться во всяких небезопасных местах");
            return false;
        }
        else if (hero.xPos == x && hero.yPos == y) {
            System.out.println("Герой не понял вашей шутки, но все же выполнил ваш приказ");
            return true;
        }
        else if (hero.yPos == y) {
            for (int i = Math.min(x, hero.xPos); i <= Math.max(x, hero.xPos); i++) {
                if (i == hero.xPos)
                    continue;
                else if (signsHash.get(hero.type).containsKey(field.get(y).get(i)))
                    minDis += signsHash.get(hero.type).get(field.get(y).get(i)) * hero.obstaclesRatio;
                else if (field.get(y).get(i) >= 97 && field.get(y).get(i) <= 122) {
                    System.out.println("Враг преградил дорогу");
                    return false;
                }
                else if (field.get(y).get(i) >= 65 && field.get(y).get(i) <= 90) {
                    System.out.println("Зверь преградил дорогу");
                    return false;
                }
                else if (field.get(y).get(i) >= 49 && field.get(y).get(i) <= 57) {
                    System.out.println("Хоть и друг, но пропустить не может...");
                    return false;
                }
                else
                    minDis++;
            }
            return minDis <= hero.movement;
        }
        else if (hero.xPos == x) {
            for (int i = Math.min(y, hero.yPos); i <= Math.max(y, hero.yPos); i++) {
                if (i == hero.yPos)
                    continue;
                else if (signsHash.get(hero.type).containsKey(field.get(y).get(i)))
                    minDis += signsHash.get(hero.type).get(field.get(y).get(i)) * hero.obstaclesRatio;
                else if (field.get(i).get(x) >= 97 && field.get(i).get(x) <= 122) {
                    System.out.println("Враг преградил дорогу");
                    return false;
                }
                else if (field.get(y).get(i) >= 65 && field.get(y).get(i) <= 90) {
                    System.out.println("Зверь преградил дорогу");
                    return false;
                }
                else if (field.get(i).get(x) >= 49 && field.get(i).get(x) <= 57) {
                    System.out.println("Хоть и друг, но пропустить не может...");
                    return false;
                }
                else
                    minDis++;
            }
            return minDis <= hero.movement;
        }
        else {
            ArrayList<ArrayList<Double>> distances = new ArrayList<>();
            for (int i = Math.min(y, hero.yPos); i <= Math.max(y, hero.yPos); i++) {
                ArrayList<Double> row = new ArrayList<>();
                for (int j = Math.min(x, hero.xPos); j <= Math.max(x, hero.xPos); j++) {
                    if (i == hero.yPos && j == hero.xPos)
                        row.add(0.0);
                    else if (signsHash.get(hero.type).containsKey(field.get(y).get(i)))
                        row.add(signsHash.get(hero.type).get(field.get(y).get(i)) * hero.obstaclesRatio) ;
                    else if ((field.get(i).get(j) >= 97 && field.get(i).get(j) <= 122) || (field.get(i).get(j) >= 49 && field.get(j).get(j) <= 57) || (field.get(y).get(i) >= 65 && field.get(y).get(i) <= 90))
                        row.add(1000.0);
                    else
                        row.add(1.0);
                }
                distances.add(row);
            }
            int arraysLen = distances.size();
            int rowLen = distances.get(0).size();
            double sum;
//            for (int i = 0; i < distances.size(); i++)
//                System.out.println(distances.get(i));

            if (distances.get(0).get(rowLen-1) == 0.0) {
                for (int i = 0; i < arraysLen; i++) {
                    for (int j = rowLen-1; j >= 0; j--) {
                        if (i == 0) {
                            if (j == rowLen-1)
                                continue;
                            else {
                                sum = distances.get(i).get(j) + distances.get(i).get(j+1);
                                distances.get(i).set(j, sum);
                            }

                        }
                        else {
                            if (j == rowLen - 1) {
                                sum = distances.get(i).get(j) + distances.get(i-1).get(j);
                                distances.get(i).set(j, sum);
                            }
                            else {
                                sum = Math.min(distances.get(i).get(j) + distances.get(i).get(j+1), distances.get(i).get(j) + distances.get(i-1).get(j));
                                distances.get(i).set(j, sum);
                            }

                        }

                    }
                }
//                for (int i = 0; i < distances.size(); i++)
//                    System.out.println(distances.get(i));
                return distances.get(arraysLen-1).get(0) <= hero.movement;

            }
            else if (distances.get(arraysLen-1).get(0) == 0.0) {
                for (int i = arraysLen-1; i >= 0; i--) {
                    for (int j = 0; j < rowLen; j++) {
                        if (i == arraysLen - 1) {
                            if (j == 0)
                                continue;
                            else {
                                sum = distances.get(i).get(j) + distances.get(i).get(j-1);
                                distances.get(i).set(j, sum);
                            }

                        }
                        else {
                            if (j == 0) {
                                sum = distances.get(i).get(j) + distances.get(i+1).get(j);
                                distances.get(i).set(j, sum);
                            }
                            else {
                                sum = Math.min(distances.get(i).get(j) + distances.get(i).get(j-1), distances.get(i).get(j) + distances.get(i+1).get(j));
                                distances.get(i).set(j, sum);
                            }

                        }

                    }
                }
//                for (int i = 0; i < distances.size(); i++)
//                    System.out.println(distances.get(i));
                return distances.get(0).get(rowLen-1) <= hero.movement;
            }
            else if (distances.get(arraysLen-1).get(rowLen-1) == 0.0) {
                for (int i = arraysLen-1; i >= 0; i--) {
                    for (int j = rowLen-1; j >= 0; j--) {
                        if (i == arraysLen-1) {
                            if (j == rowLen-1)
                                continue;
                            else {
                                sum = distances.get(i).get(j) + distances.get(i).get(j+1);
                                distances.get(i).set(j, sum);
                            }

                        }
                        else {
                            if (j == rowLen - 1) {
                                sum = distances.get(i).get(j) + distances.get(i+1).get(j);
                                distances.get(i).set(j, sum);
                            }
                            else {
                                sum = Math.min(distances.get(i).get(j) + distances.get(i).get(j+1), distances.get(i).get(j) + distances.get(i+1).get(j));
                                distances.get(i).set(j, sum);
                            }

                        }

                    }
                }
//                for (int i = 0; i < distances.size(); i++)
//                    System.out.println(distances.get(i));
                return distances.get(0).get(0) <= hero.movement;
            }
            else {
                for (int i = 0; i < arraysLen; i++) {
                    for (int j = 0; j < rowLen; j++) {
                        if (i == 0) {
                            if (j == 0)
                                continue;
                            else {
                                sum = distances.get(i).get(j) + distances.get(i).get(j-1);
                                distances.get(i).set(j, sum);
                            }

                        }
                        else {
                            if (j == 0) {
                                sum = distances.get(i).get(j) + distances.get(i-1).get(j);
                                distances.get(i).set(j, sum);
                            }
                            else {
                                sum = Math.min(distances.get(i).get(j) + distances.get(i).get(j-1), distances.get(i).get(j) + distances.get(i-1).get(j));
                                distances.get(i).set(j, sum);
                            }

                        }

                    }
                }
//                for (int i = 0; i < distances.size(); i++)
//                    System.out.println(distances.get(i));
                return distances.get(arraysLen-1).get(rowLen-1) <= hero.movement;
            }
        }
    }

    public void enemyMove(Enemy enemy) {
        double distance = 0;
        int bestYPos = enemy.yPos;
        if (enemy.yPos != 0) {
            for (int i = enemy.yPos - 1; i >= 0; i--) {
                if (field.get(i).get(enemy.xPos) == '*') {
                    distance += 1.0;
                    if (enemy.movement >= distance)
                        bestYPos = i;
                    else {
                        changePos(enemy.xPos, bestYPos, enemy);
                        return;
                    }
                }
                else if (signsHash.get(enemy.type).containsKey(field.get(i).get(enemy.xPos)))
                    distance += signsHash.get(enemy.type).get(field.get(i).get(enemy.xPos)) * enemy.obstaclesRatio;
                else if ((field.get(i).get(enemy.xPos) >= 97 && field.get(i).get(enemy.xPos) <= 122) || (field.get(i).get(enemy.xPos) >= 49 && field.get(i).get(enemy.xPos) <= 57) || (field.get(i).get(enemy.xPos) >= 65 && field.get(i).get(enemy.xPos) <= 90)) {
                    changePos(enemy.xPos, bestYPos, enemy);
                    return;
                }
            }
            changePos(enemy.xPos, bestYPos, enemy);
        }
    }

    public void beastMove(Beast beast) {
        ArrayList<ArrayList<Integer>> ways = new ArrayList<>();
        int distance = 0;
        for (int i = beast.yPos - 1; i >= 0; i--) {
            if (field.get(i).get(beast.xPos) == '*') {
                distance++;
                if (beast.movement >= distance)
                    ways.add(new ArrayList<>(Arrays.asList(i, beast.xPos)));
                else
                    break;
            }
            else if (signsHash.get(beast.type).containsKey(field.get(i).get(beast.xPos)))
                distance++;
            else if (field.get(i).get(beast.xPos) >= 97 && field.get(i).get(beast.xPos) <= 122 || field.get(i).get(beast.xPos) >= 49 && field.get(i).get(beast.xPos) <= 57 || field.get(i).get(beast.xPos) >= 65 && field.get(i).get(beast.xPos) <= 90)
                break;
        }
        distance = 0;
        for (int i = beast.yPos + 1; i < size; i++) {
            if (field.get(i).get(beast.xPos) == '*') {
                distance++;
                if (beast.movement >= distance)
                    ways.add(new ArrayList<>(Arrays.asList(i, beast.xPos)));
                else
                    break;
            }
            else if (signsHash.get(beast.type).containsKey(field.get(i).get(beast.xPos)))
                distance++;
            else if (field.get(i).get(beast.xPos) >= 97 && field.get(i).get(beast.xPos) <= 122 || field.get(i).get(beast.xPos) >= 49 && field.get(i).get(beast.xPos) <= 57 || field.get(i).get(beast.xPos) >= 65 && field.get(i).get(beast.xPos) <= 90)
                break;
        }
        distance = 0;
        for (int i = beast.xPos + 1; i < size; i++) {
            if (field.get(beast.yPos).get(i) == '*') {
                distance++;
                if (beast.movement >= distance)
                    ways.add(new ArrayList<>(Arrays.asList(beast.yPos, i)));
                else
                    break;
            }
            else if (signsHash.get(beast.type).containsKey(field.get(i).get(beast.xPos)))
                distance++;
            else if (field.get(beast.yPos).get(i) >= 97 && field.get(beast.yPos).get(i) <= 122 || field.get(beast.yPos).get(i) >= 49 && field.get(beast.yPos).get(i) <= 57 || field.get(beast.yPos).get(i) >= 65 && field.get(beast.yPos).get(i) <= 90)
                break;
        }
        distance = 0;
        for (int i = beast.xPos - 1; i >= 0; i--) {
            if (field.get(beast.yPos).get(i) == '*') {
                distance++;
                if (beast.movement >= distance)
                    ways.add(new ArrayList<>(Arrays.asList(beast.yPos, i)));
                else
                    break;
            }
            else if (signsHash.get(beast.type).containsKey(field.get(i).get(beast.xPos)))
                distance++;
            else if (field.get(beast.yPos).get(i) >= 97 && field.get(beast.yPos).get(i) <= 122 || field.get(beast.yPos).get(i) >= 49 && field.get(beast.yPos).get(i) <= 57 || field.get(beast.yPos).get(i) >= 65 && field.get(beast.yPos).get(i) <= 90)
                break;
        }
        int index = (int) (Math.random() * ways.size());
        changePos(ways.get(index).get(1), ways.get(index).get(0), beast);

    }

    private void fill(ArrayList<ArrayList<Character>> map) {
        ArrayList<Integer> repeats = new ArrayList<>();

        if (map == null) {
            for (int i = 0; i < size; i++) {
                ArrayList<Character> row = new ArrayList<>();
                for (int j = 0; j < size; j++)
                    row.add(signs.get(0));

                if (i != 0) {
                    for (int q = 0; q < size / 2; q++) {
                        int index = (int) (Math.random() * size);
                        while (repeats.contains(index))
                            index = (int) (Math.random() * size);
                        row.set((int) (Math.random() * size), signs.get((int) (Math.random() * 3) + 1));
                        repeats.add(index);
                    }
                }
                field.add(row);
                repeats.clear();
            }
        }
        else {
            field = map;
        }

        for (int k = 0; k < enemiesCount; k++) {
            int yPos, xPos;
            char tmp = (char) (97 + k);
            xPos = (int) (Math.random() * size);
            while (repeats.contains(xPos))
                xPos = (int) (Math.random() * size);

            field.get(size-1).set((xPos), tmp);

            String name;
            if (dominator && k == 0)
                name = "Dominator";
            else
                name = menu.getNames().get((int) (Math.random() * menu.getNames().size()));
            enemiesObjects.put(tmp, new Enemy(name, menu));
            enemiesObjects.get(tmp).xPos = xPos;
            enemiesObjects.get(tmp).yPos = size-1;
            repeats.add(xPos);
        }
        repeats.clear();

        for (int k = 0; k < beastsCount; k++) {
            int yPos, xPos;
            char tmp = (char) (65 + k);
            xPos = (int) (Math.random() * size);
            while (repeats.contains(xPos))
                xPos = (int) (Math.random() * size);
            yPos = size / 2;
            field.get(yPos).set((xPos), tmp);
            String name;
            if (dominator && k == 0)
                name = "Dominator";
            else
                name = menu.getBeasts().get((int) (Math.random() * menu.getBeasts().size()));
            beastsObjects.put(tmp, new Beast(name, menu, this));
            beastsObjects.get(tmp).xPos = xPos;
            beastsObjects.get(tmp).yPos = yPos;
            repeats.add(xPos);
        }
        repeats.clear();

        for (Character key : heroesObjects.keySet()) {
            int xPos = (int) (Math.random() * size);
            while (repeats.contains(xPos))
                xPos = (int) (Math.random() * size);
            field.get(0).set(xPos, key);
            heroesObjects.get(key).xPos = xPos;
            heroesObjects.get(key).yPos = 0;
            repeats.add(xPos);
        }

    }

    public void setHeroes() {
        heroesCount = heroesObjects.size();
        heroesMoves = new HashMap<>(heroesObjects);
        heroesAttacks = new HashMap<>(heroesObjects);
        moves = heroesMoves.size();
        attacks = heroesAttacks.size();
        for (Character s : heroesObjects.keySet()) {
            heroesObjects.get(s).hasMoved = false;
            heroesObjects.get(s).hasAttacked = false;
        }
    }
    public int getEnemyCount() {return enemiesCount;}
    public int getHeroesCount() {return heroesCount;}
    public int getBeastsCount() {return beastsCount;}
    public int getMoves() {return moves;}
    public int getAttacks() {return attacks;}
    public HashMap<Character, Enemy> getEnemiesObjects() {return enemiesObjects;}
    public HashMap<Character, Hero> getHeroesObjects() {return heroesObjects;}
    public HashMap<Character, Beast> getBeastsObjects() {return beastsObjects;}
    public HashMap<Hero, Integer> getBuffsHash() {return buffsHash;}
    public HashMap<Hero, Integer> getDebuffsHash() {return debuffsHash;}
}

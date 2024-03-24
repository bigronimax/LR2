import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class Menu {

    private HashMap<String, HashMap<String, Integer>> menu = new HashMap<>();
    private int money = 50;
    private ArrayList<String> types = new ArrayList<>(Arrays.asList("melee", "archers", "riders"));
    private HashMap<String, String> typesHash = new HashMap<>();
    private HashMap<String, HashMap<Character, Double>> defaultSignsHash = new HashMap<>();
    private ArrayList<String> obtainableHeroes = new ArrayList<>();
    private ArrayList<String> names = new ArrayList<>(Arrays.asList("Swordsman", "Spearman", "Axeman", "Longbow", "Short_bow", "Crossbow", "Knight", "Cuirassier", "Horse_archer"));
    private ArrayList<String> beasts = new ArrayList<>(Arrays.asList("Eagle", "Bear", "Tiger", "Elephant", "Wolf"));

    Menu() {
        fill();
    }

    @Override
    public String toString() {
        String output = "";
        obtainableHeroes.clear();
        checkObtainableHeroes();
        if (!obtainableHeroes.isEmpty()) {
            for (String name : obtainableHeroes) {
                if (!Objects.equals(name, "Dominator")) {
                    String properties = "|" + "name: " + name +
                            "|" + "hp: " + menu.get(name).get("hp") +
                            "|" + "damage: " + menu.get(name).get("damage") +
                            "|" + "attackRange: " + menu.get(name).get("attackRange") +
                            "|" + "armor: " + menu.get(name).get("armor") +
                            "|" + "movement: " + menu.get(name).get("movement") +
                            "|" + "cost: " + menu.get(name).get("cost") + "|" + "\n";
                    output += properties;
                }
            }
        }
        else
            output += "Пока у вас недостаточно денег для найма героев, продолжайте сражаться!";
        return output;
    }
    public void addUnit(String name, int hp, int damage, int attackRange, int armor, int movement, int cost) {
        HashMap<String, Integer> hero = new HashMap<>();
        hero.put("hp", hp);
        hero.put("damage", damage);
        hero.put("attackRange", attackRange);
        hero.put("armor", armor);
        hero.put("movement", movement);
        hero.put("cost", cost);
        menu.put(name, hero);
    }

    private void addSign(String type, Character s, double penalty) {
        if (!defaultSignsHash.containsKey(type)) {
            HashMap<Character, Double> sign = new HashMap<>();
            sign.put(s, penalty);
            defaultSignsHash.put(type, sign);
        }
        else {
            defaultSignsHash.get(type).put(s, penalty);
        }

    }

    private void fill() {
        addUnit(names.get(0), 50, 5, 1, 8, 3, 10);
        addUnit(names.get(1), 35, 3, 1, 4, 6, 15);
        addUnit(names.get(2), 45, 9, 1, 3, 4, 20);
        addUnit(names.get(3), 30, 6, 5, 8, 2, 15);
        addUnit(names.get(4), 25, 3, 3, 4, 4, 19);
        addUnit(names.get(5), 40, 7, 6, 3, 2, 23);
        addUnit(names.get(6), 30, 5, 1, 3, 6, 20);
        addUnit(names.get(7), 50, 2, 1, 7, 5, 23);
        addUnit(names.get(8), 25, 3, 3, 2, 5, 25);

        addUnit(beasts.get(0), 25, 3, 3, 2, 5, 0);
        addUnit(beasts.get(1), 40, 7, 1, 8, 3, 0);
        addUnit(beasts.get(2), 30, 5, 3, 3, 6, 0);
        addUnit(beasts.get(3), 50, 2, 2, 8, 2, 0);
        addUnit(beasts.get(4), 25, 5, 2, 2, 5, 0);

        addUnit("Dominator", 100, 100, 100, 0, 100, 0);

        typesHash.put(names.get(0), types.get(0));
        typesHash.put(names.get(1), types.get(0));
        typesHash.put(names.get(2), types.get(0));
        typesHash.put(names.get(3), types.get(1));
        typesHash.put(names.get(4), types.get(1));
        typesHash.put(names.get(5), types.get(1));
        typesHash.put(names.get(6), types.get(2));
        typesHash.put(names.get(7), types.get(2));
        typesHash.put(names.get(8), types.get(2));

        typesHash.put(beasts.get(0), types.get(1));
        typesHash.put(beasts.get(1), types.get(0));
        typesHash.put(beasts.get(2), types.get(0));
        typesHash.put(beasts.get(3), types.get(2));
        typesHash.put(beasts.get(4), types.get(0));

        typesHash.put("Dominator", types.get(2));

        addSign(types.get(0), '#', 1.5);
        addSign(types.get(0), '@', 2);
        addSign(types.get(0), '!', 1.2);
        addSign(types.get(1), '#', 1.8);
        addSign(types.get(1), '@', 2.2);
        addSign(types.get(1), '!', 1);
        addSign(types.get(2), '#', 2.2);
        addSign(types.get(2), '@', 1.2);
        addSign(types.get(2), '!', 1.5);



    }

    private void checkObtainableHeroes() {
        for (String hero: names) {
            if (money >= menu.get(hero).get("cost")) {
                obtainableHeroes.add(hero);
            }
        }
        obtainableHeroes.add("Dominator");
    }

    public HashMap<String, HashMap<String, Integer>> getMenu() {return menu;}
    public ArrayList<String> getBeasts() {return beasts;}
    public ArrayList<String> getNames() {return names;}
    public HashMap<String, String> getTypesHash() {return typesHash;}
    public HashMap<String, HashMap<Character, Double>> getDefaultSignsHash() {return defaultSignsHash;}
    public int getMoney() {return money;}
    public ArrayList<String> getObtainableHeroes() {return obtainableHeroes;}

    public void setMoney(int money) {this.money = money;}
}

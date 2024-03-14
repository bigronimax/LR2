import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Menu {

    private HashMap<String, HashMap<String, Integer>> menu = new HashMap<>();
    private ArrayList<String> names = new ArrayList<>(Arrays.asList("Swordsman", "Spearman", "Axeman", "Longbow", "Short bow", "Crossbow", "Knight", "Cuirassier", "Horse archer"));
    private ArrayList<String> beasts = new ArrayList<>(Arrays.asList("Eagle", "Bear", "Tiger", "Elephant", "Wolf"));

    Menu() {
        fill();
    }
    private void add(String name, int hp, int damage, int attackRange, int armor, int movement, int cost) {
        HashMap<String, Integer> hero = new HashMap<>();
        hero.put("hp", hp);
        hero.put("damage", damage);
        hero.put("attackRange", attackRange);
        hero.put("armor", armor);
        hero.put("movement", movement);
        hero.put("cost", cost);
        menu.put(name, hero);
    }

    private void fill() {
        add(names.get(0), 50, 5, 1, 8, 3, 10);
        add(names.get(1), 35, 3, 1, 4, 6, 15);
        add(names.get(2), 45, 9, 1, 3, 4, 20);
        add(names.get(3), 30, 6, 5, 8, 2, 15);
        add(names.get(4), 25, 3, 3, 4, 4, 19);
        add(names.get(5), 40, 7, 6, 3, 2, 23);
        add(names.get(6), 30, 5, 1, 3, 6, 20);
        add(names.get(7), 50, 2, 1, 7, 5, 23);
        add(names.get(8), 25, 3, 3, 2, 5, 25);
        add(beasts.get(0), 25, 3, 3, 2, 5, 0);
        add(beasts.get(1), 40, 7, 1, 8, 3, 0);
        add(beasts.get(2), 30, 5, 3, 3, 6, 0);
        add(beasts.get(3), 50, 2, 2, 8, 2, 0);
        add(beasts.get(4), 25, 5, 2, 2, 5, 0);
        add("Dominator", 100, 100, 100, 100, 100, 0);
    }

    @Override
    public String toString() {
        String output = "";
        for (String name : names) {
            String properties = "|" + "name: " + name +
                    "|" + "hp: " + menu.get(name).get("hp") +
                    "|" + "damage: " + menu.get(name).get("damage") +
                    "|" + "attackRange: " + menu.get(name).get("attackRange") +
                    "|" + "armor: " + menu.get(name).get("armor") +
                    "|" + "movement: " + menu.get(name).get("movement") +
                    "|" + "cost: " + menu.get(name).get("cost") + "|" + "\n";
            output += properties;
        }
        return output;
    }

    public HashMap<String, HashMap<String, Integer>> getMenu() {return menu;}
    public ArrayList<String> getBeasts() {return beasts;}
    public ArrayList<String> getNames() {return names;}


}

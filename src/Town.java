import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class Town implements Serializable {

    private static final long serialVersionUID = 1L;
    private int wood = 0;
    private int rock = 0;

    ArrayList<String> buildingsNames = new ArrayList<>(Arrays.asList("hospital", "forge", "tavern_movement", "tavern_obstacles", "arsenal", "market", "academy", "workshop", "magicAcademy"));
    ArrayList<String> buildingsUp = new ArrayList<>();
    ArrayList<String> obtainableBuildings = new ArrayList<>();
    HashMap<String, HashMap<String, Integer>> buildingsPrices = new HashMap<>();

    Town() {
        fill();
    }

    @Override
    public String toString() {
        String output = "";
        obtainableBuildings.clear();
        checkObtainableBuildings();
        output += "wood: " + wood + ", " + "rock: " + rock + "\n";
        if (!obtainableBuildings.isEmpty()) {
            for (String b : obtainableBuildings) {
                String prices = "|" + b +
                        "|" + "wood: " + buildingsPrices.get(b).get("wood") +
                        "|" + "rock: " + buildingsPrices.get(b).get("rock") + "|" + "\n";
                output += prices;
            }
        }
        else
            output += "Пока у вас не хватает ресурсов, продолжайте сражаться!";
        return output;
    }


    private void fill() {
        buildingsUp.add("hospital");
        buildingsUp.add("forge");
        buildingsUp.add("tavern_movement");
        buildingsUp.add("tavern_obstacles");
        buildingsUp.add("arsenal");

        for (String s: buildingsNames) {
            HashMap<String, Integer> prices = new HashMap<>();
            if (Objects.equals(s, "tavern_movement") || Objects.equals(s, "tavern_obstacles")) {
                if (!buildingsPrices.containsKey("tavern"))
                    buildingsPrices.put("tavern", prices);
            }
            else
                buildingsPrices.put(s, prices);
        }

        buildingsPrices.get("hospital").put("wood", 20);
        buildingsPrices.get("hospital").put("rock", 15);
        buildingsPrices.get("forge").put("wood", 15);
        buildingsPrices.get("forge").put("rock", 20);
        buildingsPrices.get("tavern").put("wood", 25);
        buildingsPrices.get("tavern").put("rock", 10);
        buildingsPrices.get("arsenal").put("wood", 10);
        buildingsPrices.get("arsenal").put("rock", 25);
        buildingsPrices.get("workshop").put("wood", 20);
        buildingsPrices.get("workshop").put("rock", 20);
        buildingsPrices.get("market").put("wood", 50);
        buildingsPrices.get("market").put("rock", 50);
        buildingsPrices.get("academy").put("wood", 50);
        buildingsPrices.get("academy").put("rock", 50);
        buildingsPrices.get("magicAcademy").put("wood", 20);
        buildingsPrices.get("magicAcademy").put("rock", 20);
    }

    private void checkObtainableBuildings() {
        for (String b: buildingsPrices.keySet()) {
            if (wood >= buildingsPrices.get(b).get("wood") && rock >= buildingsPrices.get(b).get("rock")) {
                obtainableBuildings.add(b);
            }
        }
    }
    public int getWood() {return wood;}
    public int getRock() {return rock;}
    public ArrayList<String> getBuildingsUp() {return buildingsUp;}
    public ArrayList<String> getObtainableBuildings() {return obtainableBuildings;}
    public HashMap<String, HashMap<String, Integer>> getBuildingsPrices() {return buildingsPrices;}
    public void setWood(int wood) {this.wood = wood;}
    public void setRock(int rock) {this.rock = rock;}
}

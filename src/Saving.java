import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Saving implements Serializable {

    private static final long serialVersionUID = 1L;
    private Academy academy;
    private Market market;
    private ArrayList<WorkShop> workShopsArray;
    private HashMap<String, Building> buildingsUp;
    private ArrayList<Hero> heroes;
    private HashMap<String, HashMap<String, Integer>> menu;
    private ArrayList<String> names;
    private int money;
    private int wood;
    private int rock;

    Saving(Academy academy,
           Market market,
           ArrayList<WorkShop> workShopsArray,
           HashMap<String, Building> buildingsUp,
           ArrayList<Hero> heroes,
           HashMap<String, HashMap<String, Integer>> menu,
           ArrayList<String> names,
           int money,
           int wood,
           int rock) {
        this.academy = academy;
        this.market = market;
        this.workShopsArray = workShopsArray;
        this.buildingsUp = buildingsUp;
        this.heroes = heroes;
        this.menu = menu;
        this.names = names;
        this.money = money;
        this.wood = wood;
        this.rock = rock;
    }

    public Academy getAcademy() {return academy;}
    public Market getMarket() {return market;}
    public ArrayList<WorkShop> getWorkShopsArray() {return workShopsArray;}
    public HashMap<String, Building> getBuildingsUp() {return buildingsUp;}
    public ArrayList<Hero> getHeroes() {return heroes;}
    public HashMap<String, HashMap<String, Integer>> getMenu() {return menu;}
    public ArrayList<String> getNames() {return names;}
    public int getMoney() {return money;}
    public int getWood() {return wood;}
    public int getRock() {return rock;}
}

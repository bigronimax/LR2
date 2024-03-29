import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class MagicAcademy implements Serializable {

    private static final long serialVersionUID = 1L;
    private Menu menu;
    private ArrayList<Hero> heroes;
    private int buff = 2;
    private int movesBuff = 2;
    private int level = 0;

    MagicAcademy(Menu menu, ArrayList<Hero> heroes) {
        this.menu = menu;
        this.heroes = heroes;
        level++;
    }

    @Override
    public String toString() {
        String output = "magicAcademy" + " " + level;
        return output;
    }

    public boolean createWizard(String name) {
        int cost = menu.getMenu().get("Wizard").get("cost");
        boolean flag = false;
        for (Hero hero: heroes) {
            if (Objects.equals(hero.name, name)) {
                heroes.remove(hero);
                flag = true;
                break;
            }
        }
        if (flag) {
            int random = (int) (Math.random() * 100) + 1;
            if (random < 50) {
                heroes.add(new Hero("Necromancer", menu, buff, movesBuff));
                System.out.println("Получить мага: >=50/100. Выпало: " + random + ". Некромант вступил в ваш отряд!");
            }
            else {
                heroes.add(new Hero("Wizard", menu, buff, movesBuff));
                System.out.println("Получить мага: >=50/100. Выпало: " + random + ". Маг вступил в ваш отряд!");
            }
            menu.setMoney(menu.getMoney() - cost);
            return true;
        }
        else {
            return false;
        }
    }

    public void levelUp() {
        buff++;
        movesBuff++;
        level++;
    }

    public int getLevel() {return level;}

}

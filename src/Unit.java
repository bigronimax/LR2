import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public abstract class Unit {
    protected String name;
    protected int hp;
    protected int damage;
    protected int attackRange;
    protected int armor;
    protected int movement;
    protected int xPos;
    protected int yPos;
    protected double swampPenalty;
    protected double treePenalty;
    protected double heapPenalty;
    protected Menu menu;

    protected Battlefield field;

    private ArrayList<String> melee = new ArrayList<>(Arrays.asList("Swordsman", "Spearman", "Axeman"));
    private ArrayList<String> archers = new ArrayList<>(Arrays.asList("Longbow", "Short bow", "Crossbow"));
    private ArrayList<String> riders = new ArrayList<>(Arrays.asList("Knight", "Cuirassier", "Horse archer"));

    Unit(String name, Menu menu, Battlefield field) {
        this.menu = menu;
        this.name = name;
        this.field = field;
        create();
    }

    private void create() {
        HashMap<String, Integer> heroHash = menu.getMenu().get(name);
        hp = heroHash.get("hp");
        damage = heroHash.get("damage");
        attackRange = heroHash.get("attackRange");
        armor = heroHash.get("armor");
        movement = heroHash.get("movement");
        if (melee.contains(name)) {
            swampPenalty = 1.5;
            treePenalty = 1.2;
            heapPenalty = 2;
        }
        else if (archers.contains(name)) {
            swampPenalty = 1.8;
            treePenalty = 1;
            heapPenalty = 2.2;
        }
        else if (riders.contains(name)) {
            swampPenalty = 2.2;
            treePenalty = 1.5;
            heapPenalty = 1.2;
        }
    }

    protected boolean attack(Unit target) {
        if (field.canAttack(this, target)) {
            int hit = damage;
            if (target.armor >= damage) {
                target.armor -= damage;
            } else {
                hit -= target.armor;
                target.armor = 0;
                target.hp -= hit;
                if (target.hp <= 0)
                    target.death();
            }
            return true;
        }
        return false;
    }

    protected void death() {
        field.unitDeath(this);
    }

}



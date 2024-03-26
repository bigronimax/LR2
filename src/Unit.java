import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public abstract class Unit implements Serializable {
    private static final long serialVersionUID = 1L;
    protected String name;
    protected int hp;
    protected int damage;
    protected int attackRange;
    protected int armor;
    protected int movement;
    protected double obstaclesRatio = 1;
    protected int xPos;
    protected int yPos;
    protected String type;
    protected HashMap<Character, Double> obstacles = new HashMap<>();
    protected Menu menu;

    Unit(String name, Menu menu) {
        this.menu = menu;
        this.name = name;
        create();
    }

    private void create() {
        HashMap<String, Integer> heroHash = menu.getMenu().get(name);
        hp = heroHash.get("hp");
        damage = heroHash.get("damage");
        attackRange = heroHash.get("attackRange");
        armor = heroHash.get("armor");
        movement = heroHash.get("movement");
        type = menu.getTypesHash().get(name);
    }

    protected boolean attack(Unit target, Battlefield field) {
        if (field.canAttack(this, target)) {
            int hit = damage;
            if (target.armor >= damage) {
                target.armor -= damage;
            } else {
                hit -= target.armor;
                target.armor = 0;
                target.hp -= hit;
                if (target.hp <= 0)
                    target.death(field);
            }
            return true;
        }
        return false;
    }

    protected void death(Battlefield field) {
        field.unitDeath(this);
    }

}



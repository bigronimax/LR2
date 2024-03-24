import java.util.HashMap;

public class Hero extends Unit {

    public boolean hasMoved = false;
    public boolean hasAttacked = false;

    protected HashMap<String, Integer> propsHash = new HashMap<>();


    Hero(String name, Menu menu) {
        super(name, menu);
        propsHash.put("hp", hp);
        propsHash.put("damage", damage);
        propsHash.put("movement", movement);
        propsHash.put("armor", armor);
    }

    protected void move(int x, int y, Battlefield field) {
        if (field.changePos(x - 1, y - 1, this) && !hasMoved) {
            hasMoved = true;
            xPos = x - 1;
            yPos = y - 1;
        }
    }

    @Override
    protected boolean attack(Unit target, Battlefield field) {
        if (field.canAttack(this, target) && !hasAttacked) {
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
            hasAttacked = true;
            return true;
        }
        return false;
    }

    protected void none(Battlefield field) {
        field.heroNone(this);
    }

}

import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;

public class Hero extends Unit implements Serializable {

    private static final long serialVersionUID = 1L;
    public boolean hasMoved = false;
    public boolean hasAttacked = false;
    private int buff = 0;
    private int movesBuff = 0;

    Hero(String name, Menu menu) {
        super(name, menu);
    }
    Hero(String name, Menu menu, int buff, int movesBuff) {
        super(name, menu);

        this.buff = buff;
        this.movesBuff = movesBuff;
    }

    protected boolean move(int x, int y, Battlefield field) {
        if (field.changePos(x - 1, y - 1, this) && !hasMoved) {
            hasMoved = true;
            xPos = x - 1;
            yPos = y - 1;
            return true;
        }
        return false;
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

    protected boolean buff(Unit target, Battlefield field) {
        if (Objects.equals(this.type, "wizards")) {
            if (field.canAttack(this, target) && !hasAttacked) {
                target.movement += buff;
                target.damage += buff;
                target.armor += buff;
                hasAttacked = true;
                field.getBuffsHash().put((Hero) target, movesBuff);
                return true;
            }
        }
        return false;
    }

    protected boolean debuff(Unit target, Battlefield field) {
        if (Objects.equals(this.type, "wizards")) {
            if (field.canAttack(this, target) && !hasAttacked) {
                target.movement -= buff;
                target.damage -= buff;
                target.armor -= buff;
                hasAttacked = true;
                field.getDebuffsHash().put((Hero) target, movesBuff);
                return true;
            }
        }
        return false;
    }

    protected void none(Battlefield field) {
        field.heroNone(this);
    }

    public int getBuff() {return buff;}

    public int getMovesBuff() {return movesBuff;}
}

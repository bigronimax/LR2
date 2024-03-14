public class Hero extends Unit {

    public boolean hasMoved = false;
    public boolean hasAttacked = false;


    Hero(String name, Menu menu, Battlefield field) {
        super(name, menu, field);
    }

    protected void move(int x, int y) {
        if (field.changePos(x - 1, y - 1, this)) {
            hasMoved = true;
            xPos = x - 1;
            yPos = y - 1;
        }
    }

    protected void none() {
        field.heroNone(this);
    }

}

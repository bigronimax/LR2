public class Enemy extends Unit {

    Enemy(String name, Menu menu, Battlefield field) {
        super(name, menu, field);
    }


    protected void move() {
        field.enemyMove(this);
    }


}

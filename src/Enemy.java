public class Enemy extends Unit {

    Enemy(String name, Menu menu) {
        super(name, menu);
    }


    protected void move(Battlefield field) {
        field.enemyMove(this);
    }


}

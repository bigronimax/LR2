public class Beast extends Unit {

    Beast(String name, Menu menu, Battlefield field) {
        super(name, menu, field);
    }

    protected void move() {
        field.beastMove(this);
    }

}

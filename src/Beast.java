public class Beast extends Unit {

    Beast(String name, Menu menu, Battlefield field) {
        super(name, menu);
    }

    protected void move(Battlefield field) {
        field.beastMove(this);
    }

}

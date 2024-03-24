public class WorkShop {

    private int goldUp = 2;
    private Menu menu;
    private int level = 0;
    WorkShop(Menu menu) {
        this.menu = menu;
        level++;
    }

    @Override
    public String toString() {
        String output = "workshop" + " " + level;
        return output;
    }

    public void upgrade() {
        menu.setMoney(menu.getMoney() + goldUp);
    }

    public void levelUp() {
        level++;
        goldUp++;
    }

    public int getLevel() {return level;}


}

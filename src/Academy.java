public class Academy {

    private Menu menu;

    Academy(Menu menu) {
        this.menu = menu;
    }

    public boolean createHero(String name, int hp, int damage, int attackRange, int armor, int movement) {
        int cost = (int) (hp * 0.1 + damage * 0.75 + attackRange + armor * 0.1 + movement * 0.75);
        if (menu.getMoney() >= cost) {
            menu.addUnit(name, hp, damage, attackRange, armor, movement, cost);
            menu.getNames().add(name);
            menu.setMoney(menu.getMoney() - cost);
            return true;
        }
        else
            return false;
    }
}

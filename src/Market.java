public class Market {

    private int rock = 0;
    private int wood = 0;
    private Town town;

    Market(Town town) {
        this.town = town;
    }

    @Override
    public String toString() {
        String output = "";
        output += "Сегодняшний курс: " + wood + " дерева = " + rock + " камня!\n";
        output += "Выберите, что вы хотите обменять:\n1. Дерево\n2. Камень";
        return output;
    }

    public void exchangeWood(int wood) {
        if (wood < this.wood)
            System.out.println("А ну кышь отсюда! Маловато будет!");
        else if (wood > town.getWood())
            System.out.println("А ну кышь отсюда! Врун!");
        else {
            int s = wood / this.wood;
            town.setRock(town.getRock() + rock * s);
            town.setWood(town.getWood() - wood);
        }
    }

    public void exchangeRock(int rock) {
        if (rock < this.rock)
            System.out.println("А ну кышь отсюда! Маловато будет!");
        else if (rock > town.getRock())
            System.out.println("А ну кышь отсюда! Врун!");
        else {
            int s = rock / this.rock;
            town.setWood(town.getWood() + wood * s);
            town.setRock(town.getRock() - rock);
        }
    }

    public void course() {
        rock = ((int) (Math.random() * 5) + 1);
        wood = ((int) (Math.random() * 5) + 1);
    }

}

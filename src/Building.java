import java.util.ArrayList;
import java.util.stream.Stream;

public class Building {

    private int level = 0;
    private String name;
    private ArrayList<Hero> heroes;
    Building(Town town, String name, ArrayList<Hero> heroes) {
        this.name = name;
        this.heroes = heroes;
        level++;
        upgrade(heroes);
    }

    public void levelUp() {
        if (level < 5) {
            level++;
            upgrade(heroes);
        }
    }

    private void upgrade(ArrayList<Hero> heroes) {
        for (Hero hero: heroes) {
            switch (name) {
                case ("hospital"):
                    //Stream.of(hero).map(x -> x.hp + 1).forEach(System.out::print);
                    hero.hp++;
                    break;
            }

        }
    }


}

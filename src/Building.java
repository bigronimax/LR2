import java.io.Serializable;
import java.util.ArrayList;
import java.util.stream.Stream;

public class Building implements Serializable {

    private static final long serialVersionUID = 1L;
    private int level = 0;
    private String name;
    private ArrayList<Hero> heroes;
    Building(String name, ArrayList<Hero> heroes) {
        this.name = name;
        this.heroes = heroes;
        level++;
        upgrade(heroes);
    }

    @Override
    public String toString() {
        String output = name + " " + level;
        return output;
    }

    public void levelUp() {
        level++;
        upgrade(heroes);

    }

    private void upgrade(ArrayList<Hero> heroes) {
        for (Hero hero: heroes) {
            switch (name) {
                case ("hospital"):
                    //Stream.of(hero).map(x -> x.hp + 1).forEach(System.out::print);
                    hero.hp++;
                    break;
                case ("arsenal"):
                    hero.armor++;
                    break;
                case ("forge"):
                    hero.damage++;
                    break;
                case ("tavern_movement"):
                    hero.movement++;
                    break;
                case ("tavern_obstacles"):
                    hero.obstaclesRatio -= 0.1;
                    break;
            }

        }
    }

    public int getLevel() {return level;}

    public void setHeroes(ArrayList<Hero> heroes) {
        for (int i = 0; i < level; i++)
            upgrade(heroes);
    }
}

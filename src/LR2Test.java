import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class LR2Test {

    Battlefield field;

    Town town;
    Menu menu;
    HashMap<Character, Double> newSignsHash;
    ArrayList<Hero> heroes;
    ArrayList<ArrayList<Character>> map;
    HashMap<Character, Enemy> enemiesObjects;
    HashMap<Character, Hero> heroesObjects;
    HashMap<Character, Beast> beastsObjects;
    @BeforeEach
    public void setUp() {

        menu = new Menu();
        town = new Town();
        newSignsHash = new HashMap<>();
    }

    @Test
    public void testHeroesWin() throws IOException {
        heroes = new ArrayList<>(List.of(new Hero("Dominator", menu)));
        field = new Battlefield(10, menu, town, heroes, null, newSignsHash, 0, 0, false);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        Main.battle(field);

        String[] consoleOutput = outputStream.toString().trim().split("\n");
        System.out.println(consoleOutput[consoleOutput.length-1]);
        String s = "Победа героев!";
        Assertions.assertEquals(s, consoleOutput[consoleOutput.length-1]);
        System.setOut(System.out);
    }

    @Test
    public void testEnemiesWin() throws IOException {
        heroes = new ArrayList<>();
        field = new Battlefield(10, menu, town, heroes, null, newSignsHash, 1, 0, false);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        Main.battle(field);

        String[] consoleOutput = outputStream.toString().trim().split("\n");
        System.out.println(consoleOutput[consoleOutput.length-1]);
        String s = "Победа врагов!";
        Assertions.assertEquals(s, consoleOutput[consoleOutput.length-1]);
        System.setOut(System.out);
    }

    @Test
    public void testBeastsWin() throws IOException {
        heroes = new ArrayList<>();
        field = new Battlefield(10, menu, town, heroes, null, newSignsHash, 0, 1, false);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        Main.battle(field);

        String[] consoleOutput = outputStream.toString().trim().split("\n");
        System.out.println(consoleOutput[consoleOutput.length-1]);
        String s = "Победа зверей!";
        Assertions.assertEquals(s, consoleOutput[consoleOutput.length-1]);
        System.setOut(System.out);
    }

    @Test
    public void testBeastsObstacles() {
        map = new ArrayList<>(List.of(
                new ArrayList<Character>(List.of('*', '*', '*', '*', '*', '*', '*')),
                new ArrayList<Character>(List.of('*', '*', '*', '*', '*', '*', '*')),
                new ArrayList<Character>(List.of('*', '*', '*', '*', '*', '*', '*')),
                new ArrayList<Character>(List.of('*', '*', '*', '!', '#', '*', '*')),
                new ArrayList<Character>(List.of('!', '#', '@', '*', '*', '*', '*')),
                new ArrayList<Character>(List.of('!', '*', '!', '*', '*', '*', '*')),
                new ArrayList<Character>(List.of('!', '@', '@', '*', '*', '*', '*'))));
        heroes = new ArrayList<>(List.of(new Hero("Longbow", menu)));
        field = new Battlefield(map.size(), menu, town, heroes, map, newSignsHash, 0, 1, false);
        beastsObjects = field.getBeastsObjects();
        field.changePos(1, 5, beastsObjects.get('A'));
        beastsObjects.get('A').movement = 2;
        int xPos = beastsObjects.get('A').xPos;
        int yPos = beastsObjects.get('A').yPos;
        beastsObjects.get('A').move(field);
        int xPosAfter = beastsObjects.get('A').xPos;
        int yPosAfter = beastsObjects.get('A').yPos;
        Assertions.assertNotEquals(xPos+yPos, xPosAfter+yPosAfter);
    }

    @Test
    public void testObstaclesFalse() {
        map = new ArrayList<>(List.of(
                new ArrayList<Character>(List.of('*', '*', '*')),
                new ArrayList<Character>(List.of('#', '!', '@')),
                new ArrayList<Character>(List.of('*', '*', '*'))));
        heroes = new ArrayList<>(List.of(new Hero("Longbow", menu)));
        field = new Battlefield(map.size(), menu, town, heroes, map, newSignsHash, 0, 0, false);
        if (!(heroes.get(0).xPos == 0 && heroes.get(0).yPos == 0))
            heroes.get(0).move(1, 1, field);
        heroes.get(0).hasMoved = false;
        Assertions.assertFalse(heroes.get(0).move(1, 3, field));
    }

    @Test
    public void testObstaclesTrue() {
        map = new ArrayList<>(List.of(
                new ArrayList<Character>(List.of('*', '*', '*')),
                new ArrayList<Character>(List.of('*', '!', '@')),
                new ArrayList<Character>(List.of('*', '*', '*'))));
        heroes = new ArrayList<>(List.of(new Hero("Longbow", menu)));
        field = new Battlefield(map.size(), menu, town, heroes, map, newSignsHash, 0, 0, false);
        if (!(heroes.get(0).xPos == 0 && heroes.get(0).yPos == 0))
            heroes.get(0).move(1, 1, field);
        heroes.get(0).hasMoved = false;
        Assertions.assertTrue(heroes.get(0).move(1, 3, field));

    }

    @Test
    public void testAttackRangeFalse() {
        map = new ArrayList<>(List.of(
                new ArrayList<Character>(List.of('*', '*', '*')),
                new ArrayList<Character>(List.of('*', '*', '*')),
                new ArrayList<Character>(List.of('*', '*', '*'))));
        heroes = new ArrayList<>(List.of(new Hero("Spearman", menu)));
        field = new Battlefield(map.size(), menu, town, heroes, map, newSignsHash, 1, 0, false);
        enemiesObjects = field.getEnemiesObjects();
        heroes.get(0).move(1, 1, field);
        heroes.get(0).hasMoved = false;
        field.changePos(0, 2, enemiesObjects.get('a'));
        System.out.println(field);
        Assertions.assertFalse(heroes.get(0).attack(enemiesObjects.get('a'), field));
    }

    @Test
    public void testAttackRangeTrue() {
        map = new ArrayList<>(List.of(
                new ArrayList<Character>(List.of('*', '*', '*')),
                new ArrayList<Character>(List.of('*', '*', '*')),
                new ArrayList<Character>(List.of('*', '*', '*'))));
        heroes = new ArrayList<>(List.of(new Hero("Spearman", menu)));
        field = new Battlefield(map.size(), menu, town, heroes, map, newSignsHash, 1, 0, false);
        enemiesObjects = field.getEnemiesObjects();
        heroes.get(0).move(1, 2, field);
        field.changePos(0, 2, enemiesObjects.get('a'));
        System.out.println(field);
        Assertions.assertTrue(heroes.get(0).attack(enemiesObjects.get('a'), field));
    }

    @Test
    public void testFieldMoveToObstacles() {
        map = new ArrayList<>(List.of(
                new ArrayList<Character>(List.of('*', '*', '*')),
                new ArrayList<Character>(List.of('*', '@', '*')),
                new ArrayList<Character>(List.of('*', '*', '*'))));
        heroes = new ArrayList<>(List.of(new Hero("Dominator", menu)));
        field = new Battlefield(map.size(), menu, town, heroes, map, newSignsHash, 1, 0, false);
        Assertions.assertFalse(heroes.get(0).move(2, 2, field));
    }
    @Test
    public void testFieldMoveToEnemies() {
        map = new ArrayList<>(List.of(
                new ArrayList<Character>(List.of('*', '*', '*')),
                new ArrayList<Character>(List.of('*', '@', '*')),
                new ArrayList<Character>(List.of('*', '*', '*'))));
        heroes = new ArrayList<>(List.of(new Hero("Dominator", menu)));
        field = new Battlefield(map.size(), menu, town, heroes, map, newSignsHash, 1, 0, false);
        enemiesObjects = field.getEnemiesObjects();
        field.changePos(0, 2, enemiesObjects.get('a'));
        Assertions.assertFalse(heroes.get(0).move(1, 3, field));
    }

    @Test
    public void testFieldMoveOutside() {
        map = new ArrayList<>(List.of(
                new ArrayList<Character>(List.of('*', '*', '*')),
                new ArrayList<Character>(List.of('*', '@', '*')),
                new ArrayList<Character>(List.of('*', '*', '*'))));
        heroes = new ArrayList<>(List.of(new Hero("Dominator", menu)));
        field = new Battlefield(map.size(), menu, town, heroes, map, newSignsHash, 1, 0, false);
        Assertions.assertFalse(heroes.get(0).move(4, 1, field));
        Assertions.assertFalse(heroes.get(0).move(-1, 1, field));
        Assertions.assertFalse(heroes.get(0).move(4, -1, field));
        Assertions.assertFalse(heroes.get(0).move(1, 4, field));
    }

    @Test
    public void testUnitDeath() {
        map = new ArrayList<>(List.of(
                new ArrayList<Character>(List.of('*', '*', '*')),
                new ArrayList<Character>(List.of('*', '@', '*')),
                new ArrayList<Character>(List.of('*', '*', '*'))));
        heroes = new ArrayList<>(List.of(new Hero("Dominator", menu)));
        field = new Battlefield(map.size(), menu, town, heroes, map, newSignsHash, 1, 0, false);
        enemiesObjects = field.getEnemiesObjects();
        field.changePos(0, 2, enemiesObjects.get('a'));
        heroes.get(0).attack(enemiesObjects.get('a'), field);
        Assertions.assertTrue(!enemiesObjects.containsKey('a'));
    }

    @Test
    public void testUnitArmor() {
        map = new ArrayList<>(List.of(
                new ArrayList<Character>(List.of('*', '*', '*')),
                new ArrayList<Character>(List.of('!', '@', '#')),
                new ArrayList<Character>(List.of('*', '*', '*'))));
        heroes = new ArrayList<>(List.of(new Hero("Dominator", menu)));
        field = new Battlefield(map.size(), menu, town, heroes, map, newSignsHash, 1, 0, false);
        field.getEnemiesObjects().get('a').armor = 5;
        field.getEnemiesObjects().get('a').hp = 10;
        field.getHeroesObjects().get('1').damage = 7;
        field.getHeroesObjects().get('1').attack(field.getEnemiesObjects().get('a'), field);
        Assertions.assertEquals(8, field.getEnemiesObjects().get('a').hp);
        Assertions.assertEquals(0, field.getEnemiesObjects().get('a').armor);
    }

    @Test
    public void testBuyingHeroesFalse() {
        ArrayList<String> heroesNames = new ArrayList<>(List.of("Axeman", "Axeman", "Axeman"));
        Assertions.assertFalse(Main.checkHeroesCost(menu, heroesNames));
    }

    @Test
    public void testBuyingHeroesTrue() {
        ArrayList<String> heroesNames = new ArrayList<>(List.of("Axeman", "Axeman"));
        Assertions.assertTrue(Main.checkHeroesCost(menu, heroesNames));
    }

    @Test
    public void testEnemyAttack() {
        map = new ArrayList<>(List.of(
                new ArrayList<Character>(List.of('*', '*', '*')),
                new ArrayList<Character>(List.of('*', '*', '*')),
                new ArrayList<Character>(List.of('*', '*', '*'))));
        heroes = new ArrayList<>(List.of(new Hero("Dominator", menu)));
        field = new Battlefield(map.size(), menu, town, heroes, map, newSignsHash, 1, 0, false);
        heroesObjects = field.getHeroesObjects();
        field.getEnemiesObjects().get('a').attackRange = 3;
        field.getEnemiesObjects().get('a').damage = 10;
        beastsObjects = new HashMap<>();
        Main.enemiesBehavior(heroesObjects, field.getEnemiesObjects(), beastsObjects, field, "");
        Assertions.assertEquals(90, heroesObjects.get('1').hp);
    }

    @Test
    public void testEnemyMove() {
        map = new ArrayList<>(List.of(
                new ArrayList<Character>(List.of('*', '*', '*')),
                new ArrayList<Character>(List.of('*', '*', '*')),
                new ArrayList<Character>(List.of('*', '*', '*'))));
        heroes = new ArrayList<>(List.of(new Hero("Dominator", menu)));
        field = new Battlefield(map.size(), menu, town, heroes, map, newSignsHash, 1, 0, false);
        heroesObjects = field.getHeroesObjects();
        heroesObjects.get('1').move(1, 1, field);
        enemiesObjects = field.getEnemiesObjects();
        enemiesObjects.get('a').attackRange = 1;
        field.changePos(2, 2, enemiesObjects.get('a'));
        beastsObjects = new HashMap<>();
        Main.enemiesBehavior(heroesObjects, enemiesObjects, beastsObjects, field, "");
        Assertions.assertEquals(2, enemiesObjects.get('a').xPos);
        Assertions.assertEquals(0, enemiesObjects.get('a').yPos);
    }

    @Test
    public void testMapVisualization() {
        map = new ArrayList<>(List.of(
                new ArrayList<Character>(List.of('*', '*', '*')),
                new ArrayList<Character>(List.of('*', '*', '*')),
                new ArrayList<Character>(List.of('*', '*', '*'))));
        heroes = new ArrayList<>(List.of(new Hero("Dominator", menu)));
        field = new Battlefield(map.size(), menu, town, heroes, map, newSignsHash, 1, 0, false);
        heroesObjects = field.getHeroesObjects();
        heroesObjects.get('1').move(1, 1, field);
        enemiesObjects = field.getEnemiesObjects();
        enemiesObjects.get('a').attackRange = 1;
        field.changePos(2, 2, enemiesObjects.get('a'));
        beastsObjects = new HashMap<>();
        Main.enemiesBehavior(heroesObjects, enemiesObjects, beastsObjects, field, "");
        map = new ArrayList<>(List.of(
                new ArrayList<Character>(List.of('1', '*', 'a')),
                new ArrayList<Character>(List.of('*', '*', '*')),
                new ArrayList<Character>(List.of('*', '*', '*'))));
        Assertions.assertEquals(map, field.getField());
    }
    
}

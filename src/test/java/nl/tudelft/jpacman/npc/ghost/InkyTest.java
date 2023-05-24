package nl.tudelft.jpacman.npc.ghost;

import nl.tudelft.jpacman.board.BoardFactory;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.level.Level;
import nl.tudelft.jpacman.level.LevelFactory;
import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.level.PlayerFactory;
import nl.tudelft.jpacman.points.DefaultPointCalculator;
import nl.tudelft.jpacman.points.PointCalculator;
import nl.tudelft.jpacman.sprite.PacManSprites;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Testing nextAiMove() method of the Inky class.
 */
public class InkyTest {

    private static final PacManSprites SPRITES = new PacManSprites();
    private LevelFactory levelFactory;
    private GhostFactory ghostFactory;
    private PlayerFactory playerFactory;
    private BoardFactory boardFactory;
    private GhostMapParser ghostMapParser;
    private PointCalculator calculator;

    /**
     * Initialize the game.
     */
    @BeforeEach
    void setup() {
        calculator = new DefaultPointCalculator();
        playerFactory = new PlayerFactory(SPRITES);
        boardFactory = new BoardFactory(SPRITES);
        ghostFactory = new GhostFactory(SPRITES);
        levelFactory = new LevelFactory(SPRITES, ghostFactory, calculator);
        ghostMapParser = new GhostMapParser(levelFactory, boardFactory, ghostFactory);
    }


    /**
     * 没有Blinky对象
     */
    @Test
    void testNoBlinky() {

        Level level = ghostMapParser.parseMap(
            Lists.newArrayList("P.I", "###", "###")
        );
        Player player = playerFactory.createPacMan();
        player.setDirection(Direction.WEST);
        level.registerPlayer(player);
        Inky inky = Navigation.findUnitInBoard(Inky.class, level.getBoard());

        assertThat(inky.nextAiMove()).isEqualTo(Optional.empty());
    }

    /**
     * Pacman和Inky之间没有通路
     */
    @Test
    void testNoPathBetweenPacmanAndInky() {

        List<String> grid = new ArrayList<>();
        grid.add("#######################");
        grid.add("##########P......#I..B#");
        grid.add("#######################");

        Level level = ghostMapParser.parseMap(grid);
        Player player = playerFactory.createPacMan();
        player.setDirection(Direction.WEST);
        level.registerPlayer(player);
        Inky inky = Navigation.findUnitInBoard(Inky.class, level.getBoard());

        assertThat(inky.nextAiMove()).isEqualTo(Optional.empty());

    }


    /**
     * 没有Pacman对象
     */
    @Test
    void testNoPacman() {
        Level level = ghostMapParser.parseMap(
            Lists.newArrayList("#####", "#B.I#", "#####")
        );
        Inky inky = Navigation.findUnitInBoard(Inky.class, level.getBoard());

        assertThat(inky.nextAiMove()).isEqualTo(Optional.empty());
    }


    /**
     * Inky跟在Pacman后面，Blinky在后面。
     */
    @Test
    void testGoTowardsPacman() {

        List<String> grid = new ArrayList<>();
        grid.add("#######################");
        grid.add("######.....P....B...I.#");
        grid.add("#######################");

        Level level = ghostMapParser.parseMap(grid);
        Player player = playerFactory.createPacMan();
        player.setDirection(Direction.EAST);
        level.registerPlayer(player);
        Inky inky = Navigation.findUnitInBoard(Inky.class, level.getBoard());
        assertThat(inky.nextAiMove()).isEqualTo(Optional.of(Direction.WEST));
    }


    /**
     * 当Inky站在Pacman面前时，他会离开。
     */
    @Test
    void testInkyMovesAway() {

        List<String> grid = new ArrayList<>();
        grid.add("#######################");
        grid.add("##.....B.P.I.....######");
        grid.add("#######################");

        Level level = ghostMapParser.parseMap(grid);
        Player player = playerFactory.createPacMan();
        player.setDirection(Direction.EAST);
        level.registerPlayer(player);
        Inky inky = Navigation.findUnitInBoard(Inky.class, level.getBoard());

        assertThat(inky.nextAiMove()).isEqualTo(Optional.of(Direction.EAST));

    }
}

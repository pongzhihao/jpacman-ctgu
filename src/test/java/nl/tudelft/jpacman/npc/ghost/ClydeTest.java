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

import java.util.Optional;

/**
 * Class that tests the nextAiMove() method in the Clyde class.
 */
public class ClydeTest {

    private PacManSprites pacManSprites;
    private BoardFactory boardFactory;
    private GhostFactory ghostFactory;
    private PointCalculator pointCalculator;
    private LevelFactory levelFactory;
    private GhostMapParser ghostMapParser;
    private PlayerFactory playerFactory;

    /**实例化GhostMapParser
     */
    @BeforeEach
    void init() {
        pacManSprites = new PacManSprites();
        boardFactory = new BoardFactory(pacManSprites);
        ghostFactory = new GhostFactory(pacManSprites);
        pointCalculator = new DefaultPointCalculator();
        levelFactory = new LevelFactory(pacManSprites, ghostFactory, pointCalculator);
        playerFactory = new PlayerFactory(pacManSprites);

        ghostMapParser = new GhostMapParser(levelFactory, boardFactory, ghostFactory);
    }


    /**
     * 测试中Clyde位于Pacman附近（距离8平方米），它会试图离开
     */
    @Test
    void testTooClose() {
        Level level = ghostMapParser.parseMap(
            Lists.newArrayList("############", "##P.....C###", "############")
        );
        Player player = playerFactory.createPacMan();
        player.setDirection(Direction.WEST);
        level.registerPlayer(player);
        Clyde clyde = Navigation.findUnitInBoard(Clyde.class, level.getBoard());

        assertThat(clyde.nextAiMove()).isEqualTo(Optional.of(Direction.EAST));
    }


    /**
     * 测试中，Clyde距离Pacman很远（在9平方米的距离上），它将试图走向吃豆人
     */
    @Test
    void testTooFar() {
        Level level = ghostMapParser.parseMap(
            Lists.newArrayList("############", "P.........C#", "############")
        );
        Player player = playerFactory.createPacMan();
        player.setDirection(Direction.EAST);
        level.registerPlayer(player);
        Clyde clyde = Navigation.findUnitInBoard(Clyde.class, level.getBoard());

        assertThat(clyde.nextAiMove()).isEqualTo(Optional.of(Direction.WEST));
    }

    /**
     * 没有Pacman
     */
    @Test
    void testNoPacman() {
        Level level = ghostMapParser.parseMap(
            Lists.newArrayList("#####", "##C..", "     ")
        );
        Clyde clyde = Navigation.findUnitInBoard(Clyde.class, level.getBoard());

        assertThat(clyde.nextAiMove()).isEqualTo(Optional.empty());
    }


    /**
     * PacMan和Clyde之间有障碍，没有路径
     */
    @Test
    void testNoPathBetweenPacmanAndClyde() {
        Level level = ghostMapParser.parseMap(
            Lists.newArrayList("######", "#P##C.", " ###  ")
        );
        Player player = playerFactory.createPacMan();
        player.setDirection(Direction.EAST);
        level.registerPlayer(player);
        Clyde clyde = Navigation.findUnitInBoard(Clyde.class, level.getBoard());

        assertThat(clyde.nextAiMove()).isEqualTo(Optional.empty());
    }
}

package nl.tudelft.jpacman.level;

import nl.tudelft.jpacman.PacmanConfigurationException;
import nl.tudelft.jpacman.board.BoardFactory;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.npc.Ghost;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Testing the MapParser class using Mock objects.
 */
public class MapParserTest {

    private LevelFactory levelFactory;
    private BoardFactory boardFactory;
    private MapParser mapParser;
    private Pellet pellet;
    private Square ground;
    private Square wall;
    private Ghost ghost;


    /**
     * 初始化
     */
    @BeforeEach
    public void init() {
        levelFactory = Mockito.mock(LevelFactory.class);
        boardFactory = Mockito.mock(BoardFactory.class);
        ground = Mockito.mock(Square.class); //占地
        wall = Mockito.mock(Square.class); //墙
        pellet = Mockito.mock(Pellet.class); //吃豆人
        ghost = Mockito.mock(Ghost.class); //鬼

        Mockito.when(boardFactory.createGround()).thenReturn(ground);
        Mockito.when(levelFactory.createPellet()).thenReturn(pellet);
        Mockito.when(boardFactory.createWall()).thenReturn(wall);
        Mockito.when(levelFactory.createGhost()).thenReturn(ghost);

        mapParser = new MapParser(levelFactory, boardFactory);
    }

    /**
     * 不存在文件
     */
    @Test
    @DisplayName("不存在文件")
    void testNoSuchFile() {
        assertThrows(PacmanConfigurationException.class, () ->
            mapParser.parseMap("/random.txt")
        );
    }



    /**
     * 测试null
     */
    @Test
    @DisplayName("null测试")
    void testParseMapExceptionNull() {
        List<String> map = null;
        assertThrows(PacmanConfigurationException.class, () ->
            mapParser.parseMap(map)
        );
    }


    /**
     * 空地图
     */
    @Test
    @DisplayName("空地图")
    void testParseMapExceptionNoText() {
        List<String> map = new ArrayList<>();
        assertThrows(PacmanConfigurationException.class, () ->
            mapParser.parseMap(map)
        );
    }

    /**
     * 没有占地
     */
    @Test
    @DisplayName("地图没有占地")
    void testParseMapExceptionEmptyLine() {
        List<String> map = Arrays.asList("");
        assertThrows(PacmanConfigurationException.class, () ->
            mapParser.parseMap(map)
        );
    }

    /**
     * 每行长度不一致
     */
    @Test
    @DisplayName("每行长度不一致")
    void testParseMapNotEqualLengthOfLines() {
        List<String> map = Arrays.asList(
            "PG#",
            "#"
        );
        assertThrows(PacmanConfigurationException.class, () ->
            mapParser.parseMap(map)
        );
    }


    /**
     * 存在非法字符
     */
    @Test
    @DisplayName("存在非法字符")
    void testParseMapForbiddenCharacter() {
        List<String> map = Arrays.asList(
            " A "
        );
        assertThrows(PacmanConfigurationException.class, () ->
            mapParser.parseMap(map)
        );
    }

    /**
     * 测试存在鬼和吃豆人的地图
     * @throws IOException when the resource could not be read
     * @throws PacmanConfigurationException when the file is not found
     */
    @Test
    @DisplayName("存在鬼和吃豆人的地图")
    void testParseMapGoodWithGhostAndPelletTest() throws IOException, PacmanConfigurationException {
        mapParser.parseMap("/simplemap.txt");
        List<Square> expStart = Arrays.asList(ground);

        Mockito.verify(boardFactory, Mockito.times(4)).createGround();
        Mockito.verify(boardFactory, Mockito.times(2)).createWall();
        Mockito.verify(levelFactory, Mockito.times(1)).createPellet();
        Mockito.verify(levelFactory, Mockito.times(1)).createGhost();
        Mockito.verify(pellet, Mockito.times(1)).occupy(ground);

        Mockito.verify(boardFactory, Mockito.times(1)).createBoard(Mockito.any());
        Mockito.verify(levelFactory, Mockito.times(1)).createLevel(
            Mockito.any(),
            Mockito.any(),
            Mockito.eq(expStart)
        );

        Mockito.verifyNoMoreInteractions(boardFactory);
        Mockito.verifyNoMoreInteractions(levelFactory);
    }


    /**
     * 测试只有吃豆人和占地
     * @throws IOException when the resource could not be read
     * @throws PacmanConfigurationException when the file is not found
     */
    @Test
    @DisplayName("只有吃豆人和占地的地图")
    void testSpaces() throws IOException, PacmanConfigurationException {
        mapParser.parseMap("/testSpaces.txt");
        List<Square> starts = Arrays.asList(ground);

        Mockito.verify(boardFactory, Mockito.times(3)).createGround();

        Mockito.verify(boardFactory, Mockito.times(1)).createBoard(Mockito.any());
        Mockito.verify(levelFactory, Mockito.times(1)).createLevel(
            Mockito.any(),
            Mockito.any(),
            Mockito.eq(starts)
        );

        Mockito.verifyNoMoreInteractions(boardFactory);
        Mockito.verifyNoMoreInteractions(levelFactory);
    }


    /**
     * 测试只有吃豆人和墙
     * @throws IOException when the resource could not be read
     * @throws PacmanConfigurationException when the file could not be found
     */
    @Test
    @DisplayName("只有吃豆人和墙的地图")
    void testWalls() throws IOException, PacmanConfigurationException {
        mapParser.parseMap("/testWalls.txt");
        List<Square> starts = Arrays.asList(ground);

        Mockito.verify(boardFactory, Mockito.times(2)).createWall();
        Mockito.verify(boardFactory, Mockito.times(1)).createGround();

        Mockito.verify(boardFactory, Mockito.times(1)).createBoard(Mockito.any());
        Mockito.verify(levelFactory, Mockito.times(1)).createLevel(
            Mockito.any(),
            Mockito.any(),
            Mockito.eq(starts)
        );

        Mockito.verifyNoMoreInteractions(boardFactory);
        Mockito.verifyNoMoreInteractions(levelFactory);
    }


    /**
     * 测试只有鬼和吃豆人
     * @throws IOException when the resource could not be read
     * @throws PacmanConfigurationException when the file is not found
     */
    @Test
    @DisplayName("只有鬼和吃豆人的地图")
    void testGhostsPlayers() throws IOException, PacmanConfigurationException {
        mapParser.parseMap("/testGhostsPlayers.txt");
        List<Square> starts = Arrays.asList(ground);

        Mockito.verify(boardFactory, Mockito.times(2)).createGround();
        Mockito.verify(levelFactory, Mockito.times(1)).createGhost();
        Mockito.verify(ghost, Mockito.times(1)).occupy(ground);

        Mockito.verify(boardFactory, Mockito.times(1)).createBoard(Mockito.any());
        Mockito.verify(levelFactory, Mockito.times(1)).createLevel(
            Mockito.any(),
            Mockito.any(),
            Mockito.eq(starts)
        );

        Mockito.verifyNoMoreInteractions(boardFactory);
        Mockito.verifyNoMoreInteractions(levelFactory);
    }

}

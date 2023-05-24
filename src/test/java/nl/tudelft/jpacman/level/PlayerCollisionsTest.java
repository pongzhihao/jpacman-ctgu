package nl.tudelft.jpacman.level;

import nl.tudelft.jpacman.npc.Ghost;
import nl.tudelft.jpacman.points.PointCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


public class PlayerCollisionsTest {

    /**
     * 实例化一个player,一个pellet,一个ghost和PlayerCollisions对象用于测试
     */
    private PlayerCollisions playerCollisions;
    private Player player;
    private Pellet pellet;
    private Ghost ghost;
    private PointCalculator pointCalculator;
    @BeforeEach
    void setup() {
        pointCalculator = Mockito.mock(PointCalculator.class);
        playerCollisions = new PlayerCollisions(pointCalculator);
        player = Mockito.mock(Player.class);
        pellet = Mockito.mock(Pellet.class);
        ghost = Mockito.mock(Ghost.class);
    }
    /**
     * 测试第一个参数是Player对象，第二个参数为Player对象，两者碰撞测试结果没有反应
     */
    @Test
    @DisplayName("Player与Player相碰撞")
    void testPlayerPlayer() {
        Player player1 = Mockito.mock(Player.class);
        playerCollisions.collide(player, player1);
        Mockito.verifyNoInteractions(player,player1);
    }



    /**
     * 测试第一个参数是Player，第二个参数为Pellet，两者碰撞导致Pellet被吃掉
     */
    @Test
    @DisplayName("Player与Pellet相碰撞")
    void testPlayerPellet() {
        playerCollisions.collide(player, pellet);

        Mockito.verify(pointCalculator, Mockito.times(1)).consumedAPellet( // 验证是否执行吃豆
            Mockito.eq(player),
            Mockito.eq(pellet)
        );

        Mockito.verify(pellet, Mockito.times(1)).leaveSquare(); // 验证豆子是否被吃掉

        Mockito.verifyNoMoreInteractions(player, pellet); // 验证之后豆子和吃豆人碰撞没有反应
    }


    /**
     * 测试第一个参数是Player，第二个参数为Ghost，两者碰撞导致Player死亡
     */
    @Test
    @DisplayName("Player与Ghost相碰撞")
    void testPlayerGhost() {
        playerCollisions.collide(player, ghost);

        Mockito.verify(pointCalculator, Mockito.times(1)).collidedWithAGhost( // 验证player与ghost之间是否发生碰撞
            Mockito.eq(player),
            Mockito.eq(ghost)
        );

        Mockito.verify(player, Mockito.times(1)).setAlive(false); // 验证player是否死亡

        Mockito.verify(player, Mockito.times(1)).setKiller(Mockito.eq(ghost)); // 验证player是否是被与其发生碰撞的ghost杀害

        Mockito.verifyNoMoreInteractions(player, ghost);
    }


    /**
     * 测试第一个参数是Ghost，第二个参数为Player，两者碰撞导致Player死亡
     */
    @Test
    @DisplayName("Ghost与Player相碰撞")
    void testGhostPlayer() {
        playerCollisions.collide(ghost, player);

        Mockito.verify(pointCalculator, Mockito.times(1)).collidedWithAGhost(
            Mockito.eq(player),
            Mockito.eq(ghost)
        );

        Mockito.verify(player, Mockito.times(1)).setAlive(false);

        Mockito.verify(player, Mockito.times(1)).setKiller(Mockito.eq(ghost));

        Mockito.verifyNoMoreInteractions(player, ghost);
    }


    /**
     * 测试第一个参数是Ghost，第二个参数为Pellet，两者碰撞结果没有反应
     */
    @Test
    @DisplayName("Ghost与Pellet相碰撞")
    void testGhostPellet() {
        playerCollisions.collide(ghost, pellet);

        Mockito.verifyNoInteractions(ghost, pellet);
    }


    /**
     * 测试第一个参数是Ghost，第二个参数也为Ghost，两者碰撞结果没有反应
     */
    @Test
    @DisplayName("Ghost与Ghost相碰撞")
    void testGhostGhost() {
        Ghost ghost1 = Mockito.mock(Ghost.class);
        playerCollisions.collide(ghost, ghost1);

        Mockito.verifyNoInteractions(ghost,ghost1);
    }


    /**
     * 测试第一个参数是Pellet，第二个参数也为Player，两者碰撞导致Pellet被吃掉
     */
    @Test
    @DisplayName("Pellet与Player相碰撞")
    void testPelletPlayer() {
        playerCollisions.collide(pellet, player);

        Mockito.verify(pointCalculator, Mockito.times(1)).consumedAPellet(
            Mockito.eq(player),
            Mockito.eq(pellet)
        );

        Mockito.verify(pellet, Mockito.times(1)).leaveSquare();

        Mockito.verifyNoMoreInteractions(pellet, player);
    }


    /**
     * 测试第一个参数是Pellet，第二个参数也为Ghost，两者碰撞结果没有反应
     */
    @Test
    @DisplayName("Pellet与Ghost相碰撞")
    void testPelletGhost() {
        playerCollisions.collide(pellet, ghost);

        Mockito.verifyZeroInteractions(pellet, ghost);
    }


    /**
     * 测试第一个参数是Pellet，第二个参数也为Pellet，两者之间不会发生碰撞
     */
    @Test
    @DisplayName("Pellet与Pellet相碰撞")
    void testPelletPellet() {
        Pellet pellet1 = Mockito.mock(Pellet.class);
        playerCollisions.collide(pellet, pellet1);

        Mockito.verifyNoInteractions(pellet, pellet1);
    }
}


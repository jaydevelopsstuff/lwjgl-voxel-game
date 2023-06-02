package net.jay.voxelgame.world.block;

import net.jay.voxelgame.api.block.BlockType;
import org.joml.Vector2f;

public class GrassBlock extends Block {
    private static final float xStartTop = 0.5f;
    private static final float xEndTop = 1f;
    private static final float yStartTop = 0.5f;
    private static final float yEndTop = 1f;
    private static final float xStartBottom = 0f;
    private static final float xEndBottom = 0.5f;
    private static final float yStartBottom = 0f;
    private static final float yEndBottom = 0.5f;
    private static final float xStartSide = 0f;
    private static final float xEndSide = 0.5f;
    private static final float yStartSide = 0.5f;
    private static final float yEndSide = 1f;
    
    public static final Vector2f[] texCoordsUp = new Vector2f[] {
            new Vector2f(xStartTop, yEndTop), new Vector2f(xEndTop, yEndTop),
            new Vector2f(xEndTop, yStartTop), new Vector2f(xStartTop, yStartTop)
    };
    public static final Vector2f[] texCoordsDown = new Vector2f[] {
            new Vector2f(xStartBottom, yStartBottom), new Vector2f(xEndBottom, yStartBottom),
            new Vector2f(xEndBottom, yEndBottom), new Vector2f(xStartBottom, yEndBottom)
    };
    public static final Vector2f[] texCoordsNorth = new Vector2f[] {
            new Vector2f(xStartSide, yEndSide), new Vector2f(xStartSide, yStartSide),
            new Vector2f(xEndSide, yStartSide), new Vector2f(xEndSide, yEndSide)
    };
    public static final Vector2f[] texCoordsEast = new Vector2f[] {
            new Vector2f(xStartSide, yEndSide), new Vector2f(xStartSide, yStartSide),
            new Vector2f(xEndSide, yStartSide), new Vector2f(xEndSide, yEndSide)
    };
    public static final Vector2f[] texCoordsSouth = new Vector2f[] {
            new Vector2f(xEndSide, yEndSide), new Vector2f(xEndSide, yStartSide),
            new Vector2f(xStartSide, yStartSide), new Vector2f(xStartSide, yEndSide)
    };
    public static final Vector2f[] texCoordsWest = new Vector2f[] {
            new Vector2f(xEndSide, yEndSide), new Vector2f(xEndSide, yStartSide),
            new Vector2f(xStartSide, yStartSide), new Vector2f(xStartSide, yEndSide)
    };

    public GrassBlock() {
        super(BlockType.Grass, texCoordsUp, texCoordsDown, texCoordsNorth, texCoordsEast, texCoordsSouth, texCoordsWest);
    }
}

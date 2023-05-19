package net.jay.voxelgame.world.block;

public class DirtBlock extends Block {
    private static final float xStart = 0f;
    private static final float xEnd = 0.5f;
    private static final float yStart = 0f;
    private static final float yEnd = 0.5f;

    public DirtBlock() {
        super(Type.Dirt, xStart, xEnd, yStart, yEnd);
    }
}

package net.jay.voxelgame.world.block;

import net.jay.voxelgame.api.block.BlockType;

public class StoneBlock extends Block {
    private static final float xStart = 0.5f;
    private static final float xEnd = 1f;
    private static final float yStart = 0f;
    private static final float yEnd = 0.5f;

    public StoneBlock() {
        super(BlockType.Stone, xStart, xEnd, yStart, yEnd);
    }
}

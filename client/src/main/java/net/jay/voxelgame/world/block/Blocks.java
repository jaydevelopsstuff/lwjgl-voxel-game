package net.jay.voxelgame.world.block;

import net.jay.voxelgame.api.block.BlockType;

public class Blocks {
    public static final Block
            Air = new AirBlock(),
            Dirt = new DirtBlock(),
            Stone = new StoneBlock(),
            Grass = new GrassBlock();

    public static Block getBlock(BlockType type) {
        switch(type) {
            case Air:
                return Air;
            case Dirt:
                return Dirt;
            case Stone:
                return Stone;
            case Grass:
                return Grass;
            default:
                return null;
        }
    }
}

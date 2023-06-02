package net.jay.voxelgame.api.block;

public enum BlockType {
    Air(0),
    Dirt(1),
    Grass(2),
    Stone(3);

    public final int id;

    BlockType(int id) {
        this.id = id;
    }

    public static BlockType fromId(int id) {
        for(BlockType value : values()) {
            if(value.id == id)
                return value;
        }
        return null;
    }
}

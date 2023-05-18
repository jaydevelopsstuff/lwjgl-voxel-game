package net.jay.voxelgame.world;

import net.jay.voxelgame.render.Mesh;

import java.util.Arrays;

public class World {
    public Block[][][] blocks;

    public World() {
        this.blocks = new Block[100][32][100];
    }

    public void generateBlocks() {
        for(int x = 0; x < blocks.length; x++) {
            for(int y = 0; y < blocks[x].length; y++) {
                for(int z = 0; z < blocks[x][y].length; z++) {
                    if(y < 15) {
                        Arrays.fill(blocks[x][y], new Block(BlockType.Dirt));
                    } else {
                        Arrays.fill(blocks[x][y], Block.AIR);
                    }
                }
            }
        }
    }

    public Mesh generateMesh() {
        Mesh mesh = new Mesh();
        for(int x = 0; x < blocks.length; x++) {
            for(int y = 0; y < blocks[x].length; y++) {
                for(int z = 0; z < blocks[x][y].length; z++) {
                    Block block = blocks[x][y][z];
                    if(block != Block.AIR) {
                        // Face culling implementation
                        Block right = x == blocks.length - 1 ? Block.AIR : blocks[x + 1][y][z];
                        Block left = x == 0 ? Block.AIR : blocks[x - 1][y][z];
                        Block top = y == blocks[x].length - 1 ? Block.AIR : blocks[x][y + 1][z];
                        Block bottom = y == 0 ? Block.AIR : blocks[x][y - 1][z];
                        Block front = z == blocks[x][y].length - 1 ? Block.AIR : blocks[x][y][z + 1];
                        Block back = z == 0 ? Block.AIR : blocks[x][y][z - 1];

                        if(right == Block.AIR) {
                            block.faceDataEast(x, y, z, mesh);
                        }
                        if(left == Block.AIR) {
                            block.faceDataWest(x, y, z, mesh);
                        }
                        if(top == Block.AIR) {
                            block.faceDataUp(x, y, z, mesh);
                        }
                        if(bottom == Block.AIR) {
                            block.faceDataDown(x, y, z, mesh);
                        }
                        if(front == Block.AIR) {
                            block.faceDataNorth(x, y, z, mesh);
                        }
                        if(back == Block.AIR) {
                            block.faceDataSouth(x, y, z, mesh);
                        }
                    }
                }
            }
        }
        return mesh;
    }
}

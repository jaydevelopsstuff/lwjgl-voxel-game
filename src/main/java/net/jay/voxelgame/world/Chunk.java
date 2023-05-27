package net.jay.voxelgame.world;

import net.jay.voxelgame.render.gl.Mesh;
import net.jay.voxelgame.render.gl.vertex.TextureVertex;
import net.jay.voxelgame.world.block.Block;
import net.jay.voxelgame.world.block.Blocks;
import org.joml.Vector3i;

import java.util.Arrays;

public class Chunk {
    private final Block[][][] blocks;

    public Chunk() {
        this.blocks = new Block[16][World.Height][16];

        for(int x = 0; x < blocks.length; x++) {
            for(int y = 0; y < blocks[x].length; y++) {
                for(int z = 0; z < blocks[x][y].length; z++) {
                    if(y == 0) {
                        Arrays.fill(blocks[x][y], Blocks.Stone);
                    } else if(y == 3) {
                        Arrays.fill(blocks[x][y], Blocks.Grass);
                    } else if(y < 3) {
                        Arrays.fill(blocks[x][y], Blocks.Dirt);
                    } else {
                        Arrays.fill(blocks[x][y], Blocks.Air);
                    }
                }
            }
        }
    }

    public void generateMesh(int xOffset, int zOffset, Mesh<TextureVertex> mesh) {
        for(int x = 0; x < blocks.length; x++) {
            for(int y = 0; y < blocks[x].length; y++) {
                for(int z = 0; z < blocks[x][y].length; z++) {
                    Block block = blocks[x][y][z];
                    if(block != Blocks.Air) {
                        // Face culling implementation
                        Block right = x == blocks.length - 1 ? Blocks.Air : blocks[x + 1][y][z];
                        Block left = x == 0 ? Blocks.Air : blocks[x - 1][y][z];
                        Block top = y == blocks[x].length - 1 ? Blocks.Air : blocks[x][y + 1][z];
                        Block bottom = y == 0 ? Blocks.Air : blocks[x][y - 1][z];
                        Block front = z == blocks[x][y].length - 1 ? Blocks.Air : blocks[x][y][z + 1];
                        Block back = z == 0 ? Blocks.Air : blocks[x][y][z - 1];

                        if(right == Blocks.Air) {
                            block.faceDataEast(xOffset + x, y, zOffset + z, mesh);
                        }
                        if(left == Blocks.Air) {
                            block.faceDataWest(xOffset + x, y, zOffset + z, mesh);
                        }
                        if(top == Blocks.Air) {
                            block.faceDataUp(xOffset + x, y, zOffset + z, mesh);
                        }
                        if(bottom == Blocks.Air) {
                            block.faceDataDown(xOffset + x, y, zOffset + z, mesh);
                        }
                        if(front == Blocks.Air) {
                            block.faceDataNorth(xOffset + x, y, zOffset + z, mesh);
                        }
                        if(back == Blocks.Air) {
                            block.faceDataSouth(xOffset + x, y, zOffset + z, mesh);
                        }
                    }
                }
            }
        }
    }

    public Block blockAt(int x, int y, int z) {
        return blocks[x][y][z];
    }

    public Block blockAt(Vector3i position) {
        return blockAt(position.x, position.y, position.z);
    }

    public void setBlock(Block block, int x, int y, int z) {
        blocks[x][y][z] = block;
    }

    public void setBlock(Block block, Vector3i position) {
        setBlock(block, position.x, position.y, position.z);
    }

    public Block[][][] blocks() {
        return blocks;
    }
}

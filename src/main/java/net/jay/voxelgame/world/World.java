package net.jay.voxelgame.world;

import net.jay.voxelgame.render.gl.Mesh;
import net.jay.voxelgame.render.gl.vertex.TextureVertex;
import net.jay.voxelgame.util.MathUtil;
import net.jay.voxelgame.world.block.Block;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector3i;

import static net.jay.voxelgame.util.MathUtil.round;

public class World {
    public static final int Height = 64;

    private final Chunk[][] loadedChunks;

    public World() {
        this.loadedChunks = new Chunk[8][8];
        for(Chunk[] chunks : loadedChunks) {
            for(int i = 0; i < chunks.length; i++) {
                chunks[i] = new Chunk();
            }
        }
    }

    public Chunk[][] getLoadedChunks() {
        return loadedChunks;
    }

    public void generateBlocks() {

    }

    public Mesh<TextureVertex> generateMesh() {
        Mesh<TextureVertex> mesh = new Mesh<>(true);

        for(int chunkX = 0; chunkX < loadedChunks.length; chunkX++) {
            for(int chunkZ = 0; chunkZ < loadedChunks[chunkX].length; chunkZ++) {
                Chunk chunk = loadedChunks[chunkX][chunkZ];

                chunk.generateMesh((chunkX - loadedChunks.length / 2) * 16, (chunkZ - loadedChunks[0].length / 2) * 16, mesh);
            }
        }

        return mesh;
    }

    public Vector3i rayTrace(Vector3f origin, Vector3f direction, Vector3i beforeIntercept, float maxDistance) {
        double moveX = direction.x / 6;
        double moveY = direction.y / 6;
        double moveZ = direction.z / 6;

        double rayX = origin.x;
        double rayY = origin.y;
        double rayZ = origin.z;

        double prevRayX, prevRayY, prevRayZ;

        double distanceTraveled = 0;
        while(distanceTraveled <= maxDistance) {
            prevRayX = rayX;
            prevRayY = rayY;
            prevRayZ = rayZ;

            rayX += moveX;
            rayY += moveY;
            rayZ += moveZ;

            Chunk chunk = getChunk(rayX, rayZ);
            if(chunk == null)
                return null;

            if(round(rayY) < 0 || rayY >= World.Height)
                continue;

            Block block = blockAt(round(rayX), round(rayY), round(rayZ));
            if(block.type() != Block.Type.Air) {
                beforeIntercept.x = round(prevRayX);
                beforeIntercept.y = round(prevRayY);
                beforeIntercept.z = round(prevRayZ);
                System.out.println(getChunkCoords(rayX, rayZ));
                return new Vector3i(round(rayX), round(rayY), round(rayZ));
            }

            distanceTraveled += Math.sqrt(moveX*moveX + moveY*moveY + moveZ*moveZ);
        }

        return null;
    }

    public Block blockAt(int x, int y, int z) {
        Chunk chunk = getChunk(x, z);
        Vector3f chunkRelativeCoords = toChunkRelativeCoords(x, y, z);

        // System.out.println(chunkRelativeCoords.x + "," + chunkRelativeCoords.z);
        return chunk.blockAt(round(chunkRelativeCoords.x), y, MathUtil.round(chunkRelativeCoords.z));
    }

    public void setBlock(Block block, int x, int y, int z) {
        Chunk chunk = getChunk(x, z);
        Vector3f chunkRelativeCoords = toChunkRelativeCoords(x, y, z);

        chunk.setBlock(block, MathUtil.round(chunkRelativeCoords.x), y, MathUtil.round(chunkRelativeCoords.z));
    }

    public Chunk getChunk(double worldX, double worldZ) {
        Vector2i coords = getChunkCoords(worldX, worldZ);
        //if(worldX < 0 || worldX >= loadedChunks.length || worldZ < 0 || worldZ >= loadedChunks[0].length)
        //    return null;
        return loadedChunks[coords.x][coords.y];
    }

    public Vector2i getChunkCoords(double worldX, double worldZ) {
        int chunkX;
        int chunkZ;
        chunkX = round(Math.floor(worldX / 16) + loadedChunks.length / 2);
        chunkZ = round(Math.floor(worldZ / 16) + loadedChunks[0].length / 2);
        if(chunkX < 0 || chunkX > loadedChunks.length - 1 || chunkZ < 0 || chunkZ > loadedChunks[0].length - 1)
            return null;

         return new Vector2i(chunkX, chunkZ);
    }

    public Vector3f toChunkRelativeCoords(double x, double y, double z) {
        float xOut = (float)(x % 16);
        float zOut = (float)(z % 16);
        if(xOut < 0) xOut += 16;
        if(zOut < 0) zOut += 16;
        return new Vector3f(xOut, (float)y, zOut);
    }
}

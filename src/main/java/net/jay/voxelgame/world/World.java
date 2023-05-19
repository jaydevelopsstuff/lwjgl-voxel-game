package net.jay.voxelgame.world;

import net.jay.voxelgame.render.gl.Mesh;
import net.jay.voxelgame.render.gl.TextureVertex;
import org.joml.Vector2i;

public class World {
    private final Chunk[][] loadedChunks;

    public World() {
        this.loadedChunks = new Chunk[4][4];
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
        Mesh<TextureVertex> mesh = new Mesh<>();

        for(int chunkX = 0; chunkX < loadedChunks.length; chunkX++) {
            for(int chunkZ = 0; chunkZ < loadedChunks[chunkX].length; chunkZ++) {
                Chunk chunk = loadedChunks[chunkX][chunkZ];

                chunk.generateMesh(chunkX * 16, chunkZ * 16, mesh);
            }
        }

        return mesh;
    }

    public Vector2i getChunkCoords(double worldX, double worldZ) {
        int chunkX = (int)(worldX / 16);
        int chunkZ = (int)(worldZ / 16);
        if(chunkX < 0 || chunkX > loadedChunks.length - 1 || chunkZ < 0 || chunkZ > loadedChunks[0].length - 1)
            return null;

         return new Vector2i(chunkX, chunkZ);
    }
}

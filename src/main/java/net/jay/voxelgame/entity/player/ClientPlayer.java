package net.jay.voxelgame.entity.player;

import net.jay.voxelgame.render.gl.Camera;

public class ClientPlayer extends Player {
    private final Camera camera;

    public ClientPlayer() {
        super();
        this.camera = new Camera();
    }

    public Camera camera() {
        return camera;
    }

    @Override
    public void setPos(double x, double y, double z) {
        super.setPos(x, y, z);
        camera.setPos(x, y, z);
    }
}

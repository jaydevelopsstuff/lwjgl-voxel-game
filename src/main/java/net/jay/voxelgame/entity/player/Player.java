package net.jay.voxelgame.entity.player;

import org.joml.Vector3f;

public class Player {
    private final Vector3f pos;

    public Player() {
        this.pos = new Vector3f();
    }

    public Vector3f pos() {
        return pos;
    }

    public void setPos(double x, double y, double z) {
        pos.x = (float)x;
        pos.y = (float)y;
        pos.z = (float)z;
    }
}

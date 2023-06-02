package net.jay.voxelgame.api.entity;

import net.jay.voxelgame.api.entity.Entity;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class SPlayer extends Entity {
    private final DataInputStream in;
    private final DataOutputStream out;
    private final int id;
    private float x, y, z;

    public SPlayer(DataInputStream in, DataOutputStream out, int id) {
        this.in = in;
        this.out = out;
        this.id = id;
    }

    public DataInputStream netIn() {
        return in;
    }

    public DataOutputStream netOut() {
        return out;
    }

    public int id() {
        return id;
    }

    public void setPos(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float x() {
        return x;
    }

    public float y() {
        return y;
    }

    public float z() {
        return z;
    }
}

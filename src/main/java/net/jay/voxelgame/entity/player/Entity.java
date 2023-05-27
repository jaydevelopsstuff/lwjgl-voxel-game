package net.jay.voxelgame.entity.player;

import net.jay.voxelgame.Game;
import net.jay.voxelgame.util.MathUtil;
import net.jay.voxelgame.world.World;
import net.jay.voxelgame.world.block.Block;
import org.joml.Vector3f;

public class Entity {
    private final Vector3f pos;
    private final Vector3f velocity;
    private float yaw;
    private float pitch;
    protected boolean onGround;

    public Entity() {
        this.pos = new Vector3f();
        this.velocity = new Vector3f();
    }

    public void tick() {
        setPos(pos.x + velocity.x, pos.y + velocity.y, pos.z + velocity.z);

        if(MathUtil.round(pos.y - 2) >= World.Height) onGround = false;
        else onGround = Game.world().blockAt(MathUtil.round(pos.x), MathUtil.round(pos.y - 2f), MathUtil.round(pos.z)).type() != Block.Type.Air;
    }

    public Vector3f pos() {
        return pos;
    }

    public void setPos(double x, double y, double z) {
        pos.x = (float)x;
        pos.y = (float)y;
        pos.z = (float)z;
    }

    public Vector3f velocity() {
        return velocity;
    }

    public void setVelocity(double x, double y ,double z) {
        velocity.x = (float)x;
        velocity.y = (float)y;
        velocity.z = (float)z;
    }

    public float yaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float pitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }
}

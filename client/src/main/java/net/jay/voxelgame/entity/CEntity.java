package net.jay.voxelgame.entity;

import net.jay.voxelgame.Game;
import net.jay.voxelgame.api.block.BlockType;
import net.jay.voxelgame.api.entity.Entity;
import net.jay.voxelgame.util.MathUtil;
import net.jay.voxelgame.world.World;
import org.joml.Vector3f;

public class CEntity extends Entity {
    private final Vector3f prevPos = new Vector3f();

    @Override
    public void tick() {
        prevPos.set(pos());
        super.tick();

        if(MathUtil.round(pos().y - 2) >= World.Height) onGround = false;
        else onGround = Game.world().blockAt(MathUtil.round(pos().x), MathUtil.round(pos().y - 2f), MathUtil.round(pos().z)).type() != BlockType.Air;
    }

    public Vector3f prevPos() {
        return prevPos;
    }
}

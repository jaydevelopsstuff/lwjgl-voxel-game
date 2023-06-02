package net.jay.voxelgame.util;

import net.jay.voxelgame.api.block.BlockType;
import net.jay.voxelgame.world.block.Block;
import org.joml.Vector3f;
import org.joml.Vector3i;

// TODO: Raycast through chunks
public class Raycast {
    public static Vector3i traceRay(Block[][][] blocks, Vector3f blocksRelativeStart, boolean relativeResult, Vector3f origin, Vector3f direction, Vector3i beforeIntercept, float maxDistance) {
        int relativeStartX = MathUtil.round(blocksRelativeStart.x);
        int relativeStartY = MathUtil.round(blocksRelativeStart.y);
        int relativeStartZ = MathUtil.round(blocksRelativeStart.z);

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

            if(rayX < 0 || rayY < 0 || rayZ < 0)
                return null;

            int roundedX = MathUtil.round(rayX);
            int roundedY = MathUtil.round(rayY);
            int roundedZ = MathUtil.round(rayZ);

            if(roundedX - relativeStartX >= 16 || roundedY - relativeStartY >= 64 || roundedZ - relativeStartZ >= 16)
                return null;

            Block block = blocks[roundedX - relativeStartX][roundedY - relativeStartY][roundedZ - relativeStartZ];
            if(block.type() != BlockType.Air) {
                if(relativeResult) {
                    beforeIntercept.x = MathUtil.round(prevRayX) - relativeStartX;
                    beforeIntercept.y = MathUtil.round(prevRayY) - relativeStartY;
                    beforeIntercept.z = MathUtil.round(prevRayZ) - relativeStartZ;
                    return new Vector3i(roundedX - relativeStartX, roundedY - relativeStartY, roundedZ - relativeStartZ);
                } else {
                    beforeIntercept.x = MathUtil.round(prevRayX);
                    beforeIntercept.y = MathUtil.round(prevRayY);
                    beforeIntercept.z = MathUtil.round(prevRayZ);
                    return new Vector3i(roundedX, roundedY, roundedZ);
                }
            }

            distanceTraveled += Math.sqrt(moveX*moveX + moveY*moveY + moveZ*moveZ);
        }

        return null;
    }
}

package net.jay.voxelgame.util;

import net.jay.voxelgame.world.block.Block;
import org.joml.Vector3f;
import org.joml.Vector3i;

// TODO: Raycast through chunks
public class Raycast {
    public static Vector3i traceRay(Block[][][] blocks, Vector3f blocksRelativeStart, boolean relativeResult, Vector3f origin, Vector3f direction, Vector3i beforeIntercept, float maxDistance) {
        int relativeStartX = round(blocksRelativeStart.x);
        int relativeStartY = round(blocksRelativeStart.y);
        int relativeStartZ = round(blocksRelativeStart.z);

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

            int roundedX = round(rayX);
            int roundedY = round(rayY);
            int roundedZ = round(rayZ);

            if(roundedX - relativeStartX >= 16 || roundedY - relativeStartY >= 64 || roundedZ - relativeStartZ >= 16)
                return null;

            Block block = blocks[roundedX - relativeStartX][roundedY - relativeStartY][roundedZ - relativeStartZ];
            if(block.type() != Block.Type.Air) {
                if(relativeResult) {
                    beforeIntercept.x = round(prevRayX) - relativeStartX;
                    beforeIntercept.y = round(prevRayY) - relativeStartY;
                    beforeIntercept.z = round(prevRayZ) - relativeStartZ;
                    return new Vector3i(roundedX - relativeStartX, roundedY - relativeStartY, roundedZ - relativeStartZ);
                } else {
                    beforeIntercept.x = round(prevRayX);
                    beforeIntercept.y = round(prevRayY);
                    beforeIntercept.z = round(prevRayZ);
                    return new Vector3i(roundedX, roundedY, roundedZ);
                }
            }

            distanceTraveled += Math.sqrt(moveX*moveX + moveY*moveY + moveZ*moveZ);
        }

        return null;
    }

    private static int round(double d){
        double dAbs = Math.abs(d);
        int i = (int) dAbs;
        double result = dAbs - (double) i;
        if(result<0.5){
            return d<0 ? -i : i;
        }else{
            return d<0 ? -(i+1) : i+1;
        }
    }
}

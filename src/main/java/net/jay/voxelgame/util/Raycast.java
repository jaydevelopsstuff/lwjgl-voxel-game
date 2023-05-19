package net.jay.voxelgame.util;

import net.jay.voxelgame.world.block.Block;
import org.joml.Vector3f;
import org.joml.Vector3i;

public class Raycast {
    public static Vector3i traceRay(Block[][][] blocks, Vector3f origin, Vector3f direction, Vector3i beforeIntercept, float maxDistance) {
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

            if(rayX < 0 || rayY < 0 || rayZ < 0) return null;

            int roundedX = round(rayX);
            int roundedY = round(rayY);
            int roundedZ = round(rayZ);
            Block block = blocks[roundedX][roundedY][roundedZ];
            if(block.type() != Block.Type.Air) {
                beforeIntercept.x = round(prevRayX);
                beforeIntercept.y = round(prevRayY);
                beforeIntercept.z = round(prevRayZ);
                return new Vector3i(roundedX, roundedY, roundedZ);
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

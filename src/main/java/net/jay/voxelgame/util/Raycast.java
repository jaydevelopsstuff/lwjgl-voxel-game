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

    /*public static Block traceRay_impl(Block[][][] blocks, double px, double py, double pz, double dx, double dy, double dz, float maxDistance, Vector3i hitPos) {

        // consider raycast vector to be parametrized by t
        //   vec = [px,py,pz] + t * [dx,dy,dz]

        // algo below is as described by this paper:
        // http://www.cse.chalmers.se/edu/year/2010/course/TDA361/grid.pdf

        double t = 0.0
                , ix = Math.floor(px)
                , iy = Math.floor(py)
                , iz = Math.floor(pz)

                , stepx = (dx > 0) ? 1 : -1
                , stepy = (dy > 0) ? 1 : -1
                , stepz = (dz > 0) ? 1 : -1

                // dx,dy,dz are already normalized
                , txDelta = Math.abs(1 / dx)
                , tyDelta = Math.abs(1 / dy)
                , tzDelta = Math.abs(1 / dz)

                , xdist = (stepx > 0) ? (ix + 1 - px) : (px - ix)
                , ydist = (stepy > 0) ? (iy + 1 - py) : (py - iy)
                , zdist = (stepz > 0) ? (iz + 1 - pz) : (pz - iz)

                // location of nearest voxel boundary, in units of t
                , txMax = (txDelta < Double.MAX_VALUE) ? txDelta * xdist : Double.MAX_VALUE
                , tyMax = (tyDelta < Double.MAX_VALUE) ? tyDelta * ydist : Double.MAX_VALUE
                , tzMax = (tzDelta < Double.MAX_VALUE) ? tzDelta * zdist : Double.MAX_VALUE

                , steppedIndex = -1;

        // main loop along raycast vector
        while (t <= maxDistance) {

            // exit check
            // TODO: BETTER SOLUTION
            if(ix < 0 || iy < 0 || iz < 0) {
                return null;
            }
            Block b = blocks[(int)Math.ceil(ix)][(int)Math.ceil(iy)][(int)Math.ceil(iz)];
            if(b.block() != BlockType.Air) {
                if(hitPos != null) {
                    hitPos.x = (int)Math.ceil(ix);
                    hitPos.y = (int)Math.ceil(iy);
                    hitPos.z = (int)Math.ceil(iz);
                }
                if (hit_norm) {
                    hit_norm[0] = hit_norm[1] = hit_norm[2] = 0
                    if (steppedIndex === 0) hit_norm[0] = -stepx
                    if (steppedIndex === 1) hit_norm[1] = -stepy
                    if (steppedIndex === 2) hit_norm[2] = -stepz
                }
                return b;
            }

            // advance t to next nearest voxel boundary
            if (txMax < tyMax) {
                if (txMax < tzMax) {
                    ix += stepx;
                    t = txMax;
                    txMax += txDelta;
                    steppedIndex = 0;
                } else {
                    iz += stepz;
                    t = tzMax;
                    tzMax += tzDelta;
                    steppedIndex = 2;
                }
            } else {
                if (tyMax < tzMax) {
                    iy += stepy;
                    t = tyMax;
                    tyMax += tyDelta;
                    steppedIndex = 1;
                } else {
                    iz += stepz;
                    t = tzMax;
                    tzMax += tzDelta;
                    steppedIndex = 2;
                }
            }

        }

        // no voxel hit found
        if(hitPos != null) {
            hitPos.x = (int) Math.ceil(ix);
            hitPos.y = (int) Math.ceil(iy);
            hitPos.z = (int) Math.ceil(iz);
        }
        if (hit_norm) {
            hit_norm[0] = hit_norm[1] = hit_norm[2] = 0
        }

        return null;

    }


// conform inputs

    public static Block traceRay(Block[][][] blocks, Vector3f origin, Vector3f direction, float maxDistance, Vector3i hitPos) {
        double px = origin.x
                , py = origin.y
                , pz = origin.z
                , dx = direction.x
                , dy = direction.y
                , dz = direction.z
                , ds = Math.sqrt(dx * dx + dy * dy + dz * dz);

        if(ds == 0) {
            throw new RuntimeException("Can't raycast along a zero vector");
        }

        dx /= ds;
        dy /= ds;
        dz /= ds;

        return traceRay_impl(blocks, px, py, pz, dx, dy, dz, maxDistance, hitPos);
    }*/
}

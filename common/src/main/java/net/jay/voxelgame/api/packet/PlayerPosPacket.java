package net.jay.voxelgame.api.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PlayerPosPacket implements Packet {
    public byte player;
    public float x, y, z;

    @Override
    public void read(DataInputStream in) throws IOException {
        player = in.readByte();
        x = in.readFloat();
        y = in.readFloat();
        z = in.readFloat();
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeByte(player);
        out.writeFloat(x);
        out.writeFloat(y);
        out.writeFloat(z);
    }

    @Override
    public int id() {
        return 2;
    }
}

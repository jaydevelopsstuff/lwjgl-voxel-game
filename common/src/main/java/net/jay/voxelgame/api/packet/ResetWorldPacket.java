package net.jay.voxelgame.api.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ResetWorldPacket implements Packet {
    @Override
    public void read(DataInputStream in) throws IOException {}

    @Override
    public void write(DataOutputStream out) throws IOException {}

    @Override
    public int id() {
        return 4;
    }
}

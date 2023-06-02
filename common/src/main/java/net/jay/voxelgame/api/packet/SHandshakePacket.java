package net.jay.voxelgame.api.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class SHandshakePacket implements Packet {
    public int id;

    @Override
    public void read(DataInputStream in) throws IOException {
        id = in.readInt();
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeInt(id);
    }

    @Override
    public int id() {
        return 0;
    }
}

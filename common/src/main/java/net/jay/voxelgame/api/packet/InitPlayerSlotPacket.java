package net.jay.voxelgame.api.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class InitPlayerSlotPacket implements Packet {
    public byte playerId;

    @Override
    public void read(DataInputStream in) throws IOException {
        playerId = in.readByte();
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeByte(playerId);
    }

    @Override
    public int id() {
        return 1;
    }
}

package net.jay.voxelgame.api.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public interface Packet {
    void read(DataInputStream in) throws IOException;
    void write(DataOutputStream out) throws IOException;

    int id();
}

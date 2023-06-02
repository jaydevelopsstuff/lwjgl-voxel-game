package net.jay.voxelgame.api.packet;

import net.jay.voxelgame.api.block.BlockType;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ModifyBlockPacket implements Packet {
    public BlockType blockType;
    public int x, y, z;

    @Override
    public void read(DataInputStream in) throws IOException {
        blockType = BlockType.fromId(in.readInt());
        x = in.readInt();
        y = in.readInt();
        z = in.readInt();
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeInt(blockType.id);
        out.writeInt(x);
        out.writeInt(y);
        out.writeInt(z);
    }

    @Override
    public int id() {
        return 3;
    }
}

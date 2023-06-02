package net.jay.voxelgame.network;

import net.jay.voxelgame.Game;
import net.jay.voxelgame.api.block.BlockType;
import net.jay.voxelgame.api.entity.SPlayer;
import net.jay.voxelgame.api.packet.*;
import net.jay.voxelgame.world.block.Blocks;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class NetworkManager {
    private SPlayer[] players;
    private boolean connected = true;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private int ourPlayerId;

    public void connectToServer(String host) {
        players = new SPlayer[256];

        try {
            socket = new Socket(host, 1024);
        } catch(IOException e) {
            // e.printStackTrace();
            System.out.println("Failed to connect to server");
            connected = false;
            return;
        }

        new Thread(() -> {
            try {
                clientNetLoop(socket);
            } catch(IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    public void sendPacket(Packet packet) throws IOException {
        if(!connected)
            return;
        out.writeByte((byte)packet.id());
        packet.write(out);
        out.flush();
    }

    public void sendPosition(float x, float y, float z) {
        PlayerPosPacket packet = new PlayerPosPacket();
        packet.player = (byte)ourPlayerId;
        packet.x = x;
        packet.y = y;
        packet.z = z;
        try {
            sendPacket(packet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void clientNetLoop(Socket socket) throws IOException {
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());

        boolean connected = true;
        while(connected) {
            int packetId = in.readByte() & 0xFF;
            handlePacket(packetId);
        }
        System.out.println("Disconnected from server");
    }

    private void handlePacket(int id) throws IOException {
        switch(id) {
            case 0: {
                SHandshakePacket packet = new SHandshakePacket();
                packet.read(in);
                ourPlayerId = packet.id;
                break;
            }
            case 1: {
                InitPlayerSlotPacket packet = new InitPlayerSlotPacket();
                packet.read(in);
                players[packet.playerId] = new SPlayer(null, null, packet.playerId);
                break;
            }
            case 2: {
                PlayerPosPacket packet = new PlayerPosPacket();
                packet.read(in);
                if(players[packet.player] == null) {
                    System.out.println("Received position packet for unknown player with id of " + packet.player);
                    break;
                }
                players[packet.player].setPos(packet.x, packet.y, packet.z);
                break;
            }
            case 3: {
                ModifyBlockPacket packet = new ModifyBlockPacket();
                packet.read(in);
                Game.world().setBlock(Blocks.getBlock(packet.blockType), packet.x, packet.y, packet.z);
                Game.renderer().queueMeshUpdate();
                break;
            }
            case 4: {
                ResetWorldPacket packet = new ResetWorldPacket();
                packet.read(in);
                Game.world().resetChunks();
                Game.renderer().queueMeshUpdate();
            }
            default: {
                System.out.println("Received unknown packet with id of " + id);
                break;
            }
        }
    }

    public SPlayer[] players() {
        return players;
    }

    public boolean isConnected() {
        return connected;
    }
}

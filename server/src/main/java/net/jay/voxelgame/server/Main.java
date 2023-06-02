package net.jay.voxelgame.server;

import net.jay.voxelgame.api.entity.SPlayer;
import net.jay.voxelgame.api.packet.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class Main {
    public static SPlayer[] players;
    public static int nextPlayerId = 0;

    public static void main(String[] args) throws IOException {
        players = new SPlayer[256];

        System.out.println("Starting server...");
        ServerSocket server = new ServerSocket(1024);

        boolean running = true;
        while(running) {
            Socket socket = server.accept();
            new Thread(() -> {
                try {
                    handleClient(socket);
                } catch(IOException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }
    }

    public static void handleClient(Socket client) throws IOException {
        System.out.println("Client Connected");

        DataInputStream in = new DataInputStream(client.getInputStream());
        DataOutputStream out = new DataOutputStream(client.getOutputStream());

        SPlayer player = new SPlayer(in, out, nextPlayerId);
        players[nextPlayerId] = player;

        {
            SHandshakePacket packet = new SHandshakePacket();
            packet.id = nextPlayerId;
            sendPacket(packet, player);
            nextPlayerId++;

            InitPlayerSlotPacket joinPacket = new InitPlayerSlotPacket();
            joinPacket.playerId = (byte)player.id();
            broadcastPacket(joinPacket, player);
        }

        // Sync already joined players
        for(SPlayer otherPlayer : players) {
            if(otherPlayer == player || otherPlayer == null)
                continue;
            InitPlayerSlotPacket slotPacket = new InitPlayerSlotPacket();
            slotPacket.playerId = (byte)otherPlayer.id();
            sendPacket(slotPacket, player);
        }

        boolean running = true;
        while(running) {
            int packetId = in.readByte() & 0xFF;
            handlePacket(packetId, player);

            try {
                Thread.sleep(10);
            } catch(InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void sendPacket(Packet packet, SPlayer player) throws IOException {
        player.netOut().writeByte((byte)packet.id());
        packet.write(player.netOut());
        player.netOut().flush();
    }

    public static void broadcastPacket(Packet packet, SPlayer... exclusions) throws IOException {
        for(SPlayer player : players) {
            if(player == null || Arrays.stream(exclusions).anyMatch(player1 -> player1 == player))
                continue;
            sendPacket(packet, player);
        }
    }

    private static void handlePacket(int id, SPlayer player) throws IOException {
        switch(id) {
            case 2: {
                Packet packet = new PlayerPosPacket();
                packet.read(player.netIn());
                broadcastPacket(packet, player);
                break;
            }
            case 3: {
                Packet packet = new ModifyBlockPacket();
                packet.read(player.netIn());
                broadcastPacket(packet, player);
                break;
            }
            case 4: {
                Packet packet = new ResetWorldPacket();
                packet.read(player.netIn());
                broadcastPacket(packet, player);
            }
            default: {
                System.out.println("Received unknown packet with id of " + id);
                break;
            }
        }
    }
}
package net.jay.voxelgame;

public class Main {
    public static void main(String[] args) {
        if(args.length == 0)
            Game.host = "localhost";
        else
            Game.host = args[0];
        Game.start();
    }
}
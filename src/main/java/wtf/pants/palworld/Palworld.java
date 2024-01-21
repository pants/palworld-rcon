package wtf.pants.palworld;
import wtf.pants.palworld.rkon.Rcon;
import wtf.pants.palworld.rkon.ex.AuthenticationException;
import wtf.pants.palworld.struct.Player;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class Palworld {
    private static List<String> whitelist = new ArrayList<>();
    private final static List<Player> userStatus = new ArrayList<>();

    public static void populateWhitelist() throws IOException {
        final var lines = Files.readAllLines(new File("whitelist.txt").toPath());
        for (String line : lines) {
            if(line.startsWith("#")) continue;
            whitelist.add(line);
        }

        System.out.println("Whitelist loaded with " + whitelist.size() + " users.");
    }

    public static void main(String[] args) throws IOException, AuthenticationException, InterruptedException {
        populateWhitelist();

        final Rcon rcon = new Rcon(args[0], Integer.parseInt(args[1]), args[2].getBytes());
        System.out.println("Connected via rcon.");

        final AdminCommands admin = new AdminCommands(rcon);
        System.out.println(admin.getInfo());

        System.out.println("Online Players:");
        admin.getPlayers().forEach(player -> System.out.println("- " + player.getPlayerName() + " [" + player.getSteamId() + "]"));

        final var initialPlayers = admin.getPlayers();
        userStatus.addAll(initialPlayers);

        while(true) {
            final var players = admin.getPlayers();

            for (Player player : players) {
                if(!userStatus.contains(player)) {
                    userStatus.add(player);
                    admin.broadcast(player.getPlayerName() + " has joined the server.");
                    System.out.println("Player joined '" + player.getPlayerName() + "' [" + player.getSteamId() + "]");
                }

                if (!whitelist.contains(player.getSteamId())) {
                    System.out.println("Player not whitelisted..");
                    admin.kickPlayer(player);
                    admin.broadcast("Kicked non-whitelisted player: " + player.getPlayerName() + " [" + player.getSteamId() + "]");
                }
            }

            for (Player historicPlayer : new ArrayList<>(userStatus)) {
                if(!players.contains(historicPlayer)) {
                    System.out.println("Player disconnected '" + historicPlayer.getPlayerName() + "' [" + historicPlayer.getSteamId() + "]");
                    admin.broadcast(historicPlayer.getPlayerName() + " has disconnected.");
                    userStatus.remove(historicPlayer);
                }
            }

            Thread.sleep(3000);
        }
    }


}

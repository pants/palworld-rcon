package wtf.pants.palworld;

import wtf.pants.palworld.rkon.Rcon;
import wtf.pants.palworld.struct.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdminCommands {
    private final Rcon rcon;

    public AdminCommands(Rcon rcon) {
        this.rcon = rcon;
    }

    public void kickPlayer(Player player) throws IOException {
        System.out.println(rcon.command("KickPlayer " + player.getPlayerId()));
    }

    public void broadcast(String message) throws IOException {
        System.out.println(rcon.command("Broadcast " + message.replace(" ", "\t")));
    }

    public String getInfo() throws IOException {
        return rcon.command("Info");
    }

    public List<Player> getPlayers() throws IOException {
        final String players = rcon.command("ShowPlayers");
        final String[] lines = players.split("\n");

        final List<Player> playerList = new ArrayList<>();
        for (int i = 1; i < lines.length; i++) {
            final var line = lines[i];

            if(line.trim().isEmpty()) {
                continue;
            }

            playerList.add(Player.parse(line));
        }

        return playerList;
    }
}

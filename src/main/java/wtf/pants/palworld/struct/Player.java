package wtf.pants.palworld.struct;

import lombok.Data;

import java.util.List;
import java.util.Objects;

@Data
public class Player {
    private final String playerName;
    private final String playerId;
    private final String steamId;

    public static Player parse(String line) {
        final String[] split = line.split(",");
        return new Player(split[0], split[1], split[2]);
    }

    @Override
    public String toString() {
        return "Player{" +
                "playerName='" + playerName + '\'' +
                ", playerId='" + playerId + '\'' +
                ", steamId='" + steamId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return this.getSteamId().equals(player.getSteamId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerName, playerId, steamId);
    }
}

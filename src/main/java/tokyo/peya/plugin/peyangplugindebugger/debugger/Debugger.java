package tokyo.peya.plugin.peyangplugindebugger.debugger;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class Debugger
{
    private final Plugin plugin;
    private final Map<Player, DebuggerOption> players;

    public Debugger(Plugin plugin)
    {
        this.plugin = plugin;
        this.players = new HashMap<>();
    }

    public void addPlayer(Player player)
    {
        if (this.players.containsKey(player))
            return;

        this.players.put(player, new DebuggerOption());
    }

    public void removePlayer(Player player)
    {
        if (!this.players.containsKey(player))
            return;

        this.players.remove(player);
    }
}

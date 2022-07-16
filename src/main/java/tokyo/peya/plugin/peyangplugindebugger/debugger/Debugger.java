package tokyo.peya.plugin.peyangplugindebugger.debugger;

import jdk.nashorn.internal.ir.Terminal;
import net.kunmc.lab.peyangpaperutils.lib.terminal.Terminals;
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

        Terminals.ofConsole().info("Player " + player.getName() + " is registered as a debug player.");
        this.players.put(player, new DebuggerOption());
    }

    public void removePlayer(Player player)
    {
        if (!this.players.containsKey(player))
            return;

        this.players.remove(player);
    }
}

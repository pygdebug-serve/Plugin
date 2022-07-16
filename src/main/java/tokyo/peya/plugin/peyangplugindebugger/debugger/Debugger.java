package tokyo.peya.plugin.peyangplugindebugger.debugger;

import net.kunmc.lab.peyangpaperutils.lib.terminal.Terminals;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import tokyo.peya.lib.pygdebug.common.debugger.DebuggerOption;
import tokyo.peya.lib.pygdebug.common.packets.debug.PacketServerConsoleOutput;
import tokyo.peya.plugin.peyangplugindebugger.PeyangPluginDebugger;
import tokyo.peya.plugin.peyangplugindebugger.networking.NetworkRouter;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class Debugger
{
    private final PeyangPluginDebugger plugin;
    private final Map<Player, DebuggerOption> players;

    private final NetworkRouter router;

    public Debugger(PeyangPluginDebugger plugin)
    {
        this.plugin = plugin;
        this.router = plugin.getRouter();

        this.players = new HashMap<>();

        Bukkit.getServer().getLogger().addHandler(new Handler()
        {
            @Override
            public void publish(LogRecord record)
            {
                Debugger.this.onConsoleLog(record);
            }

            @Override public void flush() {}
            @Override public void close() throws SecurityException {}
        });
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

    public boolean isDebugPlayer(Player player)
    {
        return this.players.containsKey(player);
    }

    private void onConsoleLog(LogRecord context)
    {
        this.players.forEach((player, option) -> {
            if (option.isPipeServerConsole())
                this.router.sendPluginMessage("debug", player, new PacketServerConsoleOutput(context));
        });
    }

    public void setOption(Player player, DebuggerOption option)
    {
        if (!this.players.containsKey(player))
            return;

        this.players.put(player, option);
    }

    public DebuggerOption getOption(Player player)
    {
        if (!this.players.containsKey(player))
            return null;

        return this.players.get(player);
    }
}

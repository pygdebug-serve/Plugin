package tokyo.peya.plugin.peyangplugindebugger.events;

import net.kunmc.lab.peyangpaperutils.lib.terminal.Terminal;
import net.kunmc.lab.peyangpaperutils.lib.terminal.Terminals;
import net.kunmc.lab.peyangpaperutils.lib.utils.Runner;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import tokyo.peya.lib.pygdebug.common.packets.main.PacketPygDebugAvailable;
import tokyo.peya.plugin.peyangplugindebugger.PeyangPluginDebugger;

public class GeneralEventListener implements Listener
{
    private final PeyangPluginDebugger plugin;

    public GeneralEventListener(PeyangPluginDebugger plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event)
    {
        Runner.runLater(() -> {
            Terminals.ofConsole().info("Checking if the player is a debug player...");
            this.plugin.getRouter().sendPluginMessage("main", event.getPlayer(), new PacketPygDebugAvailable());
        }, 20L);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event)
    {
        this.plugin.getDebugger().removePlayer(event.getPlayer());
    }
}

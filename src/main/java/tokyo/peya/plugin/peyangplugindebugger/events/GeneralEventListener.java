package tokyo.peya.plugin.peyangplugindebugger.events;

import net.kunmc.lab.peyangpaperutils.lib.utils.Runner;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import tokyo.peya.lib.pygdebug.common.packets.main.PacketPygDebugAvailable;
import tokyo.peya.plugin.peyangplugindebugger.PeyangPluginDebugger;

public class GeneralEventListener implements Listener
{
    private final PeyangPluginDebugger plugin;

    public GeneralEventListener(PeyangPluginDebugger plugin)
    {
        this.plugin = plugin;
    }

    public void onJoin(PlayerJoinEvent event)
    {
        Runner.runLater(() -> {
            this.plugin.getRouter().sendPluginMessage("main", event.getPlayer(), new PacketPygDebugAvailable());
        }, 20L);
    }
}

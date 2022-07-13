package tokyo.peya.plugin.peyangplugindebugger.events;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

public class GeneralEventListener implements Listener
{
    private final Plugin plugin;

    public GeneralEventListener(Plugin plugin)
    {
        this.plugin = plugin;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void onJoin(PlayerJoinEvent event)
    {

    }
}

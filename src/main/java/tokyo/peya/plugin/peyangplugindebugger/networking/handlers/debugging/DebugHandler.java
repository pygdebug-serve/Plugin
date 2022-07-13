package tokyo.peya.plugin.peyangplugindebugger.networking.handlers.debugging;

import org.bukkit.entity.Player;
import tokyo.peya.plugin.peyangplugindebugger.networking.NetworkHandler;
import tokyo.peya.plugin.peyangplugindebugger.networking.NetworkRouter;

public class DebugHandler implements NetworkHandler
{
    @Override
    public String getName()
    {
        return "debug";
    }

    @Override
    public void onReceived(NetworkRouter router, Player sender, byte[] data)
    {

    }
}

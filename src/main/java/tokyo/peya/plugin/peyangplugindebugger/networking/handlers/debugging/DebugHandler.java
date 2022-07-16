package tokyo.peya.plugin.peyangplugindebugger.networking.handlers.debugging;

import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import tokyo.peya.lib.pygdebug.common.packets.debug.PacketDebugOption;
import tokyo.peya.lib.pygdebug.common.packets.debug.PacketDebugOptionRequest;
import tokyo.peya.lib.pygdebug.common.packets.debug.PacketSendConsoleCommand;
import tokyo.peya.plugin.peyangplugindebugger.PeyangPluginDebugger;
import tokyo.peya.plugin.peyangplugindebugger.Utils;
import tokyo.peya.plugin.peyangplugindebugger.debugger.Debugger;
import tokyo.peya.plugin.peyangplugindebugger.networking.NetworkHandler;
import tokyo.peya.plugin.peyangplugindebugger.networking.NetworkRouter;
import tokyo.peya.plugin.peyangplugindebugger.utils.PacketEncodeUtils;

public class DebugHandler implements NetworkHandler
{
    private final PeyangPluginDebugger plugin;
    private final Debugger debugger;

    public DebugHandler(PeyangPluginDebugger plugin)
    {
        this.plugin = plugin;

        this.debugger = plugin.getDebugger();
    }

    @Override
    public String getName()
    {
        return "debug";
    }

    @Override
    public void onReceived(NetworkRouter router, Player sender, byte[] data)
    {
        if (!this.debugger.isDebugPlayer(sender))
            return;

        byte index = data[0];

        if (index == Utils.getId(PacketDebugOptionRequest.class))
            router.sendPluginMessage(this.getName(), sender, new PacketDebugOption(this.debugger.getOption(sender)));
        else if (index == Utils.getId(PacketDebugOption.class))
        {
            PacketDebugOption decodedOption = PacketEncodeUtils.decodeMessage(data, PacketDebugOption.class);

            if (decodedOption == null)
                return;

            this.debugger.setOption(sender, decodedOption.getDebuggerOption());
        }
        else if (index == Utils.getId(PacketSendConsoleCommand.class))
        {
            PacketSendConsoleCommand decodedCommand = PacketEncodeUtils.decodeMessage(data, PacketSendConsoleCommand.class);

            if (decodedCommand == null)
                return;

            this.plugin.getServer().dispatchCommand(this.plugin.getServer().getConsoleSender(), decodedCommand.getCommand());
        }
    }
}

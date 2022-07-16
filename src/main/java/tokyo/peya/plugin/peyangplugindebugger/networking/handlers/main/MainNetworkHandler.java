package tokyo.peya.plugin.peyangplugindebugger.networking.handlers.main;

import lombok.Value;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import tokyo.peya.lib.pygdebug.common.packets.main.PacketInformationRequest;
import tokyo.peya.lib.pygdebug.common.packets.main.PacketPygDebugAvailable;
import tokyo.peya.plugin.peyangplugindebugger.PeyangPluginDebugger;
import tokyo.peya.plugin.peyangplugindebugger.Utils;
import tokyo.peya.plugin.peyangplugindebugger.networking.NetworkHandler;
import tokyo.peya.plugin.peyangplugindebugger.networking.NetworkRouter;
import tokyo.peya.plugin.peyangplugindebugger.utils.PacketEncodeUtils;

public class MainNetworkHandler implements NetworkHandler
{
    private final PeyangPluginDebugger plugin;

    private final InformationSender informationSender;

    public MainNetworkHandler(PeyangPluginDebugger plugin)
    {
        this.plugin = plugin;
        this.informationSender = new InformationSender(this);
    }

    @Override
    public String getName()
    {
        return "main";
    }

    @Override
    public void onReceived(NetworkRouter router, Player sender, byte[] message)
    {
        if (message[0] == Utils.getId(PacketPygDebugAvailable.class))
        {
            this.plugin.getDebugger().addPlayer(sender);
            return;
        }

        PacketInformationRequest decodedRequest = PacketEncodeUtils.decodeMessage(message, PacketInformationRequest.class);

        if (decodedRequest == null)
            return;

        switch (decodedRequest.getAction())
        {
            case PLATFORM:
            case SERVER_STATUS:
                this.informationSender.sendInfo(decodedRequest, router, sender);
                break;
        }
    }
}

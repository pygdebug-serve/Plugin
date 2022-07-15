package tokyo.peya.plugin.peyangplugindebugger.networking.handlers.main;

import lombok.Value;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import tokyo.peya.lib.pygdebug.common.packets.main.PacketInformationRequest;
import tokyo.peya.plugin.peyangplugindebugger.networking.NetworkHandler;
import tokyo.peya.plugin.peyangplugindebugger.networking.NetworkRouter;

public class MainNetworkHandler implements NetworkHandler
{
    private final Plugin plugin;

    private final InformationSender informationSender;

    public MainNetworkHandler(Plugin plugin)
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
        PacketInformationRequest decodedRequest = this.decodeMessage(message, PacketInformationRequest.class);

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

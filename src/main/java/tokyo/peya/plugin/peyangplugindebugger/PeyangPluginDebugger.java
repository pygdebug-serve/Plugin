package tokyo.peya.plugin.peyangplugindebugger;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.common.returnsreceiver.qual.This;
import org.slf4j.Logger;
import tokyo.peya.plugin.peyangplugindebugger.debugger.Debugger;
import tokyo.peya.plugin.peyangplugindebugger.events.GeneralEventListener;
import tokyo.peya.plugin.peyangplugindebugger.networking.NetworkRouter;
import tokyo.peya.plugin.peyangplugindebugger.networking.handlers.main.MainNetworkHandler;

public final class PeyangPluginDebugger extends JavaPlugin
{
    public static Logger LOGGER;

    @Getter
    private final NetworkRouter router;
    @Getter
    private final Debugger debugger;


    public PeyangPluginDebugger()
    {
        LOGGER = getSLF4JLogger();

        this.router = new NetworkRouter(this);
        this.debugger = new Debugger(this);

        this.initNetworkingHandlers();
    }

    private void initNetworkingHandlers()
    {
        this.router.registerHandler(new MainNetworkHandler(this));
    }

    @Override
    public void onEnable()
    {
        LOGGER.info("Initializing router...");
        this.router.init();

        Bukkit.getPluginManager().registerEvents(new GeneralEventListener(this), this);

        LOGGER.info("PeyangPluginDebugger has been enabled.");
    }

    @Override
    public void onDisable()
    {
        LOGGER.info("Disposing router...");
        this.router.dispose();
        // Plugin shutdown logic
    }
}

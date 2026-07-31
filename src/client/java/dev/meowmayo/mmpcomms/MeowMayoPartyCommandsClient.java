package dev.meowmayo.mmpcomms;

import dev.meowmayo.mmpcomms.commands.PartyBLWLCommands;
import dev.meowmayo.mmcore.config.ConfigSettings;
import dev.meowmayo.mmcore.config.MeowModule;
import dev.meowmayo.mmcore.config.ModConfig;
import dev.meowmayo.mmcore.config.settings.*;
import dev.meowmayo.mmpcomms.features.PartyCommands;
import dev.meowmayo.mmpcomms.utils.PartyCommandListUtils;
import net.fabricmc.api.ClientModInitializer;

public class MeowMayoPartyCommandsClient implements ClientModInitializer {
    public static MeowModule mainModule = new MeowModule("Party Commands");
    public static ConfigSettings config;
	@Override
	public void onInitializeClient() {
        mainModule.getConfig().register(new ToggleSetting("Party Commands", "Enables Party Commands", "", false));
        mainModule.getConfig().register(new TextSetting("Party Commands Prefix", "Prefix used for party commands | Highly recommended to not keep blank", "", "!"));
        mainModule.getConfig().register(new ToggleSetting("Party Help Command", "Enables Help Party Command", "", false));

        mainModule.getConfig().register(new ToggleSetting("All Invite Command", "Enables All Invite Party Command", "General", false));
        mainModule.getConfig().register(new ToggleSetting("Warp Command", "Enables Warp Party Command", "General", false));
        mainModule.getConfig().register(new ToggleSetting("Invite Command", "Enables Invite Party Command", "General", false));
        mainModule.getConfig().register(new ToggleSetting("Party Transfer Command", "Enables Party Transfer Party Command", "General", false));
        mainModule.getConfig().register(new ToggleSetting("Party Transfer Whitelist", "Makes Party Transfer Whitelist Only", "General", false));
        mainModule.getConfig().register(new ToggleSetting("Party Transfer Me Command", "Enables Party Transfer Me Party Command", "General", false));
        mainModule.getConfig().register(new ToggleSetting("Party Transfer Me Whitelist", "Makes Party Transfer Me Whitelist Only", "General", false));
        mainModule.getConfig().register(new ToggleSetting("Sellout Command", "Enables Sellout Party Command", "General", false));
        mainModule.getConfig().register(new ToggleSetting("Coordinates Command", "Enables Coordinates Party Command", "General", false));

        mainModule.getConfig().register(new ToggleSetting("Catacombs Entrance Command", "Enables Catacombs Entrance Party Command", "Catacombs", false));
        mainModule.getConfig().register(new ToggleSetting("Master Catacombs Entrance Command", "Enables Master Catacombs Entrance Party Command", "Catacombs", false));

        mainModule.getConfig().register(new ToggleSetting("Kuudra Entrance Command", "Enables Kuudra Entrance Party Command", "Kuudra", false));

        mainModule.getConfig().register(new ToggleSetting("Coinflip Command", "Enables Coinflip Party Command", "Fun", false));
        mainModule.getConfig().register(new ToggleSetting("Dice Command", "Enables Dice Party Command", "Fun", false));

//        if (FabricLoader.getInstance().isModLoaded("meowmayo-dungeons")) {
//            mainModule.getConfig().register(new ToggleSetting("Dungeon Time Stats Command", "Enables Dungeon Time Stats Party Command", "Catacombs", false));
//        }
//
//        if (FabricLoader.getInstance().isModLoaded("meowmayo-kuudra")) {
//            mainModule.getConfig().register(new ToggleSetting("Kuudra Time Stats Command", "Enables Kuudra Time Stats Party Command", "Kuudra", false));
//        }

        ModConfig.register(mainModule);

        mainModule.init();

        config = mainModule.getConfig();

		PartyBLWLCommands.register();
        PartyCommandListUtils.init();
        PartyCommands.init();
	}
}
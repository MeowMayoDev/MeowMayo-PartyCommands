package dev.meowmayo.mmpcomms.commands;

import dev.meowmayo.mmpcomms.utils.PartyCommandListUtils;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.network.chat.Component;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.literal;

public class PartyBLWLCommands {
    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(literal("blacklist")
                    .then(argument("action", StringArgumentType.word())
                            .then(argument("username", StringArgumentType.word())
                                    .executes(context -> {
                                        String action = StringArgumentType.getString(context, "action");
                                        String username = StringArgumentType.getString(context, "username");

                                        switch (action) {
                                            case "add":
                                                PartyCommandListUtils.addToBlacklist(username);
                                                break;
                                            case "remove":
                                                PartyCommandListUtils.removeFromBlacklist(username);
                                                break;
                                            default:
                                                context.getSource().sendError(Component.literal("Usage: /blacklist <add|remove> <ign>"));
                                                break;
                                        }
                                        return 1;
                                    })
                            )
                    )
            );

            dispatcher.register(literal("whitelist")
                    .then(argument("action", StringArgumentType.word())
                            .then(argument("username", StringArgumentType.word())
                                    .executes(context -> {
                                        String action = StringArgumentType.getString(context, "action");
                                        String username = StringArgumentType.getString(context, "username");

                                        switch (action) {
                                            case "add":
                                                PartyCommandListUtils.addToWhitelist(username);
                                                break;
                                            case "remove":
                                                PartyCommandListUtils.removeFromWhitelist(username);
                                                break;
                                            default:
                                                context.getSource().sendError(Component.literal("Usage: /whitelist <add|remove> <ign>"));
                                                break;
                                        }
                                        return 1;
                                    })
                            )
                    )
            );
        });
    }
}


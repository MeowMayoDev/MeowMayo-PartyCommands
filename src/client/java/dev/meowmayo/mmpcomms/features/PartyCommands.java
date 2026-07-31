package dev.meowmayo.mmpcomms.features;

import dev.meowmayo.mmpcomms.MeowMayoPartyCommandsClient;
import dev.meowmayo.mmcore.config.settings.TextSetting;
import dev.meowmayo.mmcore.config.settings.ToggleSetting;
import dev.meowmayo.mmcore.events.MMChatEvent;
import dev.meowmayo.mmcore.utils.ChatUtils;
import dev.meowmayo.mmpcomms.utils.PartyCommandListUtils;
import dev.meowmayo.mmcore.utils.PartyUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

import static dev.meowmayo.mmcore.utils.DelayUtils.scheduleTask;

public class PartyCommands {
    private static ToggleSetting partyCommands = (ToggleSetting) MeowMayoPartyCommandsClient.config.getSetting("Party Commands");
    private static TextSetting partyCommandsPrefix = (TextSetting) MeowMayoPartyCommandsClient.config.getSetting("Party Commands Prefix");
    private static ToggleSetting partyHelpCommand = (ToggleSetting) MeowMayoPartyCommandsClient.config.getSetting("Party Help Command");

    // General Party Commands
    private static ToggleSetting allInviteCommand = (ToggleSetting) MeowMayoPartyCommandsClient.config.getSetting("All Invite Command");
    private static ToggleSetting warpCommand = (ToggleSetting) MeowMayoPartyCommandsClient.config.getSetting("Warp Command");
    private static ToggleSetting inviteCommand = (ToggleSetting) MeowMayoPartyCommandsClient.config.getSetting("Invite Command");
    private static ToggleSetting partyTransferCommand = (ToggleSetting) MeowMayoPartyCommandsClient.config.getSetting("Party Transfer Command");
    private static ToggleSetting partyTransferWhitelist = (ToggleSetting) MeowMayoPartyCommandsClient.config.getSetting("Party Transfer Whitelist");
    private static ToggleSetting partyTransferMeCommand = (ToggleSetting) MeowMayoPartyCommandsClient.config.getSetting("Party Transfer Me Command");
    private static ToggleSetting partyTransferMeWhitelist = (ToggleSetting) MeowMayoPartyCommandsClient.config.getSetting("Party Transfer Me Whitelist");
    private static ToggleSetting selloutCommand = (ToggleSetting) MeowMayoPartyCommandsClient.config.getSetting("Sellout Command");
    private static ToggleSetting coordinatesCommand = (ToggleSetting) MeowMayoPartyCommandsClient.config.getSetting("Coordinates Command");

    // Catacombs Commands
    private static ToggleSetting catacombsEntranceCommand = (ToggleSetting) MeowMayoPartyCommandsClient.config.getSetting("Catacombs Entrance Command");
    private static ToggleSetting masterCatacombsEntranceCommand = (ToggleSetting) MeowMayoPartyCommandsClient.config.getSetting("Master Catacombs Entrance Command");

    // Kuudra Commands
    private static ToggleSetting kuudraEntranceCommand = (ToggleSetting) MeowMayoPartyCommandsClient.config.getSetting("Kuudra Entrance Command");

    // Fun Commands
    private static ToggleSetting coinflipCommand = (ToggleSetting) MeowMayoPartyCommandsClient.config.getSetting("Coinflip Command");
    private static ToggleSetting diceCommand = (ToggleSetting) MeowMayoPartyCommandsClient.config.getSetting("Dice Command");

    private static ToggleSetting dtStats;
    private static ToggleSetting ktStats;

    private static boolean delayed = false;

    private static Random r = new Random();

    public static void init() {
//        if (FabricLoader.getInstance().isModLoaded("meowmayo-dungeons")) {
//            dtStats = (ToggleSetting) MeowMayoPartyCommandsClient.config.getSetting("Dungeon Time Stats Command");
//        }
//
//        if (FabricLoader.getInstance().isModLoaded("meowmayo-kuudra")) {
//            ktStats = (ToggleSetting) MeowMayoPartyCommandsClient.config.getSetting("Kuudra Time Stats Command");
//        }

        MMChatEvent.SYSTEM.register((packet) -> {
            if (!PartyCommands.partyCommands.getValue()) return;

            onChat(ChatFormatting.stripFormatting(packet.content().getString().toLowerCase()));
        });
    }

    public static void onChat(String message) {
        if (delayed) return;
        String prefix = partyCommandsPrefix.getValue().toLowerCase();
        if (!message.startsWith("party >")) {
            return;
        }

        int bracketIndex = message.indexOf(">");
        int colonIndex = message.indexOf(":");
        if (colonIndex == -1) return;
        if (bracketIndex == -1) return;

        String ign = ChatUtils.stripRank(message.substring(bracketIndex + 1, colonIndex).trim());
        String partyMessage = message.substring(colonIndex + 1).trim();

        if (PartyCommandListUtils.blacklist.contains(ign)) return;

        if (!partyMessage.startsWith(prefix)) return;
        partyMessage = partyMessage.substring(prefix.length());

        String[] args = partyMessage.split(" ");

        switch (args[0].toLowerCase()) {
            case "help":
                if (partyHelpCommand.getValue()) {
                    delayed = true;
                    if (args.length == 1) {
                        ChatUtils.partyChat("MeowMayo Party Commands Help Menu - Current Prefix: " + prefix);
                        scheduleTask(() -> ChatUtils.partyChat("Use " + prefix + "help <general|catacombs|kuudra|fun>"), 1000);
                        scheduleReset(2000);
                    } else {
                        switch (args[1]) {
                            case "general":
                                if (args.length == 2) {
                                    ChatUtils.partyChat("MeowMayo Party Commands | General:");
                                    scheduleTask(() -> ChatUtils.partyChat("Use " + prefix + "help general (Command) for more info"), 1000);
                                    scheduleTask(() -> ChatUtils.partyChat("Available Commands: AllInvite | Warp | Invite | PT | PTME | Coordinates"), 2000);
                                    scheduleReset(3000);
                                } else {
                                    switch (args[2]) {
                                        case "allinv":
                                        case "allinvite":
                                            ChatUtils.partyChat("AllInvite: Toggles All Invite");
                                            scheduleTask(() -> ChatUtils.partyChat("Usage: " + prefix + "AllInvite"), 1000);
                                            scheduleTask(() -> ChatUtils.partyChat("Aliases: AllInvite | AllInv"), 2000);
                                            scheduleReset(3000);
                                            break;
                                        case "warp":
                                        case "pwarp":
                                        case "partywarp":
                                            ChatUtils.partyChat("Warp: Warps the party");
                                            scheduleTask(() -> ChatUtils.partyChat("Usage: " + prefix + "Warp"), 1000);
                                            scheduleTask(() -> ChatUtils.partyChat("Aliases: Warp | W | PWarp | PartyWarp"), 2000);
                                            scheduleReset(3000);
                                            break;
                                        case "invite":
                                        case "party":
                                            ChatUtils.partyChat("Invite: Invites a player to the party");
                                            scheduleTask(() -> ChatUtils.partyChat("Usage: " + prefix + "Invite (player)"), 1000);
                                            scheduleTask(() -> ChatUtils.partyChat("Aliases: Invite | Party"), 2000);
                                            scheduleReset(3000);
                                            break;
                                        case "ptme":
                                            ChatUtils.partyChat("PTME: Transfers Party to the user");
                                            scheduleTask(() -> ChatUtils.partyChat("Usage: " + prefix + "PTME"), 1000);
                                            if (partyTransferMeWhitelist.getValue()) {
                                                scheduleTask(() -> ChatUtils.partyChat("Whitelisted Users Only"), 2000);
                                                scheduleTask(() -> ChatUtils.partyChat("Aliases: PTME"), 3000);
                                                scheduleReset(4000);
                                                break;
                                            }
                                            scheduleTask(() -> ChatUtils.partyChat("Aliases: PTME"), 2000);
                                            scheduleReset(3000);
                                            break;
                                        case "pt":
                                        case "partytransfer":
                                        case "ptransfer":
                                        case "transfer":
                                            ChatUtils.partyChat("PT: Transfers Party to the specified user");
                                            scheduleTask(() -> ChatUtils.partyChat("Usage: " + prefix + "PT (player)"), 1000);
                                            if (partyTransferWhitelist.getValue()) {
                                                scheduleTask(() -> ChatUtils.partyChat("Whitelisted Users Only"), 2000);
                                                scheduleTask(() -> ChatUtils.partyChat("Aliases: PT | PartyTransfer | PTransfer | Transfer"), 3000);
                                                scheduleReset(4000);
                                                break;
                                            }
                                            scheduleTask(() -> ChatUtils.partyChat("Aliases: PT | PartyTransfer | PTransfer | Transfer"), 2000);
                                            scheduleReset(3000);
                                            break;
                                        case "coords":
                                        case "coordinates":
                                            ChatUtils.partyChat("Coordinates: Sends player Coordinates");
                                            scheduleTask(() -> ChatUtils.partyChat("Usage: " + prefix + "Coordinates"), 1000);
                                            scheduleTask(() -> ChatUtils.partyChat("Aliases: Coordinates | Coords"), 2000);
                                            scheduleReset(3000);
                                            break;
                                        default:
                                            ChatUtils.partyChat("MeowMayo Party Commands | General:");
                                            scheduleTask(() -> ChatUtils.partyChat("Use " + prefix + "help general (Command) for more info"), 1000);
                                            scheduleTask(() -> ChatUtils.partyChat("Available Commands: AllInvite | Warp | Invite | PT | PTME | Coordinates"), 2000);
                                            scheduleReset(3000);
                                            break;
                                    }
                                }
                                break;
                            case "catacombs":
                                if (args.length == 2) {
                                    ChatUtils.partyChat("MeowMayo Party Commands | Catacombs:");
                                    scheduleTask(() -> ChatUtils.partyChat("Use " + prefix + "help catacombs (Command) for more info"), 1000);
                                    scheduleTask(() -> ChatUtils.partyChat("Available Commands: Floor | Master"), 2000);
                                    scheduleReset(3000);
                                } else {
                                    switch (args[2]) {
                                        case "floor":
                                        case "f":
                                            ChatUtils.partyChat("Floor: Enters the given normal mode dungeon floor");
                                            scheduleTask(() -> ChatUtils.partyChat("Usage: " + prefix + "Floor(Catacombs Floor)"), 1000);
                                            scheduleTask(() -> ChatUtils.partyChat("Aliases: Floor | F"), 2000);
                                            scheduleReset(3000);
                                            break;
                                        case "master":
                                        case "m":
                                            ChatUtils.partyChat("Master: Enters the given master mode dungeon floor");
                                            scheduleTask(() -> ChatUtils.partyChat("Usage: " + prefix + "Master(Master Catacombs Floor)"), 1000);
                                            scheduleTask(() -> ChatUtils.partyChat("Aliases: Master | M"), 2000);
                                            scheduleReset(3000);
                                            break;
                                        default:
                                            ChatUtils.partyChat("MeowMayo Party Commands | Catacombs:");
                                            scheduleTask(() -> ChatUtils.partyChat("Use " + prefix + "help catacombs (Command) for more info"), 1000);
                                            scheduleTask(() -> ChatUtils.partyChat("Available Commands: Floor | Master"), 2000);
                                            scheduleReset(3000);
                                            break;
                                    }
                                }
                                break;
                            case "kuudra":
                                if (args.length == 2) {
                                    ChatUtils.partyChat("MeowMayo Party Commands | Kuudra:");
                                    scheduleTask(() -> ChatUtils.partyChat("Use " + prefix + "help kuudra (Command) for more info"), 1000);
                                    scheduleTask(() -> ChatUtils.partyChat("Available Commands: tier"), 2000);
                                    scheduleReset(3000);
                                } else {
                                    switch (args[2]) {
                                        case "tier":
                                        case "t":
                                            ChatUtils.partyChat("Tier: Enters the given kuudra tier");
                                            scheduleTask(() -> ChatUtils.partyChat("Usage: " + prefix + "Tier(Kuudra Tier)"), 1000);
                                            scheduleTask(() -> ChatUtils.partyChat("Aliases: Tier | T"), 2000);
                                            scheduleReset(3000);
                                            break;
                                        default:
                                            ChatUtils.partyChat("MeowMayo Party Commands | Kuudra:");
                                            scheduleTask(() -> ChatUtils.partyChat("Use " + prefix + "help kuudra (Command) for more info"), 1000);
                                            scheduleTask(() -> ChatUtils.partyChat("Available Commands: tier"), 2000);
                                            scheduleReset(3000);
                                            break;
                                    }
                                }
                                break;
                            case "fun":
                                if (args.length == 2) {
                                    ChatUtils.partyChat("MeowMayo Party Commands | Fun:");
                                    scheduleTask(() -> ChatUtils.partyChat("Use " + prefix + "help fun (Command) for more info"), 1000);
                                    scheduleTask(() -> ChatUtils.partyChat("Available Commands: CoinFlip | Dice"), 2000);
                                    scheduleReset(3000);
                                } else {
                                    switch (args[2]) {
                                        case "cf":
                                        case "coinflip":
                                            ChatUtils.partyChat("CoinFlip: Flips a Coin");
                                            scheduleTask(() -> ChatUtils.partyChat("Usage: " + prefix + "CoinFlip"), 1000);
                                            scheduleTask(() -> ChatUtils.partyChat("Aliases: CoinFlip | CF"), 2000);
                                            scheduleReset(3000);
                                            break;
                                        case "dice":
                                        case "d":
                                        case "roll":
                                            ChatUtils.partyChat("Dice: Rolls a Dice");
                                            scheduleTask(() -> ChatUtils.partyChat("Usage: " + prefix + "Dice (Sides)"), 1000);
                                            scheduleTask(() -> ChatUtils.partyChat("Aliases: Dice | D | Roll"), 2000);
                                            scheduleReset(3000);
                                            break;
                                        default:
                                            ChatUtils.partyChat("MeowMayo Party Commands | Fun:");
                                            scheduleTask(() -> ChatUtils.partyChat("Use " + prefix + "help fun (Command) for more info"), 1000);
                                            scheduleTask(() -> ChatUtils.partyChat("Available Commands: CoinFlip | Dice"), 2000);
                                            scheduleReset(3000);
                                            break;
                                    }
                                }
                                break;
                            default:
                                ChatUtils.partyChat("MeowMayo Party Commands Help Menu - Current Prefix: " + prefix);
                                scheduleTask(() -> ChatUtils.partyChat("Use " + prefix + "help <general|catacombs|kuudra|fun>"), 1000);
                                scheduleReset(2000);
                                break;
                        }
                    }
                }
                break;
            case "allinv":
            case "allinvite":
                if (allInviteCommand.getValue() && PartyUtils.isLeader()) {
                    delayed = true;
                    ChatUtils.command("p settings allinvite");
                    scheduleReset(1000);
                }
                break;
            case "warp":
            case "w":
            case "pwarp":
            case "partywarp":
                if (warpCommand.getValue() && PartyUtils.isLeader()) {
                    delayed = true;
                    ChatUtils.command("p warp");
                    scheduleReset(1000);
                }
                break;
            case "invite":
            case "party":
                if (inviteCommand.getValue()) {
                    delayed = true;
                    if (args.length == 1) {
                        ChatUtils.partyChat("Please input a player to invite");
                    } else {
                        ChatUtils.command("p " + args[1]);
                    }
                    scheduleReset(1000);
                }
                break;
            case "dt":
            case "downtime": // enabled by default (assuming party commands is on!)
                PartyUtils.requestDowntime();
                break;
            case "ptme":
                if (partyTransferMeCommand.getValue() && PartyUtils.isLeader()) {
                    delayed = true;
                    if (partyTransferMeWhitelist.getValue()) {
                        if (PartyCommandListUtils.whitelist.contains(ign)) {
                            ChatUtils.command("p transfer " + ign);
                        }
                    } else {
                        ChatUtils.command("p transfer " + ign);
                    }
                    scheduleReset(1000);
                }
                break;
            case "pt":
            case "partytransfer":
            case "ptransfer":
            case "transfer":
                if (partyTransferCommand.getValue() && PartyUtils.isLeader()) {
                    delayed = true;
                    if (partyTransferWhitelist.getValue()) {
                        if (PartyCommandListUtils.whitelist.contains(ign)) {
                            if (args.length == 1) {
                                ChatUtils.partyChat("Please input a player to transfer to");
                                scheduleReset(1000);
                                return;
                            }
                            ChatUtils.command("p transfer " + args[1]);
                        }
                    } else {
                        if (args.length == 1) {
                            ChatUtils.partyChat("Please input a player to transfer to");
                            scheduleReset(1000);
                            return;
                        }
                        ChatUtils.command("p transfer " + args[1]);
                    }
                    scheduleReset(1000);
                }
                break;
            case "coords":
            case "coordinates":
                if (coordinatesCommand.getValue()) {
                    delayed = true;
                    LocalPlayer player = Minecraft.getInstance().player;
                    if (player == null) {
                        scheduleReset(1000); // just in case
                        return;
                    }
                    Vec3 location = player.position();
                    ChatUtils.partyChat("X: " + ((int) location.x) + ", Y: " + ((int) location.y) + ", Z: " + ((int) location.z));
                    scheduleReset(1000);
                }
                break;
            case "ent":
            case "entrance":
                if (catacombsEntranceCommand.getValue() && PartyUtils.isLeader()) {
                    delayed = true;
                    ChatUtils.command("joindungeon catacombs_entrance");
                    scheduleReset(1000);
                }
                break;
            case "floor1":
            case "f1":
                if (catacombsEntranceCommand.getValue() && PartyUtils.isLeader()) {
                    delayed = true;
                    ChatUtils.command("joindungeon catacombs_floor_one");
                    scheduleReset(1000);
                }
                break;
            case "floor2":
            case "f2":
                if (catacombsEntranceCommand.getValue() && PartyUtils.isLeader()) {
                    delayed = true;
                    ChatUtils.command("joindungeon catacombs_floor_two");
                    scheduleReset(1000);
                }
                break;
            case "floor3":
            case "f3":
                if (catacombsEntranceCommand.getValue() && PartyUtils.isLeader()) {
                    delayed = true;
                    ChatUtils.command("joindungeon catacombs_floor_three");
                    scheduleReset(1000);
                }
                break;
            case "floor4":
            case "f4":
                if (catacombsEntranceCommand.getValue() && PartyUtils.isLeader()) {
                    delayed = true;
                    ChatUtils.command("joindungeon catacombs_floor_four");
                    scheduleReset(1000);
                }
                break;
            case "floor5":
            case "f5":
                if (catacombsEntranceCommand.getValue() && PartyUtils.isLeader()) {
                    delayed = true;
                    ChatUtils.command("joindungeon catacombs_floor_five");
                    scheduleReset(1000);
                }
                break;
            case "floor6":
            case "f6":
                if (catacombsEntranceCommand.getValue() && PartyUtils.isLeader()) {
                    delayed = true;
                    ChatUtils.command("joindungeon catacombs_floor_six");
                    scheduleReset(1000);
                }
                break;
            case "floor7":
            case "f7":
                if (catacombsEntranceCommand.getValue() && PartyUtils.isLeader()) {
                    delayed = true;
                    ChatUtils.command("joindungeon catacombs_floor_seven");
                    scheduleReset(1000);
                }
                break;
            case "floor":
            case "f":
                if (catacombsEntranceCommand.getValue() && PartyUtils.isLeader()) {
                    delayed = true;
                    if (args.length == 2) {
                        switch (args[1]) {
                            case "1":
                                ChatUtils.command("joindungeon catacombs_floor_one");
                                break;
                            case "2":
                                ChatUtils.command("joindungeon catacombs_floor_two");
                                break;
                            case "3":
                                ChatUtils.command("joindungeon catacombs_floor_three");
                                break;
                            case "4":
                                ChatUtils.command("joindungeon catacombs_floor_four");
                                break;
                            case "5":
                                ChatUtils.command("joindungeon catacombs_floor_five");
                                break;
                            case "6":
                                ChatUtils.command("joindungeon catacombs_floor_six");
                                break;
                            case "7":
                                ChatUtils.command("joindungeon catacombs_floor_seven");
                                break;
                        }
                    }
                    scheduleReset(1000);
                }
                break;
            case "master1":
            case "m1":
                if (masterCatacombsEntranceCommand.getValue() && PartyUtils.isLeader()) {
                    delayed = true;
                    ChatUtils.command("joindungeon master_catacombs_floor_one");
                    scheduleReset(1000);
                }
                break;
            case "master2":
            case "m2":
                if (masterCatacombsEntranceCommand.getValue() && PartyUtils.isLeader()) {
                    delayed = true;
                    ChatUtils.command("joindungeon master_catacombs_floor_two");
                    scheduleReset(1000);
                }
                break;
            case "master3":
            case "m3":
                if (masterCatacombsEntranceCommand.getValue() && PartyUtils.isLeader()) {
                    delayed = true;
                    ChatUtils.command("joindungeon master_catacombs_floor_three");
                    scheduleReset(1000);
                }
                break;
            case "master4":
            case "m4":
                if (masterCatacombsEntranceCommand.getValue() && PartyUtils.isLeader()) {
                    delayed = true;
                    ChatUtils.command("joindungeon master_catacombs_floor_four");
                    scheduleReset(1000);
                }
                break;
            case "master5":
            case "m5":
                if (masterCatacombsEntranceCommand.getValue() && PartyUtils.isLeader()) {
                    delayed = true;
                    ChatUtils.command("joindungeon master_catacombs_floor_five");
                    scheduleReset(1000);
                }
                break;
            case "master6":
            case "m6":
                if (masterCatacombsEntranceCommand.getValue() && PartyUtils.isLeader()) {
                    delayed = true;
                    ChatUtils.command("joindungeon master_catacombs_floor_six");
                    scheduleReset(1000);
                }
                break;
            case "master7":
            case "m7":
                if (masterCatacombsEntranceCommand.getValue() && PartyUtils.isLeader()) {
                    delayed = true;
                    ChatUtils.command("joindungeon master_catacombs_floor_seven");
                    scheduleReset(1000);
                }
                break;
            case "master":
            case "m":
                if (masterCatacombsEntranceCommand.getValue() && PartyUtils.isLeader()) {
                    delayed = true;
                    if (args.length == 2) {
                        switch (args[1]) {
                            case "1":
                                ChatUtils.command("joindungeon master_catacombs_floor_one");
                                break;
                            case "2":
                                ChatUtils.command("joindungeon master_catacombs_floor_two");
                                break;
                            case "3":
                                ChatUtils.command("joindungeon master_catacombs_floor_three");
                                break;
                            case "4":
                                ChatUtils.command("joindungeon master_catacombs_floor_four");
                                break;
                            case "5":
                                ChatUtils.command("joindungeon master_catacombs_floor_five");
                                break;
                            case "6":
                                ChatUtils.command("joindungeon master_catacombs_floor_six");
                                break;
                            case "7":
                                ChatUtils.command("joindungeon master_catacombs_floor_seven");
                                break;
                        }
                    }
                    scheduleReset(1000);
                }
                break;
            case "dungeontimestats":
            case "dts":
            case "dtstats":
                break;
            case "basic":
            case "t1":
            case "tier1":
                if (kuudraEntranceCommand.getValue() && PartyUtils.isLeader()) {
                    delayed = true;
                    ChatUtils.command("joindungeon kuudra_normal");
                    scheduleReset(1000);
                }
                break;

            case "hot":
            case "t2":
            case "tier2":
                if (kuudraEntranceCommand.getValue() && PartyUtils.isLeader()) {
                    delayed = true;
                    ChatUtils.command("joindungeon kuudra_hot");
                    scheduleReset(1000);
                }
                break;

            case "burning":
            case "t3":
            case "tier3":
                if (kuudraEntranceCommand.getValue() && PartyUtils.isLeader()) {
                    delayed = true;
                    ChatUtils.command("joindungeon kuudra_burning");
                    scheduleReset(1000);
                }
                break;

            case "fiery":
            case "t4":
            case "tier4":
                if (kuudraEntranceCommand.getValue() && PartyUtils.isLeader()) {
                    delayed = true;
                    ChatUtils.command("joindungeon kuudra_fiery");
                    scheduleReset(1000);
                }
                break;

            case "infernal":
            case "t5":
            case "tier5":
                if (kuudraEntranceCommand.getValue() && PartyUtils.isLeader()) {
                    delayed = true;
                    ChatUtils.command("joindungeon kuudra_infernal");
                    scheduleReset(1000);
                }
                break;

            case "mm":
            case "meowmayo":
                if (selloutCommand.getValue()) {
                    delayed = true;
                    ChatUtils.partyChat("MeowMayo is a Quality of life mod that offers a ton of quality of life kuudra and dungeons features!");
                    scheduleTask(() -> ChatUtils.partyChat("Download MeowMayo here -> discord.gg/TBtp9rVHhM"), 1000);
                    scheduleReset(2000);
                }
                break;
            case "kuudratimestats":
            case "kts":
            case "ktstats":
                break;
            case "cf":
            case "coinflip":
                if (coinflipCommand.getValue()) {
                    delayed = true;
                    if (r.nextBoolean()) {
                        ChatUtils.partyChat("Heads!");
                    } else {
                        ChatUtils.partyChat("Tails!");
                    }
                    scheduleReset(1000);
                }
                break;
            case "dice":
            case "d":
            case "roll":
                if (diceCommand.getValue()) {
                    delayed = true;
                    if (args.length == 1) {
                        ChatUtils.partyChat("Please input how many sides the dice should have");
                    } else {
                        try {
                            int side = Integer.parseInt(args[1]);
                            ChatUtils.partyChat("You rolled a " + (r.nextInt(side) + 1) + "!");
                        } catch (NumberFormatException e) {
                            ChatUtils.partyChat("Please input a valid number of sides");
                        }
                    }
                    scheduleReset(1000);
                }
                break;
        }
    }

    private static void scheduleReset(long delayMs) {
        scheduleTask(() -> delayed = false, delayMs);
    }
}

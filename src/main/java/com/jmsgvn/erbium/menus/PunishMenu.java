package com.jmsgvn.erbium.menus;

import com.jmsgvn.erbium.Locale;
import com.jmsgvn.erbium.profiles.Profile;
import com.jmsgvn.erbium.punishments.Punishment;
import com.jmsgvn.erbium.utils.Stackables;
import com.jmsgvn.erbium.utils.TranslatePunishment;
import com.jmsgvn.erbium.Erbium;
import com.jmsgvn.erbium.utils.Colors;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import xyz.leuo.gooey.button.Button;
import xyz.leuo.gooey.gui.GUI;
import xyz.leuo.gooey.gui.GUIUpdate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PunishMenu {

    private static boolean silent;

    public static void openPunishMenu(Erbium plugin, Player staff, Profile targetProfile, String notes) {
        Configuration config = plugin.getMessagesFile();

        GUI gui = new GUI(Locale.PUNISH_TITLE.format(plugin).replace("%player%", targetProfile.getName()), 45);
        silent = true;

        gui.setUpdate(new GUIUpdate() {
            @Override
            public void onUpdate(GUI gui) {
                String name = silent ? "&a&lSilent Punishment (ON)" : "&C&lSilent Punishment (OFF)";

                Button button3 = new Button(Material.FEATHER, name);
                button3.setLore("&fClick to toggle silent punishments");
                button3.setButtonAction((player, gui1, b, event) -> {
                    silent = !silent;
                    b.setName(silent ? "&a&lSilent Punishment (ON)" : "&C&lSilent Punishment (OFF)");
                    gui.update();
                });
                gui.setButton(3, button3);
            }
        });

        gui.setButton(0, new Button(Material.STAINED_GLASS_PANE, ""));
        gui.setButton(1, new Button(Material.STAINED_GLASS_PANE, ""));
        gui.setButton(2, new Button(Material.STAINED_GLASS_PANE, ""));
        gui.setButton(6, new Button(Material.STAINED_GLASS_PANE, ""));
        gui.setButton(7, new Button(Material.STAINED_GLASS_PANE, ""));
        gui.setButton(8, new Button(Material.STAINED_GLASS_PANE, ""));

        String name = silent ? "&a&lSilent Punishment (ON)" : "&C&lSilent Punishment (OFF)";

        Button button3 = new Button(Material.FEATHER, name);
        button3.setLore("&fClick to toggle silent punishments");
        button3.setButtonAction((player, gui1, b, event) -> {
            silent = !silent;
            b.setName(silent ? "&a&lSilent Punishment (ON)" : "&C&lSilent Punishment (OFF)");
            gui.update();
        });
        gui.setButton(3, button3);

        Button button4 = new Button(Material.PAPER, "&c&lNotes");
        button4.setLore("&f" + notes);
        gui.setButton(4, button4);

        Button button5 = new Button(Material.BOOK_AND_QUILL, "&6&lHistory");
        button5.setLore("&fCheck the players punishment history.");
        button5.setButtonAction((player, gui1, b, event) -> {
            player.closeInventory();
            HistoryMenu.openHistoryMenu(plugin, staff, targetProfile, notes);
        });
        gui.setButton(5, button5);

        gui.setButton(9, new Button(Material.STAINED_GLASS_PANE, ""));
        gui.setButton(10, new Button(Material.STAINED_GLASS_PANE, ""));
        gui.setButton(11, new Button(Material.STAINED_GLASS_PANE, ""));
        gui.setButton(12, new Button(Material.STAINED_GLASS_PANE, ""));
        gui.setButton(13, new Button(Material.STAINED_GLASS_PANE, ""));
        gui.setButton(14, new Button(Material.STAINED_GLASS_PANE, ""));
        gui.setButton(15, new Button(Material.STAINED_GLASS_PANE, ""));
        gui.setButton(16, new Button(Material.STAINED_GLASS_PANE, ""));
        gui.setButton(17, new Button(Material.STAINED_GLASS_PANE, ""));

        for (String key : config.getConfigurationSection("MENUS.PUNISH.SLOTS").getKeys(false)) {
            ConfigurationSection section = config.getConfigurationSection("MENUS.PUNISH.SLOTS." + key);

            if (section.getString("PERMISSION") != null && !staff.hasPermission(section.getString("PERMISSION"))) continue;

            gui.addButton(createButton(plugin, targetProfile, staff, notes, section));
        }

        plugin.getServer().getScheduler().runTask(plugin, () -> {
            gui.open(staff);
        });
    }

    public static Button createButton(Erbium plugin, Profile targetProfile, Player issuer, String notes, ConfigurationSection section) {
        Button button = new Button(Material.valueOf(section.getString("MATERIAL")), section.getString("NAME"));
        int offenses = Stackables.offenseNumber(targetProfile, section.getName());

        List<String> lore = new ArrayList<>();
        for (String string : section.getStringList("LORE")) {
            lore.add(Colors.convertLegacyColors(string.replace("%offenses%", offenses + "")));
        }

        button.setLore(lore);

        String reason = notes == null || notes.equals("None") ? section.getString("DEFAULT_REASON") : notes;
        List<String> punishmentsList = section.getStringList("PUNISHMENTS");

        String punishmentString;
        try {
            punishmentString = punishmentsList.get(offenses);
        } catch (IndexOutOfBoundsException e) {
            punishmentString = punishmentsList.get(punishmentsList.size() - 1);
        }

        Punishment.Type type = TranslatePunishment.type(punishmentString);
        Date expiration = TranslatePunishment.expires(punishmentString);

        button.setButtonAction((player, gui, b, event) -> {
            if (targetProfile.getActivePunishment(type) == null || type.equals(Punishment.Type.KICK) || type.equals(Punishment.Type.WARN)) {
                targetProfile.punish(type, section.getName(), issuer.getUniqueId(), reason, expiration, silent);

                issuer.sendMessage(Locale.PUNISHMENT_SUCCESS.format(plugin)
                        .replace("%type%", type.pastMessage())
                        .replace("%target%", targetProfile.getName())
                        .replace("%reason%", reason));
            } else {
                issuer.sendMessage(ChatColor.RED + "The target you specified already has an active punishment of that type. You must unmute/unban that player first!");;
            }

            issuer.closeInventory();
        });
        return button;
    }
}

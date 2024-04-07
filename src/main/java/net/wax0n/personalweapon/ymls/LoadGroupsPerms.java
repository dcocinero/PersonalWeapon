package net.wax0n.personalweapon.ymls;

import net.wax0n.personalweapon.PersonalWeapon;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.logging.Level;

public class LoadGroupsPerms extends YmlConstructor{

    private final HashMap<String, String> groupResetSkillTimer = new HashMap<>();
    private final HashMap<String, String> groupResetTextureTimer = new HashMap<>();
    private final HashMap<String, Integer> groupAmountWeapons = new HashMap<>();

    public LoadGroupsPerms(PersonalWeapon plugin) {
        super(plugin, "groups");
        setDefaultGroups();
        loadGroups();
    }

    private void setDefaultGroups(){
        if (!configFile.contains("Groups")) {
            configFile.set("Groups.default.resetSkillTimer", "6d 23h 59m");
            configFile.set("Groups.default.resetTextureTimer", "13d 23h 59m");
            configFile.set("Groups.default.weapons", 1);
        }
        saveConfig();
    }

    private void loadGroups(){
        if (!configFile.contains("Groups")) return;

        ConfigurationSection defaultConfig =  configFile.getConfigurationSection("Groups");

        for (String groupName : defaultConfig.getKeys(false)){

            ConfigurationSection itemSection = defaultConfig.getConfigurationSection(groupName);

            String resetSkillTimer = itemSection.getString("resetSkillTimer");
            String resetTextureTimer = itemSection.getString("resetTextureTimer");
            int amountWeapon = itemSection.getInt("weapons");

            groupResetSkillTimer.put(groupName, resetSkillTimer);
            groupResetTextureTimer.put(groupName, resetTextureTimer);
            groupAmountWeapons.put(groupName, amountWeapon);
        }
    }

    public long getResetSkillTimer(Player player){
        String group = getGroup(player);
        String time = groupResetSkillTimer.get(group);
        return stringToLongTime(time);
    }

    public long getResetTextureTimer(Player player){
        String group = getGroup(player);
        String time = groupResetTextureTimer.get(group);
        return stringToLongTime(time);
    }

    public int getAmountWeapon(Player player){
        String group = getGroup(player);
        return groupAmountWeapons.get(group);
    }

    private String getGroup(Player player){
        String group = "default";
        for (String g : groupResetTextureTimer.keySet()){  //use one of hash to get both keyset idk if i should change it
            if (player.hasPermission("group." + g)){
                group = g;
            }
        }
        return group;
    }


    private static long stringToLongTime(String input) {
        String[] parts = input.split("\\s+");
        long totalSeg = 0;

        for (String part : parts) {
            char unit = part.charAt(part.length() - 1);
            int value = Integer.parseInt(part.substring(0, part.length() - 1));

            switch (unit) {
                case 'd':
                    totalSeg += (long) value * 24 * 60 * 60;
                    break;
                case 'h':
                    totalSeg += (long) value * 60 * 60;
                    break;
                case 'm':
                    totalSeg += value * 60L;
                    break;
                default:
                    throw new IllegalArgumentException("Unit unknown: " + unit);
            }
        }

        return totalSeg;
    }

    public static String segToStringTime(long segs) {
        long days = segs / (24 * 60 * 60);
        segs %= (24 * 60 * 60);
        long hours = segs / (60 * 60);
        segs %= (60 * 60);
        long minutes = segs / 60;

        StringBuilder resultado = new StringBuilder();
        if (days > 0) {
            resultado.append(days).append("d ");
        }
        if (hours > 0) {
            resultado.append(hours).append("h ");
        }
        if (minutes > 0) {
            resultado.append(minutes).append("m");
        }

        return resultado.toString().trim();
    }
}

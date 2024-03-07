package net.wax0n.personalweapon.ymls;

import net.wax0n.personalweapon.PersonalWeapon;
import net.wax0n.personalweapon.utils.PlayerUUID;
import net.wax0n.personalweapon.weapon.Weapon;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class SaveWeapons extends YmlConstructor{

    private final NamespacedKey namespacedKey;

    public SaveWeapons(PersonalWeapon plugin) {
        super(plugin, "weaponDB");
        this.namespacedKey = new NamespacedKey(plugin, "WeaponItem");
    }

    public void savePlayerWeapons(Player player, ArrayList<Weapon> weapons, int indexSelected){
        for (int i = 0; i < weapons.size(); i++) {
            Weapon weapon = weapons.get(i);

            int modelData = 0;

            if (weapon.getItemMeta().hasCustomModelData()){
                modelData = weapon.getItemMeta().getCustomModelData();
            }

            setWeaponYML(PlayerUUID.getUUID(player).toString(), i, weapon.getName(), weapon.getLevel(), weapon.getXp(), weapon.getDamagePoints(),
                    weapon.getSpeedPoints(), weapon.getSmitePoints(), weapon.getArthropodPoints(), weapon.getKnockbackPoints(),
                    weapon.getLootingPoints(), weapon.getFirePoints(), weapon.getSweepPoints(), weapon.getFreePoints(), weapon.getType().name(), modelData);
        }
        this.configFile.set("Players." + PlayerUUID.getUUID(player).toString() + ".selected", indexSelected);
        this.saveConfig();
    }

    private void setWeaponYML(String UUID, int index, String nameWeapon, int levelWeapon, double xp, int damagePoints,
                              int speedPoints, int smitePoints, int arthropodPoints, int knockbackPoints, int lootingPoints,
                              int firePoints, int sweepPoints, int freePoints, String material, int modelData){
        this.configFile.set("Players." + UUID + "." + index + ".name", nameWeapon);
        this.configFile.set("Players." + UUID + "." + index + ".material", material);
        this.configFile.set("Players." + UUID + "." + index + ".level", levelWeapon);
        this.configFile.set("Players." + UUID + "." + index + ".xp", xp);
        this.configFile.set("Players." + UUID + "." + index + ".damage", damagePoints);
        this.configFile.set("Players." + UUID + "." + index + ".speed", speedPoints);
        this.configFile.set("Players." + UUID + "." + index + ".smite", smitePoints);
        this.configFile.set("Players." + UUID + "." + index + ".arthropod", arthropodPoints);
        this.configFile.set("Players." + UUID + "." + index + ".knockback", knockbackPoints);
        this.configFile.set("Players." + UUID + "." + index + ".looting", lootingPoints);
        this.configFile.set("Players." + UUID + "." + index + ".sweep", sweepPoints);
        this.configFile.set("Players." + UUID + "." + index + ".fire",firePoints);
        this.configFile.set("Players." + UUID + "." + index + ".freePoints", freePoints);

        if (modelData != 0){
            this.configFile.set("Players." + UUID + "." + index + ".modelData", modelData);
        }
    }

    public ArrayList<Weapon> loadWeapons(Player player){
        ConfigurationSection playerSection =  configFile.getConfigurationSection("Players." + PlayerUUID.getUUID(player));

        ArrayList<Weapon> listWeapons = new ArrayList<>();

        if (playerSection != null){
            for (String weapons :  playerSection.getKeys(false)){
                if (!weapons.equals("selected")){
                    ConfigurationSection index = playerSection.getConfigurationSection(weapons);

                    String name = index.getString("name");
                    String material = index.getString("material");
                    int level = index.getInt("level");
                    double xp = index.getDouble("xp");
                    int damage = index.getInt("damage");
                    int speed = index.getInt("speed");
                    int smite = index.getInt("smite");
                    int arthropod = index.getInt("arthropod");
                    int knockback = index.getInt("knockback");
                    int looting = index.getInt("looting");
                    int fire = index.getInt("fire");
                    int sweep = index.getInt("sweep");
                    int freePoints = index.getInt("freePoints");
                    int customModeldata = index.getInt("modelData");
                    try {
                        Weapon weapon = new Weapon(new ItemStack(Material.getMaterial(material)), name, level, xp, freePoints, damage, speed, smite, arthropod, fire, looting, sweep, knockback, customModeldata, namespacedKey);
                        listWeapons.add(weapon);
                    } catch (Exception e) {
                        player.sendMessage(ChatColor.RED + "Error loading weapon");
                    }
                }
            }
        }
        return listWeapons;
    }

    public int getSelectedIndex(Player player){
        return configFile.getInt("Players." + PlayerUUID.getUUID(player) + ".selected");
    }

    public void deleteWeapon(Player player, int index){
        this.configFile.set("Players." + PlayerUUID.getUUID(player) + "." + index, null);
        saveConfig();
    }
}

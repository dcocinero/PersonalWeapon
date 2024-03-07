package net.wax0n.personalweapon.ymls;

import net.wax0n.personalweapon.PersonalWeapon;
import net.wax0n.personalweapon.utils.PlayerUUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

public class LoadItemTextures extends YmlConstructor{

    private final HashMap<Integer, ItemStack> textures = new HashMap<>();

    public LoadItemTextures(PersonalWeapon plugin) {
        super(plugin, "itemtextures");
        setBasicTextures();
    }

    private void setBasicTextures(){
        if (!configFile.contains("sizeGUI")){
            configFile.set("sizeGUI", 6*9);
        }

        if (!configFile.contains("Page-1")){
            setItemYML("Page-1", Material.WOODEN_SWORD.name(), Material.WOODEN_SWORD.name(), "Basic Texture", 1);
            setItemYML("Page-1", Material.STONE_SWORD.name(), Material.STONE_SWORD.name(), "Basic Texture", 2);
            setItemYML("Page-1", Material.GOLDEN_SWORD.name(), Material.GOLDEN_SWORD.name(), "Basic Texture", 3);
            setItemYML("Page-1", Material.IRON_SWORD.name(), Material.IRON_SWORD.name(), "Basic Texture", 4);
            setItemYML("Page-1", Material.DIAMOND_SWORD.name(), Material.DIAMOND_SWORD.name(), "Basic Texture", 5);
            setItemYML("Page-1", Material.NETHERITE_SWORD.name(), Material.NETHERITE_SWORD.name(), "Basic Texture", 6);
        }
        saveConfig();
    }

    private void setItemYML(String page, String name, String material, String lore, int slot){
        configFile.set(page + "." + name + ".material", material);
        configFile.set(page + "." + name + ".slot", slot);
        configFile.set(page + "." + name + ".lore", lore);
    }

    private void chargeItems(int numPage){
        ConfigurationSection page =  configFile.getConfigurationSection("Page-" + numPage);
        if (page != null){
            for (String itemName : page.getKeys(false)){

                ConfigurationSection itemSection = page.getConfigurationSection(itemName);

                String material = itemSection.getString("material");
                int slot = itemSection.getInt("slot");
                String lore = itemSection.getString("lore");
                int customModelData = itemSection.getInt("customId"); //add

                ItemStack itemStack;
                if (Material.getMaterial(material) != null){
                    itemStack = new ItemStack(Material.getMaterial(material));
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                    itemMeta.setDisplayName(ChatColor.AQUA + itemName);

                    List<String> loreList = new ArrayList<>();
                    loreList.add(lore);
                    itemMeta.setLore(loreList);
                    if (customModelData != 0){
                        itemMeta.setCustomModelData(customModelData);
                    }
                    itemStack.setItemMeta(itemMeta);
                    textures.put(slot, itemStack);
                }
            }
        }
    }

    public HashMap<Integer, ItemStack> getItems(int numPage) {
        chargeItems(numPage);
        return textures;
    }

    public int getSizeGUI(){
        return configFile.getInt("sizeGUI");
    }
}

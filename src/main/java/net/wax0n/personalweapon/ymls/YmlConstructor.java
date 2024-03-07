package net.wax0n.personalweapon.ymls;

import net.wax0n.personalweapon.PersonalWeapon;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public abstract class YmlConstructor {


    //TODO update all yml file using this abstract class
    private File file;
    protected FileConfiguration configFile;
    private PersonalWeapon plugin;

    public YmlConstructor(PersonalWeapon plugin, String nameFile) {
        this.plugin = plugin;
        initFile(nameFile);
    }

    private void initFile(String nameFile){
        if (!this.plugin.getDataFolder().exists()){
            this.plugin.getDataFolder().mkdir();
        }
        this.file = new File(this.plugin.getDataFolder(), nameFile + ".yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try {
                this.plugin.saveResource(nameFile + ".yml", false);
            } catch (Exception e) {
                // oww
            }
        }

        configFile = new YamlConfiguration();
        try {
            configFile.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    protected void saveConfig(){
        try {
            configFile.save(file);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save config to " + this.file, e);
        }
    }

}

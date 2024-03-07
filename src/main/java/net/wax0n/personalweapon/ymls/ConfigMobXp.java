package net.wax0n.personalweapon.ymls;

import net.wax0n.personalweapon.PersonalWeapon;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;

import java.util.HashMap;

public class ConfigMobXp extends YmlConstructor{

    private final HashMap<String, Double> mobs = new HashMap<>();

    public ConfigMobXp(PersonalWeapon plugin) {
        super(plugin, "xpmob");
        setDefaultMobs();
        chargeMobs();
    }

    private void setDefaultMobs(){
        if (!configFile.contains("Default")){
            setItemYML(EntityType.ALLAY.name(), 2);
            setItemYML(EntityType.AXOLOTL.name(), 2);
            setItemYML(EntityType.BAT.name(), 2);
            setItemYML(EntityType.BEE.name(), 2);
            setItemYML(EntityType.BLAZE.name(), 5);
            setItemYML(EntityType.BREEZE.name(), 5);
            setItemYML(EntityType.CAMEL.name(), 2);
            setItemYML(EntityType.CAT.name(), 2);
            setItemYML(EntityType.CAVE_SPIDER.name(), 5);
            setItemYML(EntityType.CHICKEN.name(), 2);
            setItemYML(EntityType.COD.name(), 0);
            setItemYML(EntityType.COW.name(), 2);
            setItemYML(EntityType.CREEPER.name(), 5);
            setItemYML(EntityType.DOLPHIN.name(), 2);
            setItemYML(EntityType.DONKEY.name(), 2);
            setItemYML(EntityType.DROWNED.name(), 5);
            setItemYML(EntityType.ELDER_GUARDIAN.name(), 70);
            setItemYML(EntityType.ENDER_CRYSTAL.name(), 1);
            setItemYML(EntityType.ENDER_DRAGON.name(), 500);
            setItemYML(EntityType.ENDERMAN.name(), 10);
            setItemYML(EntityType.ENDERMITE.name(), 5);
            setItemYML(EntityType.EVOKER.name(), 7);
            setItemYML(EntityType.FOX.name(), 2);
            setItemYML(EntityType.FROG.name(), 2);
            setItemYML(EntityType.GHAST.name(), 5);
            setItemYML(EntityType.GIANT.name(), 5);
            setItemYML(EntityType.GLOW_SQUID.name(), 2);
            setItemYML(EntityType.GOAT.name(), 2);
            setItemYML(EntityType.GUARDIAN.name(), 2);
            setItemYML(EntityType.HOGLIN.name(), 5);
            setItemYML(EntityType.HORSE.name(), 2);
            setItemYML(EntityType.HUSK.name(), 5);
            setItemYML(EntityType.ILLUSIONER.name(), 7);
            setItemYML(EntityType.IRON_GOLEM.name(), 0);
            setItemYML(EntityType.LLAMA.name(), 2);
            setItemYML(EntityType.MAGMA_CUBE.name(), 5);
            setItemYML(EntityType.MULE.name(), 2);
            setItemYML(EntityType.MUSHROOM_COW.name(), 2);
            setItemYML(EntityType.OCELOT.name(), 2);
            setItemYML(EntityType.PANDA.name(), 2);
            setItemYML(EntityType.PARROT.name(), 2);
            setItemYML(EntityType.PHANTOM.name(), 5);
            setItemYML(EntityType.PIG.name(), 2);
            setItemYML(EntityType.PIGLIN.name(), 3);
            setItemYML(EntityType.PIGLIN_BRUTE.name(), 5);
            setItemYML(EntityType.PILLAGER.name(), 5);
            setItemYML(EntityType.POLAR_BEAR.name(), 2);
            setItemYML(EntityType.PUFFERFISH.name(), 0);
            setItemYML(EntityType.RABBIT.name(), 2);
            setItemYML(EntityType.RAVAGER.name(), 12);
            setItemYML(EntityType.SALMON.name(), 0);
            setItemYML(EntityType.SHEEP.name(), 2);
            setItemYML(EntityType.SHULKER.name(), 5);
            setItemYML(EntityType.SILVERFISH.name(), 3);
            setItemYML(EntityType.SKELETON.name(), 5);
            setItemYML(EntityType.SKELETON_HORSE.name(), 3);
            setItemYML(EntityType.SLIME.name(), 5);
            setItemYML(EntityType.SNIFFER.name(), 2);
            setItemYML(EntityType.SNOWMAN.name(), 0);
            setItemYML(EntityType.SPIDER.name(), 5);
            setItemYML(EntityType.SQUID.name(), 2);
            setItemYML(EntityType.STRAY.name(), 5);
            setItemYML(EntityType.STRIDER.name(), 2);
            setItemYML(EntityType.TADPOLE.name(), 2);
            setItemYML(EntityType.TRADER_LLAMA.name(), 0);
            setItemYML(EntityType.TROPICAL_FISH.name(), 0);
            setItemYML(EntityType.TURTLE.name(), 2);
            setItemYML(EntityType.VEX.name(), 5);
            setItemYML(EntityType.VILLAGER.name(), 2);
            setItemYML(EntityType.VINDICATOR.name(), 7);
            setItemYML(EntityType.WANDERING_TRADER.name(), 2);
            setItemYML(EntityType.WARDEN.name(), 300);
            setItemYML(EntityType.WITCH.name(), 5);
            setItemYML(EntityType.WITHER.name(), 70);
            setItemYML(EntityType.WITHER_SKELETON.name(), 7);
            setItemYML(EntityType.WOLF.name(), 2);
            setItemYML(EntityType.ZOGLIN.name(), 7);
            setItemYML(EntityType.ZOMBIE.name(), 5);
            setItemYML(EntityType.ZOMBIE_HORSE.name(), 3);
            setItemYML(EntityType.ZOMBIE_VILLAGER.name(), 5);
            setItemYML(EntityType.ZOMBIFIED_PIGLIN.name(), 5);
        }
        saveConfig();
    }
    private void setItemYML(String mobName, double xp){
        configFile.set("Default." + mobName + ".XP", xp);
    }

    private void chargeMobs(){
        ConfigurationSection defaultConfig =  configFile.getConfigurationSection("Default");

        for (String mobName : defaultConfig.getKeys(false)){

            ConfigurationSection itemSection = defaultConfig.getConfigurationSection(mobName);

            double xp = itemSection.getDouble("XP");

            mobs.put(mobName, xp);
        }
    }

    public HashMap<String, Double> getMobs() {
        return mobs;
    }
}

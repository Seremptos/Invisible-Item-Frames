package fr.seremptos.invisibleitemframes;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class Main extends JavaPlugin {

    public static final List<Material> frames = List.of(Material.ITEM_FRAME, Material.GLOW_ITEM_FRAME);
    public static final String INVISIBLE = "Invisible";
    public static final String ENTITY_DATA = NamespacedKey.minecraft("entity_data").asString();


    @Override
    public void onEnable() {
        addInvisibleItemFrameRecipe();
        Bukkit.getPluginManager().registerEvents(new Listener(this), this);
        saveDefaultConfig();
    }

    public void addInvisibleItemFrameRecipe() {
        for(Material frame : frames) {
            NamespacedKey key = new NamespacedKey(this, "invisible_"+frame);
            ShapedRecipe recipe = new ShapedRecipe(key, getInvisibleFrame(this, new ItemStack(frame)));
            String[] shape = new String[]{
                    getConfig().getString("recipe.first-row", " M "),
                    getConfig().getString("recipe.second-row", "MIM"),
                    getConfig().getString("recipe.third-row", " M ")
            };

            recipe.shape(shape);

            char[] recipeMaterials = Arrays.stream(shape)
                    .distinct()
                    .collect(Collectors.joining())
                    .strip()
                    .replace(" ", "")
                    .toCharArray();

            for (char character: recipeMaterials) {
                String material = getConfig().getString("recipe."+character);
                recipe.setIngredient(character, material.equalsIgnoreCase("IFRAME") ? frame : Material.matchMaterial(material));
            }

            if (Bukkit.addRecipe(recipe)) {
                getLogger().info("Recette ajoutée avec succès ! ("+key.asString()+")");
            } else {
                getLogger().warning("La recette n'a pas pu être ajoutée ! ("+key.asString()+")");
            }
        }
    }

    public static ItemStack getInvisibleFrame(Plugin plugin, ItemStack frame) {
        NBT.modifyComponents(frame, nbt -> {
            ReadWriteNBT entity_data = nbt.getOrCreateCompound(ENTITY_DATA);
            entity_data.setString("id", frame.getType().getKey().asString());
            entity_data.setBoolean(INVISIBLE, true);
        });
        frame.editMeta(meta -> meta.displayName(
                MiniMessage.miniMessage().deserialize(
                        plugin.getConfig()
                                .getString("item-name", "%name% invisible")
                                .replace("%name%", "<lang:"+ frame.translationKey()+ ">")
                ).decoration(TextDecoration.ITALIC, false)
        ));
        return frame;
    }
}

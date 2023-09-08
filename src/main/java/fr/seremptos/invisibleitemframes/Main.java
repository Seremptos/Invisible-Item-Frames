package fr.seremptos.invisibleitemframes;

import de.tr7zw.nbtapi.NBTItem;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class Main extends JavaPlugin {

    public static List<Material> frames = List.of(Material.ITEM_FRAME, Material.GLOW_ITEM_FRAME);


    @Override
    public void onEnable() {
        addInvisibleItemFrameRecipe();
        Bukkit.getPluginManager().registerEvents(new Listener(this), this);
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
    }

    public void addInvisibleItemFrameRecipe() {
        for(Material frame : frames) {
            NamespacedKey key = new NamespacedKey(this, "invisible_"+frame);
            ShapedRecipe recipe = new ShapedRecipe(key, getInvisibleFrame(new ItemStack(frame)));
            recipe.shape(" M ", "MIM", " M ");
            recipe.setIngredient('M', Material.PHANTOM_MEMBRANE);
            recipe.setIngredient('I', frame);

            if (Bukkit.addRecipe(recipe)) {
                getLogger().info("Recette ajoutée avec succès ! ("+key.asString()+")");
            } else {
                getLogger().warning("La recette n'a pas pu être ajoutée ! ("+key.asString()+")");
            }
        }
    }

    public static ItemStack getInvisibleFrame(ItemStack frame) {
        NBTItem iframe_nbt = new NBTItem(frame);
        iframe_nbt.addCompound("EntityTag").setBoolean("Invisible", true);
        ItemStack iframe = iframe_nbt.getItem();
        iframe.editMeta(meta -> meta.displayName(MiniMessage.miniMessage().deserialize("<lang:"+ frame.translationKey()+ "> invisible").decoration(TextDecoration.ITALIC, false)));
        return iframe;
    }
}

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
        Bukkit.getPluginManager().registerEvents(new Listener(), this);

    }

    @Override
    public void onDisable() {
    }

    public void addInvisibleItemFrameRecipe() {
        for(Material material : frames) {
            ItemStack frame = new ItemStack(material);
            NBTItem iframe_nbt = new NBTItem(frame);
            iframe_nbt.addCompound("EntityTag").setBoolean("Invisible", true);

            ItemStack iframe = iframe_nbt.getItem();
            iframe.editMeta(meta -> meta.displayName(MiniMessage.miniMessage().deserialize("<lang:"+ frame.translationKey()+ "> invisible").decoration(TextDecoration.ITALIC, false)));

            NamespacedKey key = new NamespacedKey(this, "invisible_"+frame.getType());
            ShapedRecipe recipe = new ShapedRecipe(key, iframe);
            recipe.shape(" M ", "MIM", " M ");
            recipe.setIngredient('M', Material.PHANTOM_MEMBRANE);
            recipe.setIngredient('I', frame.getType());

            if (Bukkit.addRecipe(recipe)) {
                getLogger().info("Recette ajoutée avec succès ! ("+key.asString()+")");
            } else {
                getLogger().warning("La recette n'a pas pu être ajoutée ! ("+key.asString()+")");
            }
        }
    }
}

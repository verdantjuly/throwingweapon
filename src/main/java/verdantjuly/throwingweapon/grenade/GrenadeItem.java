package verdantjuly.throwingweapon;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GrenadeItem {

    public static ItemStack createGrenadeItem() {
        ItemStack grenade = new ItemStack(Material.SNOWBALL);
        ItemMeta meta = grenade.getItemMeta(); // Get the ItemMeta

        if (meta != null) {
            meta.setDisplayName("Grenade");
            meta.setCustomModelData(1234); // Set custom model data to identify the grenade
            grenade.setItemMeta(meta); // Apply the meta back to the item
        }

        return grenade;
    }

    // Check if an ItemStack has custom model data and retrieve it
    public static int getCustomModelData(ItemStack item) {
        if (item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null && meta.hasCustomModelData()) {
                return meta.getCustomModelData();
            }
        }
        return -1; // Return -1 if no custom model data is present
    }
}

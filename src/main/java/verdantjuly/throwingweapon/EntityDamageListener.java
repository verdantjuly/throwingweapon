package verdantjuly.throwingweapon.grenade;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.Material;

public class EntityDamageListener implements Listener {

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        Entity damager = event.getDamager();

        // 플레이어가 다른 엔티티를 공격했을 경우
        if (damager instanceof Player) {
            Player player = (Player) damager;
            ItemStack itemInHand = player.getInventory().getItemInMainHand();

            // 아이템이 SNOWBALL이고 이름이 Grenade인 경우
            if (itemInHand.getType() == Material.SNOWBALL) {
                ItemMeta meta = itemInHand.getItemMeta();
                if (meta != null && meta.hasDisplayName() && meta.getDisplayName().equals("Grenade")) {
                    // 엔티티에 파편 피해 처리 (예시로 10만큼 피해를 주는 경우)
                    event.setDamage(10.0); // 예시로 10만큼 피해를 줄 수 있습니다.
                    player.sendMessage("수류탄을 던졌습니다.");
                }
            }
        }
    }
}

package verdantjuly.throwingweapon;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import verdantjuly.throwingweapon.weapons.GrenadeEntity;

public class EntityDamageListener implements Listener {

    // 수류탄 던지기 처리
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        // 우클릭으로 수류탄 던지기
        if (event.getAction().toString().contains("RIGHT_CLICK") && player.getInventory().getItemInMainHand().getType() == Material.SNOWBALL) {
            ItemStack itemInHand = player.getInventory().getItemInMainHand();
            ItemMeta meta = itemInHand.getItemMeta();

            // 아이템이 수류탄이라면 (이름이 "Grenade")
            if (meta != null && meta.hasDisplayName() && meta.getDisplayName().equals("Grenade")) {
                // 던져진 아이템을 생성하고 물리적인 속도를 적용
                Plugin plugin = event.getPlayer().getServer().getPluginManager().getPlugin("throwingweapon");
                // 수류탄 아이템을 생성하고 던지는 위치 설정
                Item grenadeItem = player.getWorld().dropItem(player.getLocation(),GrenadeEntity.createGrenade() ); // 플레이어의 위치에서 수류탄 생성
                // 수류탄의 물리적 속도 설정 (플레이어의 시선 방향으로 던짐)
                Vector direction = player.getLocation().getDirection().normalize().multiply(1.5); // 시선 방향으로 속도 설정
                grenadeItem.setVelocity(direction); // 물리적 속도 적용

                ItemStack grenadeItemStack = grenadeItem.getItemStack();
                GrenadeEntity grenadeEntity = new GrenadeEntity(grenadeItemStack, player, plugin, grenadeItem);
                grenadeEntity.saveToItemData(); // 퓨즈 시작 (5초 뒤 폭발)

                // 수류탄 사용 후 아이템 감소
                itemInHand.setAmount(itemInHand.getAmount() - 1);
                player.sendMessage("수류탄을 던졌습니다!");
            }
        }
    }

    // 수류탄이 엔티티에 맞았을 때 피해 처리
    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        Entity damager = event.getDamager();

        // 수류탄을 던진 플레이어가 다른 엔티티를 공격했을 경우
        if (damager instanceof Player) {
            Player player = (Player) damager;
            ItemStack itemInHand = player.getInventory().getItemInMainHand();

            // 아이템이 SNOWBALL이고 이름이 "Grenade"인 경우
            if (itemInHand.getType() == Material.SNOWBALL) {
                ItemMeta meta = itemInHand.getItemMeta();
                if (meta != null && meta.hasDisplayName() && meta.getDisplayName().equals("Grenade")) {
                    // 수류탄이 엔티티에 맞으면 10만큼 피해 처리
                    event.setDamage(10.0); // 수류탄 피해를 10으로 설정
                }
            }
        }
    }

    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        Item item = event.getItem();

        // 아이템이 수류탄인 경우
        if (item.getItemStack().hasItemMeta()) {
            ItemMeta meta = item.getItemStack().getItemMeta();
            if (meta != null && meta.hasDisplayName() && meta.getDisplayName().equals("Grenade")) {
                event.setCancelled(true); // 수류탄은 주울 수 없게 처리
            }
        }
    }
}

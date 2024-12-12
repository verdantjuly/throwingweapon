package verdantjuly.throwingweapon;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.NamespacedKey;
import verdantjuly.throwingweapon.weapons.GrenadeEntity;

public class WeaponManager {

    private Plugin plugin;

    public WeaponManager(Plugin plugin) {
        this.plugin = plugin;
        startFuseTask();  // 퓨즈 확인 작업 시작
    }

    // 퓨즈를 체크하는 작업을 1틱마다 수행
    public void startFuseTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                handleExplosion();  // 월드 내 모든 수류탄에 대해 퓨즈 확인
            }
        }.runTaskTimer(plugin, 0L, 1L);  // 1틱마다 실행
    }

    // 폭발 처리
    public void handleExplosion() {
        // 모든 월드에서 아이템 엔티티를 확인
        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                // Item 엔티티일 경우
                if (entity instanceof Item) {
                    Item itemEntity = (Item) entity;

                    // 떨어진 아이템만 처리 (isOnGround()를 사용하여)
                    if (itemEntity.isOnGround()) {
                        ItemStack itemStack = itemEntity.getItemStack(); // ItemStack 가져오기

                        // 아이템에 메타 데이터가 있고, "fuseTicks" 키가 존재하면 무기 처리
                        if (itemStack.hasItemMeta()) {
                            ItemMeta meta = itemStack.getItemMeta();
                            if (meta != null && meta.hasDisplayName() && meta.getDisplayName().equals("Grenade") &&
                                    meta.getPersistentDataContainer().has(new NamespacedKey(plugin, "fuseTicks"), PersistentDataType.INTEGER)) {

                                // 아이템의 데이터를 바탕으로 GrenadeEntity를 생성
                                GrenadeEntity grenadeEntity = new GrenadeEntity(itemStack, null, plugin, itemEntity);
                                grenadeEntity.loadFromItemData();  // 아이템 데이터 로드
                                if (grenadeEntity.fuseTicks > 0) {
                                    grenadeEntity.fuseTicks--;  // 퓨즈 감소
                                    grenadeEntity.saveToItemData();
                                } else if (grenadeEntity.fuseTicks <= 0) {
                                    grenadeEntity.executeWeaponEffect();  // 폭발 처리
                                    itemEntity.remove();  // 수류탄 아이템 제거
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

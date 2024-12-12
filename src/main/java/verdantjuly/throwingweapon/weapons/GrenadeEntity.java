package verdantjuly.throwingweapon.weapons;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.inventory.meta.ItemMeta;
import verdantjuly.throwingweapon.WeaponEntity;

public class GrenadeEntity extends WeaponEntity {

    private static final double EXPLOSION_RADIUS = 6.0;  // 수류탄 폭발 반경
    private static final float EXPLOSION_DAMAGE = 0;
    private static final float DAMAGE_POWER = 10;
    private static final int DEFAULT_FUSE_TICKS = 100;  // 기본 폭발 대기 시간 (5초)
    private Item thrownItemEntity;  // 실제 던져진 아이템 엔티티를 추적

    public GrenadeEntity(ItemStack grenadeItem, Player player, Plugin plugin, Item thrownItemEntity) {
        super(grenadeItem, plugin, DEFAULT_FUSE_TICKS, DAMAGE_POWER);
        this.player = player;
        this.thrownItemEntity = thrownItemEntity; // 던져진 아이템 엔티티 설정
        loadFromItemData();
    }

    @Override
    public void saveToItemData() {
        super.saveToItemData(); // 아이템의 PersistentDataContainer에 데이터를 저장
    }

    @Override
    public void loadFromItemData() {
        super.loadFromItemData(); // 아이템의 PersistentDataContainer에서 데이터를 로드
    }

    // 수류탄 폭발 효과 처리
    @Override
    public void executeWeaponEffect() {
        if (thrownItemEntity == null) return;

        Location grenadeLocation = thrownItemEntity.getLocation();  // 수류탄의 실제 위치

        World world = grenadeLocation.getWorld();
        if (world == null) return;

        // 폭발 효과 (폭발, 불꽃, 연기 등)
        world.createExplosion(grenadeLocation, EXPLOSION_DAMAGE, false, true);  // 실제 폭발 발생
        world.spawnParticle(Particle.EXPLOSION_EMITTER, grenadeLocation, 1);      // 큰 폭발 파티클
        world.spawnParticle(Particle.FLAME, grenadeLocation, 20);              // 불꽃 파티클
        world.spawnParticle(Particle.LARGE_SMOKE, grenadeLocation, 20);        // 연기 파티클

        // 폭발 범위 내의 모든 엔티티에 데미지 적용
        for (Entity entity : world.getNearbyEntities(grenadeLocation, EXPLOSION_RADIUS, EXPLOSION_RADIUS, EXPLOSION_RADIUS)) {
            if (entity instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity) entity;
                // 벽이 없는 경우에만 데미지를 주도록 설정
                Location livingEntityLocation = livingEntity.getLocation();
                if (!livingEntity.isDead() && !this.isWall(grenadeLocation, livingEntityLocation)) {
                    this.applyDamage(livingEntity);  // 폭발 데미지 적용
                    this.decreaseArmorDurability(livingEntity); // 갑옷 내구도 감소 적용
                }
            }
        }

        // 수류탄 아이템 제거
        ItemStack grenadeItem = (ItemStack) this.weaponItem;
        grenadeItem.setAmount(0);  // 수류탄 아이템을 제거

    }

    // 아이템 생성 (수류탄 아이템을 생성)
    public static ItemStack createGrenade() {
        ItemStack grenade = new ItemStack(Material.SNOWBALL, 1);
        ItemMeta meta = grenade.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("Grenade");
            meta.setCustomModelData(1234);  // 커스텀 모델 데이터 설정
            grenade.setItemMeta(meta);  // 아이템에 메타 정보 반영
        }

        return grenade;
    }
}

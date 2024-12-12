package verdantjuly.throwingweapon;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.NamespacedKey;

public abstract class WeaponEntity {

    protected final Plugin plugin;
    protected final ItemStack weaponItem;  // 무기의 아이템
    protected Player player;
    protected int fuseTicks;          // Fuse time for certain weapons (e.g., grenades)
    protected float damagePower;      // 무기의 데미지
    protected static final double RADIUS = 6.0;  // 폭발 범위, 무기에 따라 조정 가능

    public WeaponEntity(ItemStack weaponItem, Plugin plugin, int fuseTicks, float damagePower) {
        this.weaponItem = weaponItem;
        this.plugin = plugin;
        this.fuseTicks = fuseTicks;
        this.damagePower = damagePower;
    }

    // 무기 데이터를 아이템에 저장
    public void saveToItemData() {
        if (weaponItem != null) {
            ItemMeta meta = weaponItem.getItemMeta();  // ItemStack에서 ItemMeta 가져오기
            if (meta != null) {
                PersistentDataContainer container = meta.getPersistentDataContainer();  // PersistentDataContainer 가져오기
                container.set(new NamespacedKey(plugin, "fuseTicks"), PersistentDataType.INTEGER, fuseTicks);
                container.set(new NamespacedKey(plugin, "damagePower"), PersistentDataType.FLOAT, damagePower);
                weaponItem.setItemMeta(meta);  // 업데이트된 ItemMeta를 ItemStack에 다시 설정
            }
        }
    }

    // 무기 데이터를 아이템에서 로드
    public void loadFromItemData() {
        if (weaponItem != null) {
            ItemMeta meta = weaponItem.getItemMeta();  // ItemStack에서 ItemMeta 가져오기
            if (meta != null) {
                PersistentDataContainer container = meta.getPersistentDataContainer();
                if (container.has(new NamespacedKey(plugin, "fuseTicks"), PersistentDataType.INTEGER)) {
                    this.fuseTicks = container.get(new NamespacedKey(plugin, "fuseTicks"), PersistentDataType.INTEGER);
                }
                if (container.has(new NamespacedKey(plugin, "damagePower"), PersistentDataType.FLOAT)) {
                    this.damagePower = container.get(new NamespacedKey(plugin, "damagePower"), PersistentDataType.FLOAT);
                }
                weaponItem.setItemMeta(meta);  // 업데이트된 ItemMeta를 ItemStack에 다시 설정
            }
        }
    }

    // 무기마다 다를 수 있는 폭발, 효과, 데미지 처리 등을 위해 추상 메서드로 정의
    public abstract void executeWeaponEffect();


    // 벽이 있는지 체크하는 메서드
    public boolean isWall(Location loc1, Location loc2) {
        double interval = 0.05; // 거리 간격 (점검의 정확도를 높이기 위해 간격을 설정)
        Vector vector = loc2.toVector().subtract(loc1.toVector()).normalize().multiply(interval); // 두 지점 사이의 벡터
        double distance = loc1.distance(loc2);  // 두 위치 사이의 거리
        Location currentLocation = loc1.clone();  // 시작 위치로부터 탐색

        // 두 지점 사이를 일정 간격으로 따라가며 벽이 있는지 확인
        for (double i = 0; i < distance; i += interval) {
            if (isReachedBlock(currentLocation)) { // 벽이 존재하는지 확인
                return true;
            }
            currentLocation.add(vector); // 현재 위치 업데이트
        }
        return false;
    }

    // 벽이 있는지 확인하는 메서드 (해당 위치에 블록이 존재하는지 체크)
    public static boolean isReachedBlock(Location location) {
        if (isChunkLoaded(location)) {
            Block block = location.getBlock();
            if (block.getType().isAir()) { // 공기 블록은 벽이 아님
                return false;
            }

            BoundingBox boundingBox = block.getBoundingBox(); // 블록의 bounding box 계산
            return location.getX() >= boundingBox.getMinX() && location.getX() <= boundingBox.getMaxX() &&
                    location.getY() >= boundingBox.getMinY() && location.getY() <= boundingBox.getMaxY() &&
                    location.getZ() >= boundingBox.getMinZ() && location.getZ() <= boundingBox.getMaxZ() &&
                    !block.isPassable(); // 블록이 통과 불가능한 경우 벽으로 판단
        }
        return true; // 청크가 로드되지 않았다면 벽이 존재한다고 판단
    }

    // 청크가 로드되었는지 확인
    public static boolean isChunkLoaded(Location location) {
        return location.getWorld().isChunkLoaded(location.getBlockX() >> 4, location.getBlockZ() >> 4);
    }

    public void decreaseArmorDurability(LivingEntity livingEntity) {
        if (livingEntity == null) {
            return;  // player가 null인 경우 메서드를 종료합니다.
        }

        // 장비 확인: null인 경우 예외 처리
        ItemStack helmet = livingEntity.getEquipment() != null ? livingEntity.getEquipment().getHelmet() : null;
        ItemStack chestplate = livingEntity.getEquipment() != null ? livingEntity.getEquipment().getChestplate() : null;
        ItemStack leggings = livingEntity.getEquipment() != null ? livingEntity.getEquipment().getLeggings() : null;
        ItemStack boots = livingEntity.getEquipment() != null ? livingEntity.getEquipment().getBoots() : null;

        // 각 장비에 대해 내구도 감소 처리
        if (helmet != null && helmet.getType().getMaxDurability() > 0) {
            decreaseDurability(helmet);  // 헬멧 내구도 감소
        }
        if (chestplate != null && chestplate.getType().getMaxDurability() > 0) {
            decreaseDurability(chestplate);  // 갑옷 내구도 감소
        }
        if (leggings != null && leggings.getType().getMaxDurability() > 0) {
            decreaseDurability(leggings);  // 바지 내구도 감소
        }
        if (boots != null && boots.getType().getMaxDurability() > 0) {
            decreaseDurability(boots);  // 부츠 내구도 감소
        }
    }

    // 보호구의 내구도를 감소시키는 메서드
    public void decreaseDurability(ItemStack armor) {
        short durability = armor.getDurability();
        if (durability < armor.getType().getMaxDurability()) {
            armor.setDurability((short) (durability + 1));  // 내구도 1 감소
        } else {
            // 내구도가 0이면 보호구가 파괴되는 효과
            armor.setDurability(armor.getType().getMaxDurability());
            armor.setAmount(0);  // 아이템을 파괴
        }
    }

    // 데미지를 적용하는 메서드
    public void applyDamage(LivingEntity entity) {

        // 데미지 적용
        entity.damage(damagePower);

        // 플레이어가 입은 보호구 내구도 감소
        if (entity instanceof Player) {
            Player player = (Player) entity;
            decreaseArmorDurability(player);
        }
    }
}

package verdantjuly.throwingweapon;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import verdantjuly.throwingweapon.weapons.GrenadeEntity;

public class WeaponCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // 명령어를 실행한 것이 플레이어인지 확인
        if (sender instanceof Player) {
            Player player = (Player) sender;

            // 인자 길이 확인 (2개 인자: weapon과 player)
            if (args.length == 2) {
                String weaponType = args[0];  // 첫 번째 인자: weapon 종류 (예: grenade)
                Player targetPlayer = player.getServer().getPlayer(args[1]);  // 두 번째 인자: 대상 플레이어

                // 플레이어가 존재하면
                if (targetPlayer != null) {
                    if (weaponType.equalsIgnoreCase("grenade")) {
                        // grenade를 선택한 경우, 그레네이드 아이템을 대상에게 주기

                        targetPlayer.getInventory().addItem(GrenadeEntity.createGrenade());
                        targetPlayer.sendMessage("수류탄이 지급되었습니다.");
                        player.sendMessage("수류탄 지급됨 :  " + targetPlayer.getName());
                    } else {
                        player.sendMessage("투척 무기 이름을 확인해 주세요.");  // 알려지지 않은 무기 종류 처리
                    }
                } else {
                    player.sendMessage("대상 플레이어가 없습니다.");  // 대상 플레이어가 없으면 메시지 출력
                }
            } else {
                player.sendMessage("Usage: /tw <weapon> <player>");
            }
        } else {
            sender.sendMessage("op를 가진 플레이어만 사용할 수 있습니다.");
        }
        return true;
    }
}

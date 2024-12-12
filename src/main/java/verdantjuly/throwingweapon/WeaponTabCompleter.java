package verdantjuly.throwingweapon;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class WeaponTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> suggestions = new ArrayList<>();

        // 명령어 인자가 1개일 때 (무기 종류 자동완성)
        if (args.length == 1) {
            if (sender.hasPermission("throwingweapon.use")) {
                suggestions.add("Grenade");  // "grenade" 추가 (다른 무기도 추가 가능)

            }
        }
        // 명령어 인자가 2개일 때 (플레이어 이름 자동완성)
        else if (args.length == 2) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                // 서버의 모든 플레이어 이름을 자동완성에 추가
                for (Player targetPlayer : player.getServer().getOnlinePlayers()) {
                    suggestions.add(targetPlayer.getName());
                }
            }
        }
        return suggestions;
    }
}

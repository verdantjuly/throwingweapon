package verdantjuly.throwingweapon;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.plugin.java.JavaPlugin;
import verdantjuly.throwingweapon.weapons.GrenadeEntity;

public class Main extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Register the event listener
        Bukkit.getPluginManager().registerEvents(this, this);

        // 명령어 Executor 등록
        this.getCommand("tw").setExecutor(new WeaponCommandExecutor());

        // TabCompleter 등록
        this.getCommand("tw").setTabCompleter(new WeaponTabCompleter());

        // EntityDamageListener 등록
        getServer().getPluginManager().registerEvents(new EntityDamageListener(), this);

        new WeaponManager(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
        }
        return false;
    }
}

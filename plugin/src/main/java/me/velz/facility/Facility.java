package me.velz.facility;

import java.util.HashMap;
import lombok.Getter;
import me.velz.facility.commands.BanCommand;
import me.velz.facility.commands.BlockPhysicsCommand;
import me.velz.facility.commands.BroadcastCommand;
import me.velz.facility.commands.ClearChatCommand;
import me.velz.facility.commands.ClearinvCommand;
import me.velz.facility.commands.DelHomeCommand;
import me.velz.facility.commands.DelWarpCommand;
import me.velz.facility.commands.EnchantmentTableCommand;
import me.velz.facility.commands.EnderChestCommand;
import me.velz.facility.commands.FacilityCommand;
import me.velz.facility.commands.FeedCommand;
import me.velz.facility.commands.FlyCommand;
import me.velz.facility.commands.FreezeCommand;
import me.velz.facility.commands.GamemodeCommand;
import me.velz.facility.commands.GlobalMuteCommand;
import me.velz.facility.commands.GodmodeCommand;
import me.velz.facility.commands.HatCommand;
import me.velz.facility.commands.HealCommand;
import me.velz.facility.commands.HomeCommand;
import me.velz.facility.commands.HomeListCommand;
import me.velz.facility.commands.JumpCommand;
import me.velz.facility.commands.KickCommand;
import me.velz.facility.commands.KillCommand;
import me.velz.facility.commands.LevelCommand;
import me.velz.facility.commands.MoneyCommand;
import me.velz.facility.commands.MoreCommand;
import me.velz.facility.commands.MotdCommand;
import me.velz.facility.commands.MsgCommand;
import me.velz.facility.commands.MuteCommand;
import me.velz.facility.commands.OpeninvCommand;
import me.velz.facility.commands.PayCommand;
import me.velz.facility.commands.PingCommand;
import me.velz.facility.commands.PlaytimeCommand;
import me.velz.facility.commands.ReplyCommand;
import me.velz.facility.commands.RocketCommand;
import me.velz.facility.commands.SeenCommand;
import me.velz.facility.commands.SetHomeCommand;
import me.velz.facility.commands.SetSpawnCommand;
import me.velz.facility.commands.SetSpawnerCommand;
import me.velz.facility.commands.SetWarpCommand;
import me.velz.facility.commands.SkullCommand;
import me.velz.facility.commands.SocialspyCommand;
import me.velz.facility.commands.SpawnCommand;
import me.velz.facility.commands.SpawnMobCommand;
import me.velz.facility.commands.SpeedCommand;
import me.velz.facility.commands.SudoCommand;
import me.velz.facility.commands.TeamchatCommand;
import me.velz.facility.commands.TempBanCommand;
import me.velz.facility.commands.TempMuteCommand;
import me.velz.facility.commands.TimeCommand;
import me.velz.facility.commands.TokensCommand;
import me.velz.facility.commands.TpCommand;
import me.velz.facility.commands.TpHereCommand;
import me.velz.facility.commands.TpLocCommand;
import me.velz.facility.commands.TpaCommand;
import me.velz.facility.commands.TpacceptCommand;
import me.velz.facility.commands.TpahereCommand;
import me.velz.facility.commands.TpdenyCommand;
import me.velz.facility.commands.UnBanCommand;
import me.velz.facility.commands.UnMuteCommand;
import me.velz.facility.commands.VanishCommand;
import me.velz.facility.commands.WarpCommand;
import me.velz.facility.commands.WarpListCommand;
import me.velz.facility.commands.WeatherCommand;
import me.velz.facility.commands.WorkbenchCommand;
import me.velz.facility.database.DatabasePlayer;
import me.velz.facility.database.DatabaseWarp;
import me.velz.facility.database.MySQLDatabase;
import me.velz.facility.implementations.Implementations;
import me.velz.facility.listeners.AsyncPlayerChatListener;
import me.velz.facility.listeners.AsyncPlayerPreLoginListener;
import me.velz.facility.listeners.BlockPhysicsListener;
import me.velz.facility.listeners.EntityDamageListener;
import me.velz.facility.listeners.PlayerJoinListener;
import me.velz.facility.listeners.PlayerKickListener;
import me.velz.facility.listeners.PlayerLoginListener;
import me.velz.facility.listeners.PlayerMoveListener;
import me.velz.facility.listeners.PlayerQuitListener;
import me.velz.facility.listeners.PlayerRespawnListener;
import me.velz.facility.listeners.ServerListPingListener;
import me.velz.facility.listeners.SignChangeListener;
import me.velz.facility.utils.FileManager;
import me.velz.facility.utils.MessageUtil;
import me.velz.facility.utils.Tools;
import me.velz.facility.version.Version;
import me.velz.facility.version.VersionMatcher;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Facility extends JavaPlugin {

    @Getter
    private static Facility instance;

    @Getter
    private final MySQLDatabase mysqlDatabase = new MySQLDatabase(this);

    @Getter
    private final Tools tools = new Tools();

    @Getter
    private FileManager fileManager;

    @Getter
    private final Implementations implementations = new Implementations();

    @Getter
    private final HashMap<String, DatabasePlayer> players = new HashMap<>();

    @Getter
    private final HashMap<String, DatabaseWarp> warps = new HashMap();
    
    @Getter
    private final VersionMatcher versionMatcher = new VersionMatcher();
    
    @Getter
    private Version version;

    @Override
    public void onEnable() {
        Facility.instance = this;
        version = this.getVersionMatcher().match();
        fileManager = new FileManager();
        MessageUtil.load();
        this.getMysqlDatabase().connect();
        this.getImplementations().hook();
        this.schedul();
        this.loadListener();
        this.loadCommands();
    }

    private void loadListener() {
        Bukkit.getPluginManager().registerEvents(new AsyncPlayerChatListener(this), this);
        Bukkit.getPluginManager().registerEvents(new AsyncPlayerPreLoginListener(this), this);
        Bukkit.getPluginManager().registerEvents(new BlockPhysicsListener(), this);
        Bukkit.getPluginManager().registerEvents(new EntityDamageListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerKickListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerLoginListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerMoveListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerRespawnListener(this), this);
        Bukkit.getPluginManager().registerEvents(new ServerListPingListener(this), this);
        Bukkit.getPluginManager().registerEvents(new SignChangeListener(), this);
    }

    private void loadCommands() {
        getCommand("ban").setExecutor(new BanCommand(this));
        getCommand("blockphysics").setExecutor(new BlockPhysicsCommand());
        getCommand("broadcast").setExecutor(new BroadcastCommand());
        getCommand("clearchat").setExecutor(new ClearChatCommand());
        getCommand("clearinv").setExecutor(new ClearinvCommand());
        getCommand("delhome").setExecutor(new DelHomeCommand(this));
        getCommand("delwarp").setExecutor(new DelWarpCommand(this));
        getCommand("enchantmenttable").setExecutor(new EnchantmentTableCommand());
        getCommand("enderchest").setExecutor(new EnderChestCommand());
        getCommand("facility").setExecutor(new FacilityCommand(this));
        getCommand("feed").setExecutor(new FeedCommand());
        getCommand("fly").setExecutor(new FlyCommand());
        getCommand("freeze").setExecutor(new FreezeCommand());
        getCommand("gamemode").setExecutor(new GamemodeCommand());
        getCommand("globalmute").setExecutor(new GlobalMuteCommand());
        getCommand("godmode").setExecutor(new GodmodeCommand());
        getCommand("hat").setExecutor(new HatCommand());
        getCommand("heal").setExecutor(new HealCommand());
        getCommand("home").setExecutor(new HomeCommand(this));
        getCommand("homelist").setExecutor(new HomeListCommand(this));
        getCommand("jump").setExecutor(new JumpCommand());
        getCommand("kick").setExecutor(new KickCommand());
        getCommand("kill").setExecutor(new KillCommand());
        getCommand("level").setExecutor(new LevelCommand());
        getCommand("money").setExecutor(new MoneyCommand(this));
        getCommand("more").setExecutor(new MoreCommand());
        getCommand("motd").setExecutor(new MotdCommand());
        getCommand("msg").setExecutor(new MsgCommand(this));
        getCommand("mute").setExecutor(new MuteCommand(this));
        getCommand("openinv").setExecutor(new OpeninvCommand());
        getCommand("pay").setExecutor(new PayCommand());
        getCommand("ping").setExecutor(new PingCommand());
        getCommand("playtime").setExecutor(new PlaytimeCommand(this));
        getCommand("reply").setExecutor(new ReplyCommand(this));
        getCommand("rocket").setExecutor(new RocketCommand());
        getCommand("seen").setExecutor(new SeenCommand(this));
        getCommand("sethome").setExecutor(new SetHomeCommand(this));
        getCommand("setspawn").setExecutor(new SetSpawnCommand(this));
        getCommand("setspawner").setExecutor(new SetSpawnerCommand());
        getCommand("setwarp").setExecutor(new SetWarpCommand(this));
        getCommand("skull").setExecutor(new SkullCommand());
        getCommand("socialspy").setExecutor(new SocialspyCommand());
        getCommand("spawn").setExecutor(new SpawnCommand(this));
        getCommand("spawnmob").setExecutor(new SpawnMobCommand());
        getCommand("speed").setExecutor(new SpeedCommand());
        getCommand("sudo").setExecutor(new SudoCommand());
        getCommand("teamchat").setExecutor(new TeamchatCommand(this));
        getCommand("tempban").setExecutor(new TempBanCommand(this));
        getCommand("tempmute").setExecutor(new TempMuteCommand(this));
        getCommand("time").setExecutor(new TimeCommand());
        getCommand("tokens").setExecutor(new TokensCommand(this));
        getCommand("tp").setExecutor(new TpCommand());
        getCommand("tphere").setExecutor(new TpHereCommand());
        getCommand("tploc").setExecutor(new TpLocCommand());
        getCommand("tpa").setExecutor(new TpaCommand());
        getCommand("tpaccept").setExecutor(new TpacceptCommand(this));
        getCommand("tpahere").setExecutor(new TpahereCommand());
        getCommand("tpaccept").setExecutor(new TpacceptCommand(this));
        getCommand("tpdeny").setExecutor(new TpdenyCommand());
        getCommand("unban").setExecutor(new UnBanCommand(this));
        getCommand("unmute").setExecutor(new UnMuteCommand(this));
        getCommand("vanish").setExecutor(new VanishCommand(this));
        getCommand("warp").setExecutor(new WarpCommand(this));
        getCommand("warplist").setExecutor(new WarpListCommand(this));
        getCommand("weather").setExecutor(new WeatherCommand());
        getCommand("workbench").setExecutor(new WorkbenchCommand());
        getCommand("day").setExecutor(new TimeCommand());
        getCommand("night").setExecutor(new TimeCommand());
        getCommand("sun").setExecutor(new WeatherCommand());
        getCommand("rain").setExecutor(new WeatherCommand());
    }

    private void schedul() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            if (!TpaCommand.getTpaStorage().isEmpty()) {
                TpaCommand.getTpaStorage().values().forEach((facilityTpa) -> {
                    if (facilityTpa.getTimer() == 0) {
                        facilityTpa.getPlayer1().sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.TELEPORT_TPA_TIMEOUT_PLAYER.getLocal().replaceAll("%player", facilityTpa.getPlayer2().getName()));
                        facilityTpa.getPlayer2().sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.TELEPORT_TPA_TIMEOUT_TARGET.getLocal().replaceAll("%player", facilityTpa.getPlayer1().getName()));
                        TpaCommand.getTpaStorage().remove(facilityTpa.getPlayer1());
                    } else {
                        facilityTpa.setTimer(facilityTpa.getTimer() - 1);
                    }
                });
            }
            if (!TpaCommand.getTeleportStorage().isEmpty()) {
                TpaCommand.getTeleportStorage().values().forEach((facilityTeleport) -> {
                    if (facilityTeleport.seconds == 0) {
                        facilityTeleport.teleport();
                        TpaCommand.getTeleportStorage().remove(facilityTeleport.player);
                    } else {
                        facilityTeleport.seconds--;
                    }
                });
            }
            this.getPlayers().values().forEach((dbPlayer) -> {
                dbPlayer.setPlaytime(dbPlayer.getPlaytime() + 1);
            });
        }, 20, 20);
    }

}

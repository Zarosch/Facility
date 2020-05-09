package me.velz.facility.utils;

import lombok.Getter;

public enum MessageUtil {

    // Prefix Messages
    PREFIX("§8[§3Facility§8] "),
    // Error Messages
    ERROR_NOPERMISSIONS("§cDazu hast du keine Berechtigung."),
    ERROR_PLAYERONLY("§cDieser Befehl kann nur von einem Spieler genutzt werden."),
    ERROR_PLAYERNOTFOUND("§cDieser Spieler konnte nicht gefunden werden."),
    ERROR_NONUMBER("§cDu musst eine gültige Zahl angeben."),
    ERROR_NOITEMINHAND("§cDu hast kein Item in der Hand."),
    ERROR_NOTENOUGHITEMS("§cDu hast nicht genügend Items."),
    ERROR_DATE("§cDiese Aktion kann nur zu einem bestimmten Datum erfolgen."),
    ERROR_SYNTAX("§cSyntax Fehler, bitte nutze: %command"),
    // Server Messages
    SERVER_JOIN("§e%player hat das Spiel betreten."),
    SERVER_QUIT("§e%player hat das Spiel verlassen."),
    SERVER_FULL("§cDer Server ist derzeit voll.\n§cVersuche es später noch einmal."),
    SERVER_TABLIST_HEADER("§cHeader\n"),
    SERVER_TABLIST_FOOTER("§cFooter\n"),
    // Player Messages
    PLAYER_BOAT("§6Wir wünschen eine schöne Fahrt."),
    PLAYER_HEAL_SELF("§6Du hast dich geheilt."),
    PLAYER_HEAL_OTHER("§6Du hast §e%player §6geheilt."),
    PLAYER_FEED_SELF("§6Du hast deinen Hunger gestillt."),
    PLAYER_FEED_OTHER("§6Du hast §e%players §6Hunger gestillt."),
    PLAYER_OPENINV("§6Du hast das Inventar von §e%player §6geöffnet."),
    PLAYER_CLEARINV_SELF("§6Du hast dein Inventar geleert."),
    PLAYER_CLEARINV_OTHER("§6Du hast das Inventar von §e%player §6geleert."),
    PLAYER_KILL_SELF("§6Du hast dich selbst getötet."),
    PLAYER_KILL_OTHER("§6Du hast §e%player §6getötet."),
    PLAYER_HAT("§6Du hast das Item nun auf deinem Kopf."),
    PLAYER_FLY_SELF_ON("§6Du hast deinen Flugmodus aktiviert."),
    PLAYER_FLY_SELF_OFF("§6Du hast deinen Flugmodus deaktiviert."),
    PLAYER_FLY_OTHER_ON("§6Du hast §e%players §6Flugmodus aktiviert."),
    PLAYER_FLY_OTHER_OFF("§6Du hast §e%players §6Flugmodus deaktiviert."),
    PLAYER_ENDERCHEST_SELF("§6Du hast deine Enderchest geöffnet."),
    PLAYER_ENDERCHEST_OTHER("§6Du hast die Enderchest von §e%player §6geöffnet."),
    PLAYER_WORKBENCH("§6Du hast eine Workbench geöffnet."),
    PLAYER_ENCHANTMENTTABLE("§6Du hast einen Zaubertisch geöffnet."),
    PLAYER_GAMEMODE_SELF_SURVIVAL("§6Du bist nun im §eSurvival§6 Modus."),
    PLAYER_GAMEMODE_SELF_CREATIVE("§6Du bist nun im §eKreativ§6 Modus."),
    PLAYER_GAMEMODE_SELF_SPECTATOR("§6Du bist nun im §eZuschauer§6 Modus."),
    PLAYER_GAMEMODE_SELF_ADVENTURE("§6Du bist nun im §eAdventure§6 Modus."),
    PLAYER_GAMEMODE_OTHER_SURVIVAL("§6Der Spieler §e%player§6 ist nun im §eSurvival§6 Modus."),
    PLAYER_GAMEMODE_OTHER_CREATIVE("§6Der Spieler §e%player§6 ist im §eKreativ§6 Modus."),
    PLAYER_GAMEMODE_OTHER_SPECTATOR("§6Der Spieler §e%player§6 ist im §eZuschauer§6 Modus."),
    PLAYER_GAMEMODE_OTHER_ADVENTURE("§6Der Spieler §e%player§6 ist im §eAdventure§6 Modus."),
    PLAYER_SPEED_SELF_FLY("§6Du hast deine Fluggeschwindigkeit auf §e%speed§6 gesetzt."),
    PLAYER_SPEED_OTHER_FLY("§6Du hast §e%players§6 Fluggeschwindigkeit auf §e%speed§6 gesetzt."),
    PLAYER_SPEED_SELF_WALK("§6Du hast deine Laufgeschwindigkeit auf §e%speed§6 gesetzt."),
    PLAYER_SPEED_OTHER_WALK("§6Du hast §e%players§6 Laufgeschwindigkeit auf §e%speed§6 gesetzt."),
    PLAYER_VANISH_SELF_ON("§6Du hast Vanish aktiviert."),
    PLAYER_VANISH_SELF_OFF("§6Du hast Vanish deaktiviert."),
    PLAYER_VANISH_OTHER_ON("§6Du hast Vanish für §e%player§6 aktiviert."),
    PLAYER_VANISH_OTHER_OFF("§6Du hast Vanish für §e%player§6 deaktiviert."),
    PLAYER_GODMODE_SELF_ON("§6Du hast Godmodus aktiviert."),
    PLAYER_GODMODE_SELF_OFF("§6Du hast Godmodus deaktiviert."),
    PLAYER_GODMODE_OTHER_ON("§6Du hast Godmodus für §e%player§6 aktiviert."),
    PLAYER_GODMODE_OTHER_OFF("§6Du hast Godmodus für §e%player§6 deaktiviert."),
    PLAYER_PING_SELF("§6Du hast einen Ping von §e%pingms§6."),
    PLAYER_PING_OTHER("§6Der Spieler §e%player §6hat einen Ping von §e%pingms§6."),
    PLAYER_FREEZE_ON("§6Du hast §e%player §6eingefroren."),
    PLAYER_FREEZE_OFF("§6Du hast §e%player §6aufgetaut."),
    PLAYER_ROCKET_SELF("§6Du hast dich in die Luft katapultiert."),
    PLAYER_ROCKET_OTHER("§6Du hast §e%player§6 in die Luft katapultiert."),
    PLAYER_LEVEL_GET("§6Der Spieler §e%player §6hat §e%level§6 Level."),
    PLAYER_LEVEL_ADD("§e%player§6 wurden §e%level§6 hinzugefügt."),
    PLAYER_LEVEL_TAKE("§e%player§6 wurden §e%level§6 entnommen."),
    PLAYER_LEVEL_SET("§6Du hast §e%player§6 auf §e%level§6 Level gesetzt."),
    PLAYER_SEEN_HEADER("§6Spieler Informationen von §e%player"),
    PLAYER_SEEN_MESSAGE("§6Spielzeit: §f%playtime\n§6Kontostand: §f%money\n§6Gruppe: §f%group\n§6Erster Server beitritt: §f%firstjoin\n§6Zuletzt gesehen: §f%lastjoin"),
    PLAYER_SUDO_COMMAND("§6Befehl wurde ausgeführt."),
    PLAYER_SUDO_CHAT("§6Chat Nachricht wurde gesendet."),
    TELEPORT_TP_SELF("§6Du hast dich zu §e%player §6teleportiert."),
    TELEPORT_TP_OTHER("§6Du hast §e%player §6zu§e %target §6teleportiert."),
    TELEPORT_TPHERE("§6Du hast §e%player §6zu dir teleportiert."),
    TELEPORT_TPLOC_SELF("§6Du hast dich zu §e%loc §6teleportiert."),
    TELEPORT_TPLOC_OTHER("§6Du hast §e%player §6zu §e%loc §6teleportiert."),
    TELEPORT_TPA_PLAYER("§6Du hast §e%player §6eine Teleportanfrage geschickt."),
    TELEPORT_TPA_ALREADY("§cDiesem Spieler hast du bereits eine Teleportanfrage geschickt."),
    TELEPORT_TPA_NOREQUEST("§cDieser Spieler hat dir keine Teleportanfrage geschickt."),
    TELEPORT_TPA_CANCEL_PLAYER("§cDu hast deine Teleportanfrage zu §e%player §cabgebrochen."),
    TELEPORT_TPA_SAMEPLAYER("§cDu kannst dich nicht zu dir selbst teleportieren."),
    TELEPORT_TPA_CANCEL_TARGET("§e%player §chat seine Teleportanfrage zu dir abgebrochen."),
    TELEPORT_TPA_TIMEOUT_PLAYER("§cDeine Anfrage zu §e%player §cist abgelaufen."),
    TELEPORT_TPA_TIMEOUT_TARGET("§cDie Teleportanfrage von §e%player §cist abgelaufen."),
    TELEPORT_TPA_TARGET("§e%player §6möchte §esich zu dir§6 teleportieren."),
    TELEPORT_TPA_TPAHERETARGET("§e%player §6möchte das §edu dich zu ihm§6 teleportierst."),
    TELEPORT_TPA_ACCEPT("      §a[Annehmen]§r  "),
    TELEPORT_TPA_DENY("§c[Ablehnen]"),
    TELEPORT_TPA_ACCPTED_PLAYER("§e%player §ahat deine Teleportanfrage angenommen."),
    TELEPORT_TPA_ACCPTED_TARGET("§aDu hast die Teleportanfrage von §e%player §aangenommen."),
    TELEPORT_TPA_DENIED_PLAYER("§e%player §chat deine Teleportanfrage abgelehnt."),
    TELEPORT_TPA_DENIED_TARGET("§6Du hast die Teleportanfrage von §e%player §6abgelehnt."),
    TELEPORT_TELEPORTCANCEL("§cDer Teleportationsvorgang wurde abgebrochen."),
    TELEPORT_TELEPORTSTART("§6Du wirst in §e%seconds Sekunden§6 teleportiert, bitte bewege dich nicht."),
    TELEPORT_JUMP_TOOFAR("§cDas ist leider zu weit."),
    WARP_TELEPORT_SELF("§6Du wirst zum Warp §e%warp§6 teleportiert."),
    WARP_TELEPORT_OTHER("§6Du hast §e%player §6zum Warp §e%warp §6teleportiert."),
    WARP_LIST("§6Alle Warp-Punkte: §f%list"),
    WARPLIST_NOINT("§cSyntax Fehler! Bitte nutze /warplist <Seite>"),
    WARPLIST_PAGENOTFOUND("§cSyntax Fehler! Diese Warplist Seite gibt es nicht."),
    WARP_NOWARPS("§cEs wurden noch keine Warps gesetzt."),
    WARP_SET("§6Du hast den Warp §e%warp §6gesetzt."),
    WARP_DEL("§6Du hast den Warp §e%warp §6gelöscht."),
    WARP_FOUND("§cDieser Warp wurde schon gesetzt."),
    WARP_NOTFOUND("§cDieser Warp konnte nicht gefunden werden."),
    HOME_TELEPORT("§6Du wirst zum Home '§e%home§6' teleportiert."),
    HOME_SET("§6Du hast den Home '§e%home§6' gesetzt."),
    HOME_DELETE("§6Du hast den Home '§e%home§6' gelöscht."),
    HOME_LIST("§6Deine Home-Punkte: §f%list"),
    HOME_NOTFOUND("§cDiesen Home hast du noch nicht gesetzt."),
    HOME_NOHOMES("§cDu hast noch keine Homes gesetzt."),
    ITEMS_MORE("§6Dein Item wurde vermehrt."),
    ITEMS_SKULL("§6Du hast den Kopf von §e%skull §6erhalten."),
    SPAWN_TELEPORT_SELF("§6Du wurdest zum Spawn teleportiert."),
    SPAWN_TELEPORT_OTHER("§6Du hast §e%player §6zum Spawn teleportiert."),
    SPAWN_NOTSET("§cDer Spawn wurde noch nicht gesetzt."),
    SPAWN_SET("§6Du hast den Spawn gesetzt."),
    CHAT_BROADCAST("§7┃ §cRundruf §7┃ §c%message"),
    CHAT_TEAMCHAT("§7┃ §cTeam §7┃ %player§f: %message"),
    CHAT_MSG_SELF("§7[ §6Du §f➡ §6%player §7] §f%message"),
    CHAT_MSG_TARGET("§7[ §6%player §f➡ §6Dir §7] §f%message"),
    CHAT_MSG_TARGETSELF("§cDu kannst dir nicht selbst schreiben."),
    CHAT_MSG_NOTFOUND("§cDu hast noch mit niemanden geschrieben."),
    CHAT_CLEAR("§6Der Chat wurde von §e%player §6bereinigt."),
    CHAT_GLOBALMUTE_ON("§6Der Chat wurde von §e%player §6deaktiviert."),
    CHAT_GLOBALMUTE_OFF("§6Der Chat wurde von §e%player §6aktiviert."),
    CHAT_GLOBALMUTE_CANCEL("§cDer Chat wurde deaktiviert, du kannst derzeit leider nichts im Chat schrieben."),
    CHAT_SOCIALSPY_ON("§6Du hast §eSocialspy§6 bis zum nächsten ausloggen §aaktiviert§6."),
    CHAT_SOCIALSPY_OFF("§6Du hast §eSocialspy§6 wieder §cdeaktiviert§6."),
    CHAT_SOCIALSPY_MSG("§7┃ §cSPY §7┃ §6%player §f➡ §6%target§f: %message"),
    CHAT_MOTD("§cWillkommen!\nDieser Server benutzt Facility!"),
    MISC_SPAWNMOB_UNKNOWNMOB("§cDu hast einen ungültigen Entity Type angegeben."),
    MISC_SPAWNMOB_OTHER("§6Du hast §e%amountx %type §6bei §e%player §6erschaffen."),
    MISC_SPAWNMOB_SPAWNED("§6Du hast §e%amountx %type§6 erschaffen."),
    MISC_WEATHER_RAIN("§6Du hast Gott weinen lassen."),
    MISC_WEATHER_SUN("§6Du hast die Sonne wieder rauskommen lassen."),
    MISC_TIME_DAY("§6Du hast die Zeit auf §eTag§6 gesetzt."),
    MISC_TIME_NIGHT("§6Du hast die Zeit auf §eNacht§6 gesetzt."),
    MISC_TIME_NUMBER("§6Du hast die Zeit auf §e%number§6 gesetzt."),
    MISC_BLOCKPHYSICS_ON("§6Du hast Block Physics §eaktiviert§6."),
    MISC_BLOCKPHYSICS_OFF("§6Du hast Block Physics §edeaktiviert§6."),
    MISC_SETSPAWNER_TARGET("§cBitte schaue einen Spawner an."),
    MISC_SETSPAWNER_CHANGED("§6Der Spawner spawnt nun §e%type§6."),
    MISC_SETSPAWNER_TYPE("§cBitte verwende einen gültigen EntityType."),
    MONEY_PREFIX("§7┃ §3Münzen §7┃ "),
    MONEY_BALANCE_SELF("§6Du trägst §e%money %moneyname§6 bei dir."),
    MONEY_BALANCE_OTHER("§6Der Spieler §e%player §6trägt §e%money %moneyname§6 bei sich."),
    MONEY_NOTENOUGH("§cDu hast nicht genügend Geld."),
    MONEYNAME("Münzen"),
    MONEY_PAY_PLAYER("§6Du hast §e%target§6 %money %moneyname gegeben."),
    MONEY_PAY_TARGET("§e%player§6 hat dir §e%money %moneyname§6 gegeben."),
    MONEY_PAY_SELF("§cDu kannst dir selber kein Geld geben."),
    MONEY_BALANCETOP_HEADER("§6Die Reichsten Spieler:"),
    MONEY_BALANCETOP_ENTRY("§a#%place  §7-  §6%player  §7-  §b%money"),
    MONEY_HELP_CHECK("§6/money <Spieler> §f➡ Spieler Kontostand anschauen"),
    MONEY_HELP_PAY("§6/money pay <Spieler> <Betrag> §f➡ Betrag überweisen"),
    MONEY_HELP_SET("§6/money set <Spieler> <Betrag> §f➡ Kontostand setzen"),
    MONEY_HELP_GIVE("§6/money give <Spieler> <Betrag> §f➡ Betrag überweisen"),
    MONEY_HELP_TAKE("§6/money take <Spieler> <Betrag> §f➡ Betrag abbuchen"),
    MONEY_HELP_GIVEALL("§6/money giveall <Betrag> §f➡ Jedem einen Betrag überweisen"),
    MONEY_HELP_TAKEALL("§6/money takeall <Betrag> §f➡ Jedem einen Betrag abbuchen"),
    MONEY_HELP_TOP("§6/money top §f➡ Topliste anzeigen"),
    MONEY_SET("§e%players §6Kontostand wurde auf §a%money %moneyname §6gesetzt."),
    MONEY_GIVE("§e%player §6wurden §e%money %moneyname§6 gutgeschrieben."),
    MONEY_GIVEINFO("§aDu hast §e%money %moneyname §aerhalten."),
    MONEY_TAKEINFO("§cDir wurden §e%money %moneyname §cabgezogen."),
    MONEY_GIVEALL("§6Du hast jeden §f%money %moneyname §agegeben."),
    MONEY_TAKEALL("§6Du hast jeden §f%money %moneyname §cabgezogen."),
    MONEY_TAKE("§e%player §6wurden §e%money %moneyname§6 abgezogen."),
    TOKENS_BALANCE_SELF("§6Du trägst §c%money %moneyname§6 bei dir."),
    TOKENS_BALANCE_OTHER("§6Der Spieler §e%player §6trägt §c%money %moneyname§6 bei sich."),
    TOKENS_NOTENOUGH("§cDu hast nicht genügend Geld."),
    TOKENSNAME("Tokens"),
    TOKENS_PAY_PLAYER("§6Du hast §e%target§6 %money %moneyname gegeben."),
    TOKENS_PAY_TARGET("§e%player§6 hat dir §e%money %moneyname§6 gegeben."),
    TOKENS_PAY_SELF("§cDu kannst dir selber kein Geld geben."),
    TOKENS_BALANCETOP_HEADER("§6Die Reichsten Spieler:"),
    TOKENS_BALANCETOP_ENTRY("§a#%place  §7-  §6%player  §7-  §b%money"),
    TOKENS_HELP_CHECK("§6/tokens <Spieler> §f➡ Spieler Kontostand anschauen"),
    TOKENS_HELP_PAY("§6/tokens pay <Spieler> <Betrag> §f➡ Betrag überweisen"),
    TOKENS_HELP_SET("§6/tokens set <Spieler> <Betrag> §f➡ Kontostand setzen"),
    TOKENS_HELP_GIVE("§6/tokens give <Spieler> <Betrag> §f➡ Betrag überweisen"),
    TOKENS_HELP_TAKE("§6/tokens take <Spieler> <Betrag> §f➡ Betrag abbuchen"),
    TOKENS_HELP_GIVEALL("§6/tokens giveall <Betrag> §f➡ Jedem einen Betrag überweisen"),
    TOKENS_HELP_TAKEALL("§6/tokens takeall <Betrag> §f➡ Jedem einen Betrag abbuchen"),
    TOKENS_HELP_TOP("§6/tokens top §f➡ Topliste anzeigen"),
    TOKENS_SET("§e%players §6Kontostand wurde auf §a%money %moneyname §6gesetzt."),
    TOKENS_GIVE("§e%player §6wurden §e%money %moneyname§6 gutgeschrieben."),
    TOKENS_GIVEINFO("§aDu hast §e%money %moneyname §aerhalten."),
    TOKENS_TAKEINFO("§cDir wurden §e%money %moneyname §cabgezogen."),
    TOKENS_GIVEALL("§6Du hast jeden §f%money %moneyname §agegeben."),
    TOKENS_TAKEALL("§6Du hast jeden §f%money %moneyname §cabgezogen."),
    TOKENS_TAKE("§e%player §6wurden §e%money %moneyname§6 abgezogen."),
    PLAYTIME_SELF("§6Du hast eine Spielzeit von §e%playtime§6."),
    PLAYTIME_OTHER("§e%player §6hat eine Spielzeit von §e%playtime§6."),
    PLAYTIME_HOUR("Stunde"),
    PLAYTIME_HOURS("Stunden"),
    PLAYTIME_MINUTE("Minute"),
    PLAYTIME_MINUTES("Minuten"),
    PLAYTIME_H("h"),
    PLAYTIME_M("m"),
    PUNISH_BYPASSPUNISH("§cDieser Spieler darf nicht bestraft werden."),
    PUNISH_KICKSCREEN("§cDu wurdest vom Server gekickt.\n §cGrund: §e%reason"),
    PUNISH_KICKED("§e%name §7wurde für §c%reason §7gekickt."),
    PUNISH_KICKEDHOVER("§cPunisher: §e%punisher"),
    PUNISH_KICK("§aDer Spieler wurde gekickt."),
    PUNISH_ALREADYBANNED("§cDieser Spieler ist bereits gebannt."),
    PUNISH_ALREADYMUTED("§cDieser Spieler ist bereits gemutet."),
    PUNISH_BANNEDBROADCAST("§e%name §7wurde für §c%reason §7gebannt."),
    PUNISH_MUTEDBROADCAST("§e%name §7wurde für §c%reason §7gemutet."),
    PUNISH_BAN("§aDer Spieler wurde gebannt."),
    PUNISH_BANNEDSCREEN("§cDein Account wurde vom Server ausgeschlossen.\n §cDauer: §e%time \n §cGrund: §e%reason"),
    PUNISH_BANNEDHOVER("§cPunisher: §e%punisher\n §cDauer: §b%time"),
    PUNISH_MUTEDHOVER("§cPunisher: §e%punisher\n §cDauer: §b%time"),
    PUNISH_UNBAN("§aDer Spieler wurde entbannt."),
    PUNISH_UNMUTE("§aDer Spieler wurde entmutet."),
    PUNISH_UNBANNED("§e%name §7wurde entbannt."),
    PUNISH_UNMUTED("§e%name §7wurde entmutet."),
    PUNISH_UNBANNEDHOVER("§cPunisher: §e%punisher"),
    PUNISH_UNMUTEDHOVER("§cPunisher: §e%punisher"),
    PUNISH_NOTBANNED("§cDieser Spieler ist nicht gebannt."),
    PUNISH_NOTMUTED("§cDieser Spieler ist nicht gemutet."),
    PUNISH_MUTED_MESSAGE("§r\n§cDu wurdest vom Chat ausgeschlossen.\n§cVerbleibende Zeit:§e %time\n§cGrund:§e %reason\n§r"),
    KIT_GIVEKIT("§6Du hast dir das Kit §e%kit §6gegeben."),
    KIT_NOTFOUND("§cDieses Kit konnte nicht gefunden werden."),
    KIT_LIST("§6Alle verfügbaren Kits: §f%kits"),
    KIT_NOKITS("§cEs wurden noch keine Kits erstellt."),
    KIT_CREATE("§6Du hast das Kit §e%kit §6erstellt."),
    KIT_DELETE("§6Du hast das Kit §e%kit §6gelöscht."),
    KIT_NOTREADY("§cDu kannst das Kit noch nicht erneut nutzen."),
    KIT_ALREADYEXIST("§cEs gibt bereits ein Kit mit diesem namen."),
    TRASH_TITLE("Müll"),
    TRASH_MESSAGE("§6Viel Spaß beim Müll entsorgen."),
    FACILITY_HELP_RELOAD("§6/facility reload §f➡ Alle Konfigurationen neuladen"),
    FACILITY_RELOAD("§6Alle Konfigurationen wurden neugeladen."),
    
    // Arena
    ARENA_COMMAND_HELP_JOIN("§6/a join <Arena> §f➡ Arena beitreten"),
    ARENA_COMMAND_HELP_LEAVE("§6/a leave §f➡ Arena verlassen"),
    ARENA_COMMAND_HELP_KICK("§6/a kick <Player> §f➡ Spieler aus Arena kicken"),
    ARENA_COMMAND_HELP_ADD("§6/a add <Arena> §f➡ Arena hinzufügen"),
    ARENA_COMMAND_HELP_REMOVE("§6/a remove <Arena> §f➡ Arena löschen"),
    ARENA_COMMAND_HELP_LIST("§6/a list <Arena> §f➡ Arenen auflisten"),
    ARENA_COMMAND_HELP_ADDMODE("§6/a addmode <Mode> §f➡ Spielmodus hinzufügen"),
    ARENA_COMMAND_HELP_REMOVEMODE("§6/a remmode <Mode> §f➡ Spielmodus löschen"),
    ARENA_COMMAND_HELP_LISTMODES("§6/a listmodes §f➡ Spielmodis auflisten"),
    ARENA_COMMAND_HELP_SETMODE("§6/a setmode <Arena> §f➡ Spielmodus setzen"),
    ARENA_COMMAND_HELP_ADDLOC("§6/a addloc <Arena> <Location> §f➡ Location setzen"),
    ARENA_COMMAND_HELP_REMLOC("§6/a remloc <Arena> <Location> §f➡ Location entfernen"),
    ARENA_COMMAND_HELP_LISTLOCS("§/a listlocs <Arena> §f➡ Locations auflisten"),
    ARENA_COMMAND_HELP_ADDSETTINGS("§6/a addsetting <arena/mode> <target> <setting> §f➡ Settings setzen"),
    ARENA_COMMAND_HELP_RMSETTINGS("§6/a remsetting <arena/mode> <target> <setting> §f➡ Settings entfernen"),
    ARENA_COMMAND_HELP_LISTSETTINGS("§6/a listsetting <arena/mode> <target> §f➡ Settings auflisten"),
    ARENA_COMMAND_KICK("§6Du hast §a%player §6aus der Arena §e%arena §6gekickt."),
    ARENA_COMMAND_ADD("§6Die Arena §e%arena §6wurde erstellt."),
    ARENA_COMMAND_REMOVE("§6Die Arena §e%arena §6wurde gelöscht."),
    ARENA_COMMAND_SETMODE("§6Die Arena §e%arena §6ist nun dem Spielmodus §e%mode §6zugeordnet."),
    ARENA_COMMAND_ADDMODE("§6Der Spielmodus §a%mode §6wurde hinzugefügt."),
    ARENA_COMMAND_REMOVEMODE("§6Der Spielmodus §a%mode §6wurde gelöscht."),
    ARENA_COMMAND_ADDLOC("§6Die Location §a%loc §6in Arena §a%arena §6wurde gesetzt."),
    ARENA_COMMAND_REMOVELOC("§6Die Location §a%loc §6in Arena §a%arena §6wurde gelöscht."),
    ARENA_COMMAND_LIST("§6Arenen: §a%arenas"),
    ARENA_COMMAND_LISTMODES("§6Spielmodis: §a%modes"),
    ARENA_COMMAND_LISTSETTINGS("§6Settings: §a%settings"),
    ARENA_COMMAND_LISTLOCS("§6Locations: §a%locs"),
    ARENA_COMMAND_ADDSETTING_ALREADY("Diese Setting ist bereits gesetzt."),
    ARENA_COMMAND_ADDSETTING_SET("§6Die Setting §e%setting §6wurde zu §e%target §6hinzugefügt."),
    ARENA_COMMAND_REMSETTING_NOTSET("§cIn §e%target §ckonnte die Setting §e%setting §cnicht gefunden werden."),
    ARENA_COMMAND_REMSETTING_REMOVED("§6Die Setting §e%setting §6wurde von §e%target §6entfernt."),
    
    ARENA_NOT_FOUND("§cDiese Arena konnte nicht gefunden werden."),
    ARENA_MODE_NOT_FOUND("§cDieser Modus konnte nicht gefunden werden"),
    ARENA_EXISTS("§cDiese Arena gibt es bereits."),
    ARENA_MODE_EXISTS("§6Diesen Spielmodus gibt es bereits."),
    ARENA_JOIN_JOINED("§6Du bist der Arena §a%arena §6beigetreten."),
    ARENA_LEAVE("§6Du hast die Arena §a%arena §6verlassen."),
    ARENA_KICK("§cDu wurdest aus der Arena §a%arena §cgekickt."),
    ARENA_JOIN_FAILED("§cDiese Arena kann derzeit nicht betreten werden."),
    ARENA_NOTIN_SELF("§cDu musst dafür in einer Arena sein."),
    ARENA_NOTIN_OTHER("§cDieser Spieler ist in keiner Arena."),
    ARENA_LOC_NOT_FOUND("§cDiese Location konnte nicht gefunden werden."),
    
    
    //Functions
    FUNC_PUNISHMENTS_NOTFOUND("§cDiese Bestrafung konnte nicht gefunden werden."),
    FUNC_PUNISHMENTS_EXECUTED("§aBestrafung wurde ausgeführt."),
    
    FUNC_INVENTORY_HELP_OPEN("§6/inventory open <Inventory> [Player] §f➡ Inventar öffnen"),
    FUNC_INVENTORY_HELP_ADD("§6/inventory additems <Inventory> §f➡ Items hinzufügen"),
    FUNC_INVENTORY_HELP_MOVE("§6/inventory moveitems <Inventory> §f➡ Items verschieben"),
    FUNC_INVENTORY_HELP_LIST("§6/inventory list §f➡ Alle Inventare anzeigen lassen"),
    
    FUNC_INVENTORY_ITEMSMOVE("§aEinfach die Items verschieben um die Position zu ändern."),
    FUNC_INVENTORY_ITEMSADD("§aEinfach die neuen Items ins Inventar packen."),
    FUNC_INVENTORY_ITEMSMOVED("§aDie Positionen der Items wurden gespeichert."),
    FUNC_INVENTORY_SLOTNOTFOUND("§cEs konnte kein freie Slot mehr gefunden werden."),
    FUNC_INVENTORY_ITEMSADDED("§aDie Items wurden dem Inventar hinzugefügt."),
    FUNC_INVENTORY_NOINVENTORIES("§cEs konnten keine Inventare gefunden werden."),
    FUNC_INVENTORY_LIST("§6Alle Inventare: §f%invs"),
    FUNC_INVENTORY_NOTFOUND("§cDieses Inventar konnte nicht gefunden werden."),
    FUNC_INVENTORY_OPEN("§aInventar wurde geöffnet.");

    @Getter
    private String local, english, german;

    private MessageUtil(String local) {
        this.local = local;
    }

    public static void load() {
        FileBuilder message = new FileBuilder("plugins/Facility", "messages.yml");
        for (MessageUtil m : MessageUtil.values()) {
            message.addDefault("message." + m.toString().replaceAll("_", ".").toLowerCase(), m.local.replaceAll("§", "&"));
        }
        message.save();
        for (MessageUtil m : MessageUtil.values()) {
            m.local = message.getConfiguration().getString("message." + m.toString().replaceAll("_", ".").toLowerCase()).replaceAll("&", "§");
        }
    }

    public static void save(MessageUtil m) {
        FileBuilder message = new FileBuilder("plugins/Facility", "messages.yml");
        message.save();
    }

}

package me.velz.facility.functions;

import java.util.ArrayList;
import me.velz.facility.Facility;

public class FunctionManager {

    private final Facility plugin;

    public FunctionManager(Facility plugin) {
        this.plugin = plugin;
    }

    public final ArrayList<Function> functions = new ArrayList<>();

    public void load() {
        functions.add(new BroadcastFunction());
        functions.add(new ArmorstandFunction());
        functions.add(new CommandFunction());
        functions.add(new InventoryFunction());

        functions.forEach((function) -> {
            function.onEnable();
        });
    }

    public void reload() {
        functions.forEach((function) -> {
            function.onReload();
        });
    }

    public void schedule() {
        functions.forEach((function) -> {
            function.onSchedule();
        });
    }

}

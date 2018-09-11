package me.velz.facility.objects;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

public class FacilityTpa {

    @Getter
    private Player player1 = null, player2 = null;
    
    @Getter
    private final String type;
    
    @Getter @Setter
    private Integer timer;
    
    public FacilityTpa(Player player1, Player player2, String type, Integer timer) {
        this.player1 = player1;
        this.player2 = player2;
        this.type = type;
        this.timer = timer;
    }
    
    
    
}

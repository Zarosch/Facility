package me.velz.facility.objects;

import lombok.Getter;

public class FacilityCurrency {
    
    @Getter
    private final String prefix, displayName, internalName;
    
    @Getter
    private final Double startBalance;
    
    @Getter
    private final boolean vault;

    public FacilityCurrency(String prefix, String displayName, String internalName, Double startBalance, boolean vault) {
        this.prefix = prefix;
        this.displayName = displayName;
        this.internalName = internalName;
        this.startBalance = startBalance;
        this.vault = vault;
    }
    
}

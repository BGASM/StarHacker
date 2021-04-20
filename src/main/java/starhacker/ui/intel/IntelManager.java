package starhacker.ui.intel;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.comm.IntelInfoPlugin;
import com.fs.starfarer.api.campaign.comm.IntelManagerAPI;
import com.fs.starfarer.api.util.Pair;
import org.apache.log4j.Logger;
import starhacker.impl.campaign.SH_CommRelayEntityPlugin;

import java.util.LinkedList;

public class IntelManager {
    public static Logger log = Global.getLogger(IntelManager.class);


    private IntelManagerAPI intelManager;

    public IntelManager() {
        this.intelManager = Global.getSector().getIntelManager();
    }

    public void add(IntelInfoPlugin intel) {
        intelManager.addIntel(intel);
    }

    public IntelInfoPlugin get(Class<?> className) {
        return intelManager.getFirstIntel(className);
    }

    public void remove(IntelInfoPlugin intel) {
        intelManager.removeIntel(intel);
    }

    public void remove(Class<?> className) {
        IntelInfoPlugin intel = intelManager.getFirstIntel(className);
        while (intel != null) {
            remove(intel);
            intel = intelManager.getFirstIntel(className);
        }
    }

}

package starhacker.helper;

import com.fs.starfarer.api.campaign.comm.IntelInfoPlugin;
import com.fs.starfarer.api.impl.campaign.intel.BaseIntelPlugin;

public class IntelHelper {

    public static void addIntel(BaseIntelPlugin plugin) {
        GlobalHelper.getIntelManager().addIntel(plugin);
    }

    public static IntelInfoPlugin getFirstIntel(Class<?> className) {
        return GlobalHelper.getIntelManager().getFirstIntel(className);
    }

    public static void removeIntel(IntelInfoPlugin plugin) {
        GlobalHelper.getIntelManager().removeIntel(plugin);
    }
}

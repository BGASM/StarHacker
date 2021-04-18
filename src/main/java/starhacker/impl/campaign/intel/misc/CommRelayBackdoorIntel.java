package starhacker.impl.campaign.intel.misc;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.comm.IntelInfoPlugin;
import org.apache.log4j.Logger;


public class CommRelayBackdoorIntel extends BackdoorIntel{
    public static Logger log = Global.getLogger(CommRelayBackdoorIntel.class);
    public CommRelayBackdoorIntel(SectorEntityToken relay) {
        super(relay);
    }

    public static Boolean checkIntel(IntelInfoPlugin intel){
        return true;
    }
}

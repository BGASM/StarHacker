package starhacker.plugins;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.comm.IntelInfoPlugin;
import com.fs.starfarer.api.impl.campaign.CommRelayEntityPlugin;

public interface BackdoorPlugin {

        boolean hasBackdoor();
        void setBackdoor(boolean backdoor, float days);
        void setBackdoor(boolean backdoor);
        boolean isConnected();
        void checkBackdoor();
        void vanillaBonus();
        void checkIntelFromBackdoor();
    }


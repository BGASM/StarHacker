package starhacker.ui.extractor;

import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.comm.IntelInfoPlugin;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.util.Pair;
import starhacker.ui.CollectionsHelper;
import starhacker.ui.filter.intel.IntelMatchQueryFilter;


import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public abstract class IntelFactory {

    protected String dataId;
    private String postingLocation;
    private String postingFaction;
    private int reward;
    private SectorEntityToken target;
    private LinkedHashSet<Pair<SectorEntityToken, IntelInfoPlugin>> intelMap;

    public IntelFactory(String dataId, LinkedHashSet<Pair<SectorEntityToken, IntelInfoPlugin>> intelMap) {
        this.dataId = dataId;
        this.intelMap = intelMap;
    }

    public List<IntelInfoPlugin> getIntel() {
        List<IntelInfoPlugin> intel = unpack();
        return CollectionsHelper.reduceList(intel, new IntelMatchQueryFilter(dataId));
    }

    public List<IntelInfoPlugin> unpack(){
        List<IntelInfoPlugin> intel = new ArrayList<>();
        for (Pair<SectorEntityToken, IntelInfoPlugin> s : intelMap){
            intel.add(s.two);
        }
        return intel;
    }


}

package starhacker.ui.extractor;

import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.comm.IntelInfoPlugin;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.intel.AnalyzeEntityMissionIntel;
import com.fs.starfarer.api.impl.campaign.intel.BaseIntelPlugin;
import com.fs.starfarer.api.util.Pair;
import starhacker.ui.CollectionsHelper;
import starhacker.ui.filter.intel.CommodityAvailableFilter;


import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;

public class CommIntelFactory extends IntelFactory {

    public CommIntelFactory(String dataId, LinkedHashSet<Pair<SectorEntityToken, IntelInfoPlugin>> intel) {
        super(dataId, intel);

    }
}

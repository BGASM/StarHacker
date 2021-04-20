package starhacker.ui.extractor;

import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.comm.IntelInfoPlugin;
import com.fs.starfarer.api.campaign.econ.CommodityOnMarketAPI;
import com.fs.starfarer.api.campaign.econ.EconomyAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.intel.BaseIntelPlugin;
import com.fs.starfarer.api.util.Pair;

import java.util.LinkedHashSet;
import java.util.List;

public class CommTableContent extends IntelTableContent {

    public CommTableContent(String commodityId, List<IntelInfoPlugin> intel, LinkedHashSet<Pair<SectorEntityToken, IntelInfoPlugin>> intelMap) {
        super(commodityId, intel, intelMap);
    }

    @Override
    public Object[] getHeaders(float width) {
        return getHeader(width);
    }
}

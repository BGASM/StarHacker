package starhacker.ui.filter.intel;

import com.fs.starfarer.api.campaign.comm.IntelInfoPlugin;
import com.fs.starfarer.api.campaign.econ.CommodityOnMarketAPI;
import com.fs.starfarer.api.campaign.econ.EconomyAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import starhacker.ui.extractor.TableCellHelper;


@Deprecated public class CommodityDemandFilter implements IntelFilter {
    @Override
    public boolean accept(IntelInfoPlugin object) {
        return false;
    }
/*
    private String commodityId;
    private TableCellHelper helper;

    public CommodityDemandFilter(String commodityId, EconomyAPI economy) {
        this.commodityId = commodityId;
        this.helper = new TableCellHelper();
    }

    @Override
    public boolean accept(MarketAPI market) {
        CommodityOnMarketAPI commodity = market.getCommodityData(commodityId);
        int demand = helper.getDemand(market, commodity);
        if (demand <= 0) {
            return false;
        }
        return true;
    }*/
}

package starhacker.ui.extractor;

import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.comm.IntelInfoPlugin;
import com.fs.starfarer.api.campaign.econ.CommodityOnMarketAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.intel.BaseIntelPlugin;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.util.Pair;
import starhacker.ui.ui.TableContent;


import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public abstract class IntelTableContent implements TableContent {

    protected String commodityId;
    protected TableCellHelper helper;
    List<IntelInfoPlugin> intel;
    private LinkedHashSet<Pair<SectorEntityToken, IntelInfoPlugin>> intelQueue;


    protected IntelTableContent(String commodityId, List<IntelInfoPlugin> intel, LinkedHashSet<Pair<SectorEntityToken, IntelInfoPlugin>> intelQueue) {
        this.commodityId = commodityId;
        this.helper = new TableCellHelper();
        this.intel = intel;
        this.intelQueue = intelQueue;
    }

    @Override
    public Object[] getHeaders(float width) {
        return getHeader(width);
    }

    @Override
    public List<Object[]> getRows() {
        List<Object[]> content = new ArrayList<>();
        int i = 1;
        for (IntelInfoPlugin intel : this.intel) {
            Object[] row = getRow(i++, (BaseIntelPlugin) intel);
            content.add(row);
        }
        return content;
    }

    @Override
    public int getSize() {
        return intel.size();
    }

    protected Object[] getHeader(float width) {
        Object header[] = {
                "#", .05f * width,
                "Location", .1f * width,
                "Star system", .2f * width,
                "Intel", .4f * width,
                "Dist (ly)", .1f * width };
        return header;
    }

    protected Object[] getRow(int i, BaseIntelPlugin intel) {
        Object[] row = new Object[15];
        // Position
        row[0] = Alignment.MID;
        row[1] = Misc.getGrayColor();
        row[2] = String.valueOf(i) + ".";
        // Location
        row[3] = Alignment.LMID;
        row[4] = intel.getPostingLocation().getFaction().getColor();
        row[5] = intel.getPostingLocation().getName();
        // Star system
        row[6] = Alignment.MID;
        row[7] = intel.getPostingLocation().getFaction().getColor();;
        row[8] = intel.getPostingLocation().getStarSystem().getBaseName();
        //Post
        row[9] = Alignment.MID;
        row[10] = intel.getPostingLocation().getFaction().getColor();;
        row[11] = intel.getSmallDescriptionTitle();
        // Distance
        row[12] = Alignment.MID;
        row[13] = Misc.getHighlightColor();
        row[14] = helper.getDistance(intel);
        return row;
    }
}

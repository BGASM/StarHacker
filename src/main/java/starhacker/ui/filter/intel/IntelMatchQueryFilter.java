package starhacker.ui.filter.intel;

import com.fs.starfarer.api.campaign.comm.IntelInfoPlugin;

public class IntelMatchQueryFilter implements IntelFilter {
    private String sortId;

    public IntelMatchQueryFilter(String sortId){this.sortId = sortId;}

    @Override
    public boolean accept(IntelInfoPlugin intel) {
        return intel.getSortString().equals(sortId);
    }
}

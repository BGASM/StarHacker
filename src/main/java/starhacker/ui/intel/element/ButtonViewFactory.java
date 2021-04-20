package starhacker.ui.intel.element;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.econ.CommoditySpecAPI;
import starhacker.ui.ui.Button;
import starhacker.ui.ui.IntelConstants;
import starhacker.ui.ui.Renderable;
import starhacker.ui.ui.Stack;


import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class ButtonViewFactory {


    public ButtonViewFactory(){
    };

    public Stack get(String activeId, IntelConstants.Source activeTab) {
        List<Renderable> buttons = new LinkedList<>();
        List<String> intelIds = IntelConstants.getIntelId(activeTab.toString(), "TIER1");
        sortIntel(intelIds);
        for (String intelId : intelIds) {
            buttons.add(get(intelId, activeId));
        }
        return new Stack(buttons);
    }


    private Button get(String intelId, String activeId) {
        boolean isOn = intelId.equals(activeId);
        Button button = new DataButton(intelId, isOn);
        return button;
    }

    private void sortIntel(List<String> commodityIds) {
        Collections.sort(commodityIds, new Comparator<String>() {

            @Override
            public int compare(String stringA, String stringB) {
                return stringA.compareToIgnoreCase(stringB);
            }
        });
    }
}

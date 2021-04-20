package starhacker.ui.intel.element;

import com.fs.starfarer.api.campaign.comm.IntelInfoPlugin;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.ui.CutStyle;
import com.fs.starfarer.api.util.Misc;
import starhacker.ui.KeyboardHelper;
import starhacker.ui.intel.IntelTracker;
import starhacker.ui.intel.StarHackerBoard;
import starhacker.ui.intel.StarHackerBoard.DataTab;
import starhacker.ui.ui.Button;
import starhacker.ui.ui.Callable;
import starhacker.ui.ui.Size;
import starhacker.ui.ui.ToggleButton;


import java.awt.event.KeyEvent;

public class IntelButton extends Button {

    public IntelButton(int i, final DataTab dataTab, final String dataId, final IntelInfoPlugin intel) {
        super(new Size(28f, 24f), String.valueOf(i), true, Misc.getTextColor());
        setCallback(new Callable() {

            @Override
            public void callback() {
                //intel.setForceAddNextFrame(true);
                KeyboardHelper.send(KeyEvent.VK_E);
            }
        });
        setCutStyle(CutStyle.TL_BR);
    }
}

package starhacker.ui.intel.element;

import com.fs.starfarer.api.ui.CutStyle;
import com.fs.starfarer.api.util.Misc;
import starhacker.ui.intel.StarHackerBoard.DataTab;
import starhacker.ui.intel.StarHackerBoard;
import starhacker.ui.ui.Button;
import starhacker.ui.ui.Callable;
import starhacker.ui.ui.Size;


public class TabButton extends Button {

    public TabButton(final DataTab currentTab, DataTab activeTab, int shortcut) {
        super(new Size(200, 22), currentTab.title, true, Misc.getGrayColor());
        if (currentTab.equals(activeTab)) {
            setColor(Misc.getButtonTextColor());
        }
        setCallback(new Callable() {

            @Override
            public void callback() {
                StarHackerBoard board = StarHackerBoard.getInstance();
                board.setActiveTab(currentTab);
            }
        });
        setCutStyle(CutStyle.TOP);
        setShortcut(shortcut);
    }
}

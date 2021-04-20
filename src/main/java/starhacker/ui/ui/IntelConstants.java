package starhacker.ui.ui;

import com.fs.starfarer.api.Global;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import starhacker.StarHackerConstants;
import starhacker.StarHackerPlugin;

import java.io.IOException;
import java.util.*;
import java.util.List;

public class IntelConstants {
    public enum Source {COMM, SENSOR, NAV;}
    public enum Tier {TIER0, TIER1, TIER2, TIER3;}
    protected static EnumMap<Source, EnumMap<Tier, List<String>>> intel = new EnumMap<Source,
            EnumMap<Tier, List<String>>>(Source.class);

    static {
        try {
            loadSettings();
        } catch (IOException | JSONException | NullPointerException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static List<String> getIntelId(String comm, String tier1) {
        return intel.get(Source.valueOf(comm)).get(Tier.valueOf(tier1));
    }

    protected static void loadSettings() throws IOException, JSONException {
        JSONObject baseJson = Global.getSettings().getMergedJSONForMod(StarHackerConstants.INTEL_FILE,
                StarHackerConstants.MOD_ID);
        StarHackerPlugin.log.info(baseJson);

        // load intel
        JSONObject intelJson = baseJson.getJSONObject("intel");
        Iterator<String> source = intelJson.keys();
        while (source.hasNext()) {
            String src = source.next();
            intel.put(Source.valueOf(src), new EnumMap<Tier, List<String>>(Tier.class));
            JSONObject srcJson = intelJson.getJSONObject(src);
            Iterator<String> tiers = srcJson.keys();
            while (tiers.hasNext()) {
                String tier = tiers.next();
                JSONArray jsonArray = srcJson.getJSONArray(tier);
                List<String> strings = new ArrayList<String>();
                for (int i=0; i<jsonArray.length(); i++) {
                    strings.add(jsonArray.getString(i));
                }
                intel.get(Source.valueOf(src)).put(Tier.valueOf(tier), strings);
            }
        }
    }

/*
    private final List<String> intelID = Arrays.asList(
            "Hostilities",
            "Analyze",
            "Commission",
            "Personal Bounty",
            "Procurement",
            "Survey",
            "System Bounty",
            "Distress Call",
            "Delivery",
            "Red Planet",
            "Technology Cache",
            "Loan",
            "Weapon Blueprint",
            "Ship Blueprint",
            "AAA",
            "Weapon Blueprint",
            "Decivilized ",
            "Location",
            "Stabilized Cargo Pods",
            "Comm Sniffer",
            "Production",
            "Trade Fleet Departure",
            "Raid");*/

}

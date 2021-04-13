package starhacker.helper;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.util.Pair;

import java.text.Normalizer;
import java.util.*;
/**
 * Shamelessly stolen from Histidine's Nexerelin mod.
 */
@SuppressWarnings("rawtypes")
public class StringHelper {
    public StringHelper() {
    }

    public static String getString(String category, String id, boolean ucFirst) {
        String str;

        try {
            str = Global.getSettings().getString(category, id);
        } catch (Exception var5) {
            Global.getLogger(starhacker.helper.StringHelper.class).warn(var5);
            return "[INVALID]" + id;
        }

        if (ucFirst) {
            str = Misc.ucFirst(str);
        }

        return str;
    }

    public static String getString(String category, String id) {
        return getString(category, id, false);
    }

    public static String getString(String id, boolean ucFirst) {
        return getString("general", id, ucFirst);
    }

    public static String getString(String id) {
        return getString("general", id, false);
    }

    public static String ucFirstIgnore$(String str) {
        if (str == null) {
            return "Null";
        } else if (str.isEmpty()) {
            return "";
        } else {
            return str.charAt(0) != '$' ? Misc.ucFirst(str) : "" + str.charAt(0) + ("" + str.charAt(1)).toUpperCase() + str.substring(2);
        }
    }

    public static String substituteToken(String toModify, String token, String replace, boolean ucFormToo) {
        String str = toModify.replaceAll("\\" + token, replace);
        if (ucFormToo) {
            str = str.replaceAll("\\" + ucFirstIgnore$(token), Misc.ucFirst(replace));
        }

        return str;
    }

    public static String substituteToken(String toModify, String token, String replace) {
        return toModify.replaceAll("\\" + token, replace);
    }

    public static String substituteTokens(String toModify, Map<String, String> replacements) {
        return substituteTokens(toModify, replacements, true);
    }

    public static String substituteTokens(String toModify, Map<String, String> replacements, boolean ucFormToo) {
        Map.Entry tmp;
        for(Iterator var3 = replacements.entrySet().iterator(); var3.hasNext(); toModify = substituteToken(toModify, (String)tmp.getKey(), (String)tmp.getValue(), ucFormToo)) {
            tmp = (Map.Entry)var3.next();
        }

        return toModify;
    }

    public static String substituteTokens(String toModify, List<Pair<String, String>> replacements) {
        return substituteTokens(toModify, replacements, true);
    }

    public static String substituteTokens(String toModify, List<Pair<String, String>> replacements, boolean ucFormToo) {
        Pair tmp;
        for(Iterator var3 = replacements.iterator(); var3.hasNext(); toModify = substituteToken(toModify, (String)tmp.one, (String)tmp.two, ucFormToo)) {
            tmp = (Pair)var3.next();
        }

        return toModify;
    }

    public static String getStringAndSubstituteToken(String category, String id, String token, String replace) {
        return getStringAndSubstituteToken(category, id, token, replace, false);
    }

    public static String getStringAndSubstituteToken(String category, String id, String token, String replace, boolean ucFormToo) {
        String str = getString(category, id);
        return substituteToken(str, token, replace, ucFormToo);
    }

    public static String getStringAndSubstituteToken(String id, String token, String replace) {
        return getStringAndSubstituteToken("general", id, token, replace, false);
    }

    public static String getStringAndSubstituteToken(String id, String token, String replace, boolean ucFormToo) {
        return getStringAndSubstituteToken("general", id, token, replace, ucFormToo);
    }

    public static String getStringAndSubstituteTokens(String category, String id, List<Pair<String, String>> replacements) {
        String str = getString(category, id);
        return substituteTokens(str, replacements);
    }

    public static String getStringAndSubstituteTokens(String category, String id, Map<String, String> replacements) {
        String str = getString(category, id);
        return substituteTokens(str, replacements);
    }

    public static String getStringAndSubstituteTokens(String id, List<Pair<String, String>> replacements) {
        return getStringAndSubstituteTokens("general", id, replacements);
    }

    public static String getStringAndSubstituteTokens(String id, Map<String, String> replacements) {
        return getStringAndSubstituteTokens("general", id, replacements);
    }

    public static String getHisOrHer(PersonAPI person) {
        switch(person.getGender()) {
            case MALE:
                return getString("his");
            case FEMALE:
                return getString("her");
            default:
                return getString("their");
        }
    }

    public static String getShipOrFleet(CampaignFleetAPI fleet) {
        String fleetOrShip = getString("general", "fleet");
        if (fleet != null && fleet.getFleetData().getMembersListCopy().size() == 1) {
            fleetOrShip = getString("general", "ship");
            if (fleet.getFleetData().getMembersListCopy().get(0).isFighterWing()) {
                fleetOrShip = getString("general", "fighterWing");
            }
        }

        return fleetOrShip;
    }

    public static String flattenToAscii(String string) {
        StringBuilder sb = new StringBuilder(string.length());
        string = Normalizer.normalize(string, Normalizer.Form.NFD);
        char[] var2 = string.toCharArray();
        int var3 = var2.length;

        for (char c : var2) {
            if (c <= 127) {
                sb.append(c);
            }
        }

        return sb.toString();
    }

    public static List<String> factionIdListToFactionNameList(List<String> factionIds, boolean ucFirst) {
        List<String> result = new ArrayList<>();

        String name;
        for(Iterator<String> var3 = factionIds.iterator(); var3.hasNext(); result.add(name)) {
            String factionId = var3.next();
            FactionAPI faction = Global.getSector().getFaction(factionId);
            name = faction.getDisplayName();
            if (ucFirst) {
                name = Misc.ucFirst(name);
            }
        }

        return result;
    }

    public static String writeStringCollection(Collection<String> strings) {
        return writeStringCollection(strings, false, false);
    }

    public static String writeStringCollection(Collection<String> strings, boolean includeAnd, boolean oxfordComma) {
        String str = "";
        int num = 0;
        Iterator var5 = strings.iterator();

        while(true) {
            do {
                if (!var5.hasNext()) {
                    return str;
                }

                String entry = (String)var5.next();
                str = str + entry;
                ++num;
            } while(num >= strings.size());

            if (oxfordComma || !includeAnd || num <= strings.size() - 1) {
                str = str + ", ";
            }

            if (includeAnd) {
                str = str + getString("and") + " ";
            }
        }
    }
}

package starhacker.helper;

import com.fs.starfarer.api.campaign.SectorEntityToken;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;


import java.util.Comparator;

public class DistanceHelper {

    /**
     * Check if two {@link SectorEntityToken}s are within a certain distance of
     * each other, taking interaction radii into account.
     * <p>
     * This is <i>vastly</i> more efficient than comparisons using
     * {@link MathUtils#getDistance(SectorEntityToken, SectorEntityToken)},
     * and should be used whenever possible.
     *
     * @param token1 The first {@link SectorEntityToken} to check.
     * @param token2 The second {@link SectorEntityToken} to check.
     * @param range  The minimum distance between {@code token1} and
     *               {@code token2}.
     *
     * @return Whether {@code token1} is within {@code range} su of
     *         {@code token2}.
     *
     * @since 1.8
     */
    public static boolean isWithinRangeHyper(SectorEntityToken token1, SectorEntityToken token2, float range)
    {
        return MathUtils.isWithinRange(token1.getLocationInHyperspace(), token2.getLocationInHyperspace(),
                (range + token1.getRadius() + token2.getRadius()));
    }

    /**
     * Check if a {@link SectorEntityToken} is within a certain distance of a
     * location, taking interaction radius into account.
     * <p>
     * This is <i>vastly</i> more efficient than comparisons using
     * {@link MathUtils#getDistance(SectorEntityToken, Vector2f)},
     * and should be used whenever possible.
     *
     * @param token The {@link SectorEntityToken} to check.
     * @param loc   The {@link Vector2f} to check.
     * @param range The minimum distance between {@code token} and {@code loc}.
     *
     * @return Whether {@code token} is within {@code range} su of {@code loc}.
     *
     * @since 1.8
     */
    public static boolean isWithinRangeHyper(SectorEntityToken token, Vector2f loc, float range)
    {
        return MathUtils.isWithinRange(token.getLocationInHyperspace(), loc, (range + token.getRadius()));
    }



    public static class SortTokensByHyperDistance implements Comparator<SectorEntityToken>
    {
        private Vector2f location;

        private SortTokensByHyperDistance(){}

        public SortTokensByHyperDistance(Vector2f location){
            this.location = location;
        }
        public SortTokensByHyperDistance(SectorEntityToken sector){
            this.location = sector.getLocationInHyperspace();
        }

        @Override
        public int compare(SectorEntityToken o1, SectorEntityToken o2){
            return Float.compare(MathUtils.getDistanceSquared(o1.getLocationInHyperspace(),
                    location), MathUtils.getDistanceSquared(o2.getLocationInHyperspace(), location));
        }
    }
}


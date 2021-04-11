package starhacker.helper;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.impl.campaign.ids.Tags;
import org.jetbrains.annotations.Nullable;
import org.lazywizard.lazylib.MathUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CampaignHelper {
    @Nullable
    public static <T extends SectorEntityToken> T getNearestEntityWithTagHyper(
            SectorEntityToken token, String entityTag)
    {
        T closest = null;
        float distanceSquared, closestDistanceSquared = Float.MAX_VALUE;

        for (SectorEntityToken tmp : Global.getSector().getEntitiesWithTag(entityTag))
        {
            T entity = (T) tmp;

            if (entity == token)
            {
                continue;
            }

            distanceSquared = MathUtils.getDistanceSquared(token.getLocationInHyperspace(),
                    entity.getLocationInHyperspace());
            if (distanceSquared < closestDistanceSquared)
            {
                closest = entity;
                closestDistanceSquared = distanceSquared;
            }
        }

        return closest;
    }

    /**
     * Find entities with a specific tag near a {@link SectorEntityToken},
     * excluding itself.
     *
     * @param token     The {@link SectorEntityToken} to search around.
     * @param range     How far around {@code token} to search.
     * @param entityTag The tag we should be searching for; for example:
     *                  {@link Tags#STATION} or {@link Tags#JUMP_POINT}.
     *
     * @return All objects with tag {@code entityTag} within range of
     *         {@code token}.
     *
     * @since 2.0
     */
    public static <T extends SectorEntityToken> List<T> getNearbyEntitiesWithTagHyper(
            SectorEntityToken token, float range, String entityTag)
    {
        List<T> entities = new ArrayList<>();

        // Find all tokens of the given type within range
        for (SectorEntityToken tmp : Global.getSector().getEntitiesWithTag(entityTag))
        {
            T entity = (T) tmp;

            // Exclude passed in token
            if (entity == token)
            {
                continue;
            }

            // Add any token within range
            if (DistanceHelper.isWithinRangeHyper(token, entity, range))
            {
                entities.add(entity);
            }
        }

        return entities;
    }

    /**
     * Find the closest entity with a specific tag and faction near a
     * {@link SectorEntityToken}, excluding itself.
     *
     * @param token     The {@link SectorEntityToken} to search around.
     * @param entityTag The tag we should be searching for; for example:
     *                  {@link Tags#STATION} or {@link Tags#JUMP_POINT}.
     * @param faction   The faction ownership we are looking for.
     *
     * @return The object with tag {@code entityTag} closest to {@code token}
     *         that is owned by {@code faction}, or {@code null} if none are found.
     *
     * @since 2.0
     */
    @Nullable
    public static <T extends SectorEntityToken> T getNearestEntityFromFactionHyper(
            SectorEntityToken token, String entityTag, FactionAPI faction)
    {
        T closest = null;
        float distanceSquared, closestDistanceSquared = Float.MAX_VALUE;

        for (SectorEntityToken tmp : Global.getSector().getEntitiesWithTag(entityTag))
        {
            T entity = (T) tmp;

            if (entity == token || faction != entity.getFaction())
            {
                continue;
            }

            distanceSquared = MathUtils.getDistanceSquared(token.getLocationInHyperspace(),
                    entity.getLocationInHyperspace());
            if (distanceSquared < closestDistanceSquared)
            {
                closest = entity;
                closestDistanceSquared = distanceSquared;
            }
        }

        return closest;
    }

    /**
     * Find entities with a specific tag and from a specific faction near a
     * {@link SectorEntityToken}, excluding itself.
     *
     * @param token     The {@link SectorEntityToken} to search around.
     * @param range     How far around {@code token} to search.
     * @param entityTag The tag we should be searching for; for example:
     *                  {@link Tags#STATION} or {@link Tags#JUMP_POINT}.
     * @param faction   What faction the entity must be owned by.
     *
     * @return All objects with tag {@code entityTag} and faction
     *         {@code faction} within range of {@code token}.
     *
     * @since 2.0
     */
    public static <T extends SectorEntityToken> List<T> getNearbyEntitiesFromFactionHyper(
            SectorEntityToken token, float range, String entityTag, FactionAPI faction)
    {
        List<T> entities = new ArrayList<>();

        // Find all tokens of the given type within range
        for (SectorEntityToken tmp : Global.getSector().getEntitiesWithTag(entityTag))
        {
            T entity = (T) tmp;

            // Exclude passed in token and tokens of wrong faction
            if (entity == token || faction != entity.getFaction())
            {
                continue;
            }

            // Add any token within range
            if (DistanceHelper.isWithinRangeHyper(token, entity, range))
            {
                entities.add(entity);
                Collections.sort(entities, new DistanceHelper.SortTokensByHyperDistance(token));
            }
        }

        return entities;
    }

    /**
     * Find all entities with a specific tag and from a specific faction related to
     * {@link SectorEntityToken}, excluding itself.
     *
     * @param token     The {@link SectorEntityToken} to search around.
     *
     * @param entityTag The tag we should be searching for; for example:
     *                  {@link Tags#STATION} or {@link Tags#JUMP_POINT}.
     * @param faction   What faction the entity must be owned by.
     *
     * @return All objects with tag {@code entityTag} and faction
     *         {@code faction} within range of {@code token}.
     *
     * @since 2.0
     */
    public static <T extends SectorEntityToken> List<T> getAllEntitiesFromFactionHyper(
            SectorEntityToken token, String entityTag, FactionAPI faction)
    {
        List<T> entities = new ArrayList<>();

        // Find all tokens of the given type within range
        for (SectorEntityToken tmp : Global.getSector().getEntitiesWithTag(entityTag))
        {
            T entity = (T) tmp;

            // Exclude passed in token and tokens of wrong faction
            if (entity == token || faction != entity.getFaction())
            {
                continue;
            }

            entities.add(entity);
            Collections.sort(entities, new DistanceHelper.SortTokensByHyperDistance(token));
        }
        return entities;
    }
}


package de.photon.anticheataddition.util.violationlevels;

import com.google.common.base.Preconditions;
import de.photon.anticheataddition.AntiCheatAddition;
import de.photon.anticheataddition.modules.ViolationModule;
import de.photon.anticheataddition.util.datastructure.broadcast.BroadcastReceiver;
import de.photon.anticheataddition.util.datastructure.broadcast.Broadcaster;
import de.photon.anticheataddition.util.violationlevels.threshold.ThresholdManagement;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public abstract class ViolationManagement extends Broadcaster<Player> implements BroadcastReceiver<Player>
{
    /**
     * The module id of the handler
     */
    @NotNull protected final ViolationModule module;
    /**
     * A {@link List} of thresholds which is guaranteed to be sorted.
     */
    @NotNull protected final ThresholdManagement thresholds;

    protected ViolationManagement(@NotNull ViolationModule module, @NotNull ThresholdManagement thresholds)
    {
        this.module = Preconditions.checkNotNull(module, "Tried to construct ViolationManagement with null module.");
        this.thresholds = Preconditions.checkNotNull(thresholds, "Tried to construct ViolationManagement with null ThresholdManagement.");
    }

    /**
     * Flags a {@link Player} according to the options set in the {@link Flag}.
     */
    public abstract void flag(@NotNull final Flag flag);

    /**
     * @param uuid the {@link UUID} of the {@link Player} whose vl should be returned.
     *
     * @return the vl of the given uuid.
     */
    public abstract int getVL(@NotNull final UUID uuid);

    /**
     * Sets the vl of a player.
     *
     * @param player the {@link Player} whose vl should be set
     * @param newVl  the new vl of the player.
     */
    public abstract void setVL(@NotNull final Player player, final int newVl);

    /**
     * Adds an {@link Integer} to the vl of a player. The number can be negative, this will decrease the vl then.
     *
     * @param player the {@link Player} whose vl should be set
     * @param vl     the vl to be added to the current vl.
     */
    protected abstract void addVL(@NotNull final Player player, final int vl);

    /**
     * Used to execute the commands that are defined in the config section CHECK_NAME.thresholds
     *
     * @param player the {@link Player} that should be punished and that should be used to apply the placeholders
     * @param fromVl the last vl of the player before the addition and the searching-range for command.
     */
    protected void punishPlayer(@NotNull Player player, int fromVl, int toVl)
    {
        // Only schedule the command execution if the plugin is loaded.
        if (AntiCheatAddition.getInstance().isEnabled()) this.thresholds.executeThresholds(fromVl, toVl, player);
    }

    @Override
    public void receive(Player value)
    {
        // Ignore, only important for aggregations.
    }
}

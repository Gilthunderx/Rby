package dev.wotnak.rby.announcement

import org.bukkit.entity.Player

interface PlayerAnnouncement {

    fun receiversOf(catcher: Player): Collection<Player>

    companion object {
        fun ofEmpty(): PlayerAnnouncement =
            NoAnnouncement()

        fun ofRanged(radius: Double): PlayerAnnouncement {
            require(radius >= 0) { "Radius must not be negative" }
            return RangedAnnouncement(radius)
        }

        fun ofBaseOnly(): PlayerAnnouncement =
            BaseOnlyAnnouncement()

        fun ofServerBroadcast(): PlayerAnnouncement =
            ServerAnnouncement()
    }

}

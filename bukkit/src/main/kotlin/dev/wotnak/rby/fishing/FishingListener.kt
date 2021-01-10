package dev.wotnak.rby.fishing

import dev.wotnak.rby.configuration.Config
import dev.wotnak.rby.configuration.Lang
import dev.wotnak.rby.fishing.catchhandler.CatchHandler
import dev.wotnak.rby.fishing.catchhandler.CompetitionRecordAdder
import dev.wotnak.rby.fishing.catchhandler.NewFirstBroadcaster
import dev.wotnak.rby.fishing.competition.FishingCompetition
import dev.wotnak.rby.item.FishItemStackConverter
import org.bukkit.Material
import org.bukkit.entity.Item
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerFishEvent

class FishingListener(
    private val fishTypeTable: FishTypeTable,
    private val converter: FishItemStackConverter,
    private val competition: FishingCompetition,
    private val globalCatchHandlers: List<CatchHandler>
) : Listener {

    private val fishMaterials: List<Material> = listOf(
        Material.COD, Material.SALMON, Material.PUFFERFISH, Material.TROPICAL_FISH
    )
    private val replacingVanillaConditions: List<(PlayerFishEvent) -> Boolean> = listOf<(PlayerFishEvent) -> Boolean>(
        {
            if (Config.standard.boolean("general.only-for-contest"))
                competition.isEnabled()
            else
                true
        },
        {
            if (Config.standard.boolean("general.replace-only-fish"))
                (it.caught as Item).itemStack.type in fishMaterials
            else
                true
        }
    )

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun onPlayerFish(event: PlayerFishEvent) {
        if (event.state == PlayerFishEvent.State.CAUGHT_FISH && event.caught is Item) {
            if (Config.standard.boolean("general.no-fishing-unless-contest") && !competition.isEnabled()) {
                event.isCancelled = true
                event.player.sendMessage(Lang.text("no-fishing-allowed"))
            } else if (canReplaceVanillaFishing(event)) {
                val caught = event.caught as Item
                val fish = fishTypeTable.pickRandomType(caught, event.player, competition).generateFish()

                for (handler in catchHandlersOf(event, fish)) {
                    handler.handle(event.player, fish)
                }
                caught.itemStack = converter.createItemStack(fish, event.player)
            }
        }
    }

    private fun canReplaceVanillaFishing(event: PlayerFishEvent): Boolean {
        return replacingVanillaConditions.all { it(event) }
    }

    private fun catchHandlersOf(event: PlayerFishEvent, fish: Fish): Collection<CatchHandler> {
        val catchHandlers = globalCatchHandlers + fish.type.catchHandlers

        val contestDisabledWorlds = Config.standard.strings("general.contest-disabled-worlds")
            .map { event.player.server.getWorld(it) }
        return if (event.player.world in contestDisabledWorlds) {
            catchHandlers.filter {
                it !is CompetitionRecordAdder && it !is NewFirstBroadcaster
            }
        } else {
            catchHandlers
        }
    }

}

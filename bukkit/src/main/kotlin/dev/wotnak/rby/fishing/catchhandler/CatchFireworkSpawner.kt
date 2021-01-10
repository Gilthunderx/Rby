package dev.wotnak.rby.fishing.catchhandler

import dev.wotnak.rby.fishing.Fish
import org.bukkit.Color
import org.bukkit.FireworkEffect
import org.bukkit.entity.Firework
import org.bukkit.entity.Player

class CatchFireworkSpawner : CatchHandler {

    private val effect = FireworkEffect.builder()
        .with(FireworkEffect.Type.BALL_LARGE)
        .withColor(Color.AQUA)
        .withFade(Color.BLUE)
        .withTrail()
        .withFlicker()
        .build()

    override fun handle(catcher: Player, fish: Fish) {
        if (fish.type.hasCatchFirework) {
            val firework = catcher.world.spawn(catcher.location, Firework::class.java)
            val meta = firework.fireworkMeta
            meta.addEffect(effect)
            meta.power = 1
            firework.fireworkMeta = meta
        }
    }

}

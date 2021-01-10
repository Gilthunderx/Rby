package dev.wotnak.rby.configuration.format

import dev.wotnak.rby.configuration.translated
import org.bukkit.entity.Player

class TextListFormat(
    private var strings: List<String>
) : Format<TextListFormat, List<String>> {

    override fun replace(vararg pairs: Pair<String, Any>): TextListFormat {
        for (pair in pairs) {
            strings = strings.map { it.replace(pair.first, pair.second.toString()) }
        }
        return this
    }

    override fun output(player: Player?): List<String> {
        return strings.translated().map { Format.tryReplacing(it, player) }
    }

}

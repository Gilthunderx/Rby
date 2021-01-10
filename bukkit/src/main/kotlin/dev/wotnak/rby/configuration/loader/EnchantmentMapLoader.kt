package dev.wotnak.rby.configuration.loader

import dev.wotnak.rby.configuration.ConfigurationValueAccessor
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment

class EnchantmentMapLoader : CustomLoader<Map<Enchantment, Int>> {

    override fun loadFrom(section: ConfigurationValueAccessor, path: String): Map<Enchantment, Int> {
        return if (section.contains(path)) {
            section.strings(path).map {
                val tokens = it.split(DELIMITER)
                val enchantment = Enchantment.getByKey(NamespacedKey.minecraft(tokens[0]))!!
                val level = tokens[1].toInt()
                Pair<Enchantment, Int>(enchantment, level)
            }.toMap()
        } else {
            emptyMap()
        }
    }

    companion object {
        private const val DELIMITER = '|'
    }

}

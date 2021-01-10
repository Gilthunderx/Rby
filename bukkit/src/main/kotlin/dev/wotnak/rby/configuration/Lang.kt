package dev.wotnak.rby.configuration

import dev.wotnak.rby.configuration.format.TextFormat
import dev.wotnak.rby.configuration.format.TextListFormat
import java.time.Duration

object Lang {

    const val ALTERNATE_COLOR_CODE = '&'
    private val langConfig: ConfigurationAccessor = Config.lang

    fun text(id: String): String =
        langConfig.string(id).translated()

    fun texts(id: String): List<String> =
        langConfig.strings(id).translated()

    fun format(id: String): TextFormat =
        TextFormat(langConfig.string(id))

    fun formats(id: String): TextListFormat =
        TextListFormat(langConfig.strings(id))

    fun time(second: Long): String {
        val builder = StringBuilder()
        val duration = Duration.ofSeconds(second)

        if (duration.toMinutes() > 0) {
            builder.append(duration.toMinutes())
                .append(text("time-format-minutes"))
                .append(" ")
        }
        builder.append(duration.seconds % 60)
            .append(text("time-format-seconds"))
        return builder.toString()
    }

}

/*
* Taken from https://github.com/Stardust-Discord/Octave/blob/development/src/main/kotlin/gg/octave/bot/entities/framework/parsers/EnumParser.kt
*/

package xyz.aesthetical.lookupbot.utils.parsers.enums

import me.devoxin.flight.api.Context
import me.devoxin.flight.internal.parsers.Parser
import java.util.*

open class EnumParser<T : Enum<*>>(private val enumClass: Class<T>) : Parser<T> {
  override fun parse(ctx: Context, param: String): Optional<T> {
    return Optional.ofNullable(
      enumClass.enumConstants.firstOrNull {
        it.name.equals(param, ignoreCase = true)
      }
    )
  }
}
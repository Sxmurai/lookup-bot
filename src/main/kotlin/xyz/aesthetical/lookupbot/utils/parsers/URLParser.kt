package xyz.aesthetical.lookupbot.utils.parsers

import me.devoxin.flight.api.Context
import me.devoxin.flight.internal.parsers.Parser
import java.net.URL
import java.util.*

class URLParser : Parser<URL> {
  override fun parse(ctx: Context, param: String): Optional<URL> {
    return try {
      var finalUrl = param
        .replace("<(.+)>".toRegex(), "$1")

      if (!"https?://".toRegex().containsMatchIn(finalUrl)) {
        finalUrl = "http://${finalUrl}"
      }
      
      Optional.of(URL(finalUrl))
    } catch (e: Exception) {
      Optional.empty()
    }
  }
}
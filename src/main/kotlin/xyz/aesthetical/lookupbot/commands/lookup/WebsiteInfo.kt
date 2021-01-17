package xyz.aesthetical.lookupbot.commands.lookup

import me.devoxin.flight.api.Context
import me.devoxin.flight.api.annotations.Command
import me.devoxin.flight.api.entities.Cog
import xyz.aesthetical.lookupbot.utils.Line
import xyz.aesthetical.lookupbot.utils.PrologBuilder
import java.net.URL

class WebsiteInfo : Cog {
  @Command(
    aliases = ["web", "websiteinfo", "webinfo"],
    description = "Displays information on a website"
  )
  fun website(ctx: Context, url: URL) {
    return ctx.send {
      setColor("3377de".toInt(16))
      setAuthor(
        "Website Info",
        "${url.protocol}://${url.host}",
        ctx.author.effectiveAvatarUrl
      )
      setDescription(
        PrologBuilder().apply {
          addLine(Line("Hostname", url.host))
          addLine(
            Line(
              "Port",
              if (url.port == -1)
                "None"
              else url.port.toString()
            )
          )
          addLine(
            Line(
              "Default Port",
              if (url.defaultPort == -1)
                "None"
              else url.defaultPort.toString()
            )
          )
          addLine(Line("Protocol", url.protocol))
          addLine(Line("Reference", url.ref ?: "None"))
        }.build()
      )
    }
  }
}
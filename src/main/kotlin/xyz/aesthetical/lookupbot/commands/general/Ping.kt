package xyz.aesthetical.lookupbot.commands.general

import me.devoxin.flight.api.Context
import me.devoxin.flight.api.annotations.Command
import me.devoxin.flight.api.entities.Cog
import xyz.aesthetical.lookupbot.utils.Line
import xyz.aesthetical.lookupbot.utils.PrologBuilder

class Ping : Cog {
  @Command(
    aliases = ["latency", "pong"],
    description = "Displays the bots latency"
  )
  fun ping(ctx: Context) {
    ctx.typing {
      ctx.jda.restPing.queue {
        ctx.send {
          setColor("3377de".toInt(16))
          setDescription(
            PrologBuilder()
              .addLine(Line("WS", "${ctx.jda.gatewayPing}ms"))
              .addLine(Line("REST", "${it}ms"))
              .build()
          )
        }
      }
    }
  }
}
package xyz.aesthetical.lookupbot.commands.lookup

import khttp.get
import me.devoxin.flight.api.Context
import me.devoxin.flight.api.annotations.Command
import me.devoxin.flight.api.entities.Cog
import java.net.URL

class IsOnline : Cog {
  @Command(
    aliases = ["isup", "online"],
    description = "Displays if a website is up"
  )
  fun isonline(ctx: Context, url: URL) {
    val request = get(url.toString())
    
    // DO NOT USE AT&T wifi for this
    // cause at&t is a bitch and redirects you to their search engine if its invalid smfh
    return if (request.statusCode != 200) {
      ctx.send {
        setColor("f55e53".toInt(16))
        setDescription("**${url.host.take(25)}** is not online.")
      }
    } else {
      ctx.send {
        setColor("3377de".toInt(16))
        setDescription("**${url.host.take(25)}** is online.")
      }
    }
  }
}
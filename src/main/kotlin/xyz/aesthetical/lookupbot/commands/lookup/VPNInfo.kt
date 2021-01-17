package xyz.aesthetical.lookupbot.commands.lookup

import khttp.get
import me.devoxin.flight.api.Context
import me.devoxin.flight.api.annotations.Command
import me.devoxin.flight.api.entities.Cog
import org.json.JSONObject
import xyz.aesthetical.lookupbot.utils.Line
import xyz.aesthetical.lookupbot.utils.PrologBuilder
import java.lang.StringBuilder

class VPNInfo : Cog {
  @Command(
    aliases = ["vpn"],
    description = "Displays information about a VPN IP"
  )
  fun vpninfo(ctx: Context, ip: String) {
    val request = get("https://vpnapi.io/api/$ip").jsonObject
    
    if (request.has("message")) {
      return ctx.send {
        setColor("f55e53".toInt(16))
        setDescription(request.getString("message"))
      }
    }
    
    ctx.send {
      setColor("3377de".toInt(16))
      setAuthor("VPN Lookup", null, ctx.author.effectiveAvatarUrl)
      setDescription(request.getString("ip"))
      
      addField(
        "› Location",
        allData(request.getJSONObject("location")),
        false
      )
      
      addField(
        "› Network",
        allData(request.getJSONObject("network")),
        false
      )
      
      addField(
        "› VPN",
        allData(request.getJSONObject("security")),
        false
      )
    }
  }
  
  private fun allData(data: JSONObject): String {
    val prologBuilder = PrologBuilder()
    
    for ((k, v) in data.toMap()) {
      if (v.toString().isEmpty()) {
        continue
      }
      
      prologBuilder
        .addLine(
          Line(
            k
              .replace("_" ," ")
              .replace("(\\b\\w)".toRegex()) { res: MatchResult -> res.groupValues[1].toUpperCase() },
            if (v is Boolean)
              if (v) "Yes" else "No"
            else v.toString()
          )
        )
    }
    
    return prologBuilder.build()
  }
}
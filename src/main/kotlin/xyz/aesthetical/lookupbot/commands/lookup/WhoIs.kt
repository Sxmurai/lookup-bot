package xyz.aesthetical.lookupbot.commands.lookup

import khttp.get
import me.devoxin.flight.api.Context
import me.devoxin.flight.api.annotations.Command
import me.devoxin.flight.api.entities.Cog
import org.json.JSONObject
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.safety.Whitelist
import xyz.aesthetical.lookupbot.utils.Line
import xyz.aesthetical.lookupbot.utils.PrologBuilder
import java.net.URL

class WhoIs : Cog {
  @Command(
    aliases = ["who", "lookup"],
    description = "Displays Whois information on a website"
  )
  fun whois(ctx: Context, url: URL) {
    val response = get(
      "https://whoisjs.com/api/v1/${url.host}"
    ).jsonObject
    
    if (!response.getBoolean("success")) {
      return ctx.send {
        setColor("f55e53".toInt(16))
        setDescription("I couldn't find any information on that URL.")
      }
    }
    
    ctx.send {
      setColor("3377de".toInt(16))
      setAuthor(
        "Whois Information",
        "${url.protocol}://${url.host}",
        ctx.author.effectiveAvatarUrl
      )
      
      addField(
        "› Registry",
        allData(response.getJSONObject("registry")),
        false
      )
      
      addField(
        "› Registrar",
        allData(response.getJSONObject("registrar")),
        false,
      )
      
      addField(
        "› Dates",
        PrologBuilder().apply {
          addLine(
            Line(
              "Creation",
              response
                .getJSONObject("creation")
                .getString("date")
            )
          )
          
          addLine(
            Line(
              "Updated",
              response
                .getJSONObject("updated")
                .getString("date")
            )
          )
        }.build(),
        false
      )
    }
//    val response = get(
//      "https://www.onlydomains.com/whois/whoisengine/request/whoisinfo.php?domain_name=${url.host}&security_code=null"
//    ).text.replace("<b>", "\n")
//
//    if (response.equals("Error! Please insert a valid domain", ignoreCase = true)) {
//      return ctx.send {
//        setColor("f55e53".toInt(16))
//        setDescription("Invalid URL provided.")
//      }
//    }
//
//    ctx.send("```${
//      Jsoup.clean(
//        response,"",
//        Whitelist.none(),
//        Document.OutputSettings()
//          .prettyPrint(false)
//      ).take(1950)
//    }```")
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
package xyz.aesthetical.lookupbot.commands.lookup

import khttp.get
import me.devoxin.flight.api.Context
import me.devoxin.flight.api.annotations.Command
import me.devoxin.flight.api.entities.Cog
import org.json.JSONObject
import xyz.aesthetical.lookupbot.utils.Line
import xyz.aesthetical.lookupbot.utils.PrologBuilder
import xyz.aesthetical.lookupbot.utils.enums.DNSTypeEnum
import java.net.URL

class DNSInfo : Cog {
  @Command(
    aliases = ["dnsinfo", "dnslookup"],
    description = "Displays DNS related information depending on the type"
  )
  fun dns(ctx: Context, type: DNSTypeEnum, url: URL) {
    return lookup(ctx, type, url)
  }
  
  private fun lookup(ctx: Context, type: DNSTypeEnum, url: URL) {
    val request = get(
      "https://cloudflare-dns.com/dns-query?name=${url.host}&type=${type}",
      mapOf(
        "Accept" to "application/dns-json"
      )
    ).jsonObject
    
    /*
    * Taken from https://github.com/MattIPv4/DNS-over-Discord/blob/8790a362cf910fc19a17417c56bb4448bcc58850/src/utils/dns.js#L1#L22
    */
    val errorCodes = mapOf(
      1  to "A format error [1 - FormErr] occurred when looking up the domain",
      2  to "An unexpected server failure [2 - ServFail] occurred when looking up the domain",
      3  to "A non-existent domain [3 - NXDomain] was requested and could not be found",
      4  to "A request was made that is not implemented [4 - NotImp] by the resolver",
      5  to "The query was refused [5 - Refused] by the DNS resolver",
      6  to "Name Exists when it should not",
      7  to "RR Set Exists when it should not",
      8  to "RR Set that should exist does not",
      9  to "Server Not Authoritative for zone or Not Authorized",
      10 to "Name not contained in zone",
      11 to "DSO-TYPE Not Implemented",
      16 to "Bad OPT Version or TSIG Signature Failure",
      17 to "Key not recognized",
      18 to "Signature out of time window",
      19 to "Bad TKEY Mode",
      20 to "Duplicate key name",
      21 to "Algorithm not supported",
      22 to "Bad Truncation",
      23 to "Bad/missing Server Cookie",
    )
    
    if (request.getInt("Status") != 0) {
      return ctx.send {
        setColor("f55e53".toInt(16))
        setDescription(errorCodes[request.getInt("Status")])
      }
    }
    
    if (request.getJSONArray("Answer").length() == 0) {
      return ctx.send {
        setColor("f55e53".toInt(16))
        setDescription("No results found.")
      }
    }
    
    val results = arrayListOf<String>()
    
    for (item in request.getJSONArray("Answer")) {
      val prologBuilder = PrologBuilder()
      
      for ((k, v) in (item as JSONObject).toMap()) {
        prologBuilder.addLine(
          Line(
            k.replace("(\\b\\w)".toRegex()) { res: MatchResult -> res.groupValues[1].toUpperCase() },
            v.toString()
          )
        )
      }
      
      results.add(prologBuilder.build())
    }
    
    return ctx.send {
      setColor("3377de".toInt(16))
      setAuthor("DNS Results", "http://${url.host}", ctx.author.effectiveAvatarUrl)
      setDescription(results.joinToString("\n\n"))
    }
  }
}









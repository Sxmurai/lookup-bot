package xyz.aesthetical.lookupbot

import me.devoxin.flight.api.CommandClient
import me.devoxin.flight.api.CommandClientBuilder
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.MemberCachePolicy
import net.dv8tion.jda.api.utils.cache.CacheFlag
import xyz.aesthetical.lookupbot.listeners.FlightListener
import xyz.aesthetical.lookupbot.listeners.JDAListener
import xyz.aesthetical.lookupbot.utils.parsers.URLParser
import xyz.aesthetical.lookupbot.utils.parsers.enums.DNSOptionParser

object Launcher {
  val config = Configuration()
  
  lateinit var commandClient: CommandClient
    private set
  
  lateinit var jda: JDA
    private set
  
  @ExperimentalStdlibApi
  @JvmStatic
  fun main(args: Array<String>) {
    val prefixes = config.get<List<String>>("bot.prefixes")
    
    commandClient = CommandClientBuilder()
      .addEventListeners(FlightListener())
      .registerDefaultParsers()
      .addCustomParser(URLParser())
      .addCustomParser(DNSOptionParser())
      .setPrefixes(prefixes)
      .setOwnerIds(* config.get<List<String>>("bot.owners").toTypedArray())
      .configureDefaultHelpCommand { enabled = false }
      .build()
    
    jda = JDABuilder.createLight(config.get("bot.token"))
      .addEventListeners(JDAListener(), commandClient)
      .setStatus(OnlineStatus.DO_NOT_DISTURB)
      .setActivity(Activity.playing("${prefixes[0]}help"))
      .enableIntents(GatewayIntent.GUILD_MESSAGES)
      .setMemberCachePolicy(MemberCachePolicy.NONE)
      .disableCache(
        CacheFlag.ACTIVITY,
        CacheFlag.EMOTE,
        CacheFlag.CLIENT_STATUS,
        CacheFlag.MEMBER_OVERRIDES,
        CacheFlag.VOICE_STATE
      ).build()
    
    commandClient.commands.register("xyz.aesthetical.lookupbot.commands")
  }
}
package xyz.aesthetical.lookupbot.commands.general

import me.devoxin.flight.api.Context
import me.devoxin.flight.api.annotations.Command
import me.devoxin.flight.api.entities.Cog
import net.dv8tion.jda.api.EmbedBuilder
import xyz.aesthetical.lookupbot.utils.Line
import xyz.aesthetical.lookupbot.utils.PrologBuilder
import xyz.aesthetical.lookupbot.utils.extensions.isOwner

class Help : Cog {
  @Command(
    aliases = ["h", "halp", "commands", "cmds"],
    description = "Helps the user out a bit"
  )
  fun help(ctx: Context, cmd: String?) {
    val command = ctx.commandClient.commands.findCommandByName(cmd ?: "") ?:
      ctx.commandClient.commands.findCommandByAlias(cmd ?: "")
    
    if (command == null) {
      return fullHelp(ctx)
    } else {
      if (
        command.properties.hidden ||
          command.category.equals("Developers", ignoreCase = true) &&
          !ctx.author.isOwner
      ) {
        return ctx.send {
          setColor("f55e53".toInt(16))
          setDescription("You do not have permission to view this command.")
        }
      }
      
      return ctx.send {
        setColor("3377de".toInt(16))
        addField(
          "› Basic Information",
          PrologBuilder().apply {
            addLine(Line("Trigger", command.name))
            
            if (command.properties.aliases.isNotEmpty()) {
              addLine(
                Line(
                  "Triggers",
                  command.properties.aliases.joinToString(", ")
                )
              )
            }
            
            addLine(Line("Category", command.category ?: "None"))
          }.build(),
          false
        )
      }
    }
  }
  
  private fun fullHelp(ctx: Context) {
    val categories = ctx.commandClient.commands.values
      .groupBy { it.category }
    
    val embed = EmbedBuilder()
      .setAuthor("Command Menu", null, ctx.author.effectiveAvatarUrl)
      .setColor("3377de".toInt(16))
    
    for ((id, commands) in categories) {
      if (
        id.equals("Developers", ignoreCase = true) &&
        !ctx.author.isOwner
      ) {
        continue
      }
      
      embed.addField(
        "› $id (${commands.size})",
        commands.filter {
          !it.properties.hidden
        }.joinToString(", ") { "`${it.name}`" },
        false
      )
    }
    
    ctx.textChannel!!
      .sendMessage(embed.build())
      .submit()
  }
}
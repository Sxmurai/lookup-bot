package xyz.aesthetical.lookupbot.utils.extensions

import net.dv8tion.jda.api.entities.User
import xyz.aesthetical.lookupbot.Launcher

val User.isOwner: Boolean
  get() = Launcher.config.get<List<String>>("bot.owners").contains(id)
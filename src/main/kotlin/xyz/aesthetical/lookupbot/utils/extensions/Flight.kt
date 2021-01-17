package xyz.aesthetical.lookupbot.utils.extensions

import me.devoxin.flight.api.Context
import xyz.aesthetical.lookupbot.Configuration
import xyz.aesthetical.lookupbot.Launcher

val Context.config: Configuration
  get() = Launcher.config
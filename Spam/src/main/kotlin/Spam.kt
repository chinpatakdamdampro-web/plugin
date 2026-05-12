import android.content.Context
import com.aliucord.annotations.AliucordPlugin
import com.aliucord.entities.Plugin
import com.aliucord.api.CommandsAPI
import com.discord.api.commands.ApplicationCommandType
import kotlinx.coroutines.*

@AliucordPlugin
class Spam : Plugin() {
    override fun start(context: Context) {
        commands.registerCommand(
            "spam",
            "Spam messages",
            listOf(
                CommandsAPI.Option(ApplicationCommandType.STRING, "message", "Message to spam", null, true),
                CommandsAPI.Option(ApplicationCommandType.INTEGER, "amount", "How many times", null, true),
                CommandsAPI.Option(ApplicationCommandType.INTEGER, "speed", "Delay in ms (default 50)", null, false)
            )
        ) { ctx ->
            try {
                val message = ctx.getString("message") ?: return@registerCommand ctx.sendMessage("❌ Provide a message")
                val amount = ctx.getInt("amount") ?: return@registerCommand ctx.sendMessage("❌ Provide amount")
                val speed = (ctx.getInt("speed") ?: 50).toLong()

                if (amount !in 1..500) {
                    return@registerCommand ctx.sendMessage("❌ Amount: 1-500")
                }
                
                if (speed < 50) {
                    return@registerCommand ctx.sendMessage("❌ Speed min 50ms")
                }

                ctx.sendMessage("✅ Spamming $amount times @ ${speed}ms...")

                CoroutineScope(Dispatchers.IO).launch {
                    repeat(amount) {
                        try {
                            ctx.sendMessage(message)
                            delay(speed)
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                ctx.sendMessage("⚠️ Error: ${e.message}")
                            }
                            return@launch
                        }
                    }
                }
            } catch (e: Exception) {
                ctx.sendMessage("❌ Error: ${e.message}")
            }
        }
    }

    override fun stop(context: Context) {
        commands.unregisterAll()
    }
}

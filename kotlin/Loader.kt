import core.GameMain
import core.Item
import core.Player
import core.TextManager
import dao.JDBC
import net.mamoe.mirai.Bot
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.event.subscribeMessages
import net.mamoe.mirai.join
import net.mamoe.mirai.message.data.At

@Suppress("UNUSED_VARIABLE")
suspend fun main() {
    val config = ConfigKit.getConfig("config.txt")
    val hina = config["qq"]?.toLong()?.let {
        Bot(it, config["password"]!!) {
        fileBasedDeviceInfo("device.json")
    }.alsoLogin()
    }
    if (hina != null) {
        hina.messageDSL()
        hina.join()
    } else {
        print("Error:未能正常启动bot,请检查配置文件是否正确")
    }
}

fun Bot.messageDSL() {
    GameMain.init()
    this.subscribeMessages {
        "阳菜" reply "在的说!"
        "帮助" reply TextManager.help
        "签到" reply { At(sender as Member) + TextManager.signIn(sender.id) }
        "抽签" reply{ At(sender as Member) + TextManager.getLot(sender.id) }
        "单抽" reply { At(sender as Member) + TextManager.draw(sender.id) }
        "十连抽" reply { At(sender as Member) + TextManager.drawTenTimes(sender.id) }
        "查看库存" reply { At(sender as Member) + TextManager.getInventory(sender.id) }
        "!初始化数据" reply { At(sender as Member) + TextManager.initDate() }
        startsWith("#查看") { reply(At(sender as Member) + TextManager.getItemInfo(it)) }

        //查单词词根系列
        "随机词根" {
            var str = "随机十个词根(无翻译)\n"
            for (i in Word.randomWords(10))
                str += i.word + "\n"
            str.dropLast(1)
            reply(str)
        }
        startsWith("查词根") {
            var temp = Word.findByWord(it)
            when (temp.size) {
                0 -> reply(At(sender as Member) + "没能找到该词根")
                in 1..15 -> {
                    var str = "包含" + it + "的词根有\n"
                    for (i in temp)
                        str += i.word + " = " + i.translate + "\n" + i.link + "\n"
                    str.dropLast(1)
                    reply(str)
                }
                else -> reply(At(sender as Member) + "检索到的词根太多了,请更详细一点")
            }
        }
        startsWith("查释义") {
            var temp = Word.findByTranslate(it)
            when (temp.size) {
                0 -> reply(At(sender as Member) + "没能找到该释义")
                in 1..15 -> {
                    var str = "有释义 $it 的词根有\n"
                    for (i in temp)
                        str += i.word + " = " + i.translate + "\n" + i.link + "\n"
                    str.dropLast(1)
                    reply(str)
                }
                else -> reply(At(sender as Member) + "检索到的词根太多了,请更详细一点")
            }
        }
    }
}
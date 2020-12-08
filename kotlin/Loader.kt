import core.GameMain
import core.TextManager
import net.mamoe.mirai.Bot
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.event.subscribeMessages
import net.mamoe.mirai.join
import net.mamoe.mirai.message.data.At
import tools.ConfigKit
import kotlin.system.exitProcess

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
        case("!杀掉阳菜") {
            if(sender.id == 2218773842) {
                reply("阳菜死了")
                exitProcess(0)
            }
        }

        //猫猫系列
        "公告" reply "在写了 在写了"
        "关于" reply "目前版本:alpha v0.3.2\n项目的开源页面:https://github.com/ALINGCAT/hinabot"
        "帮助" reply TextManager.HELP
        "!初始化数据" reply TextManager.initDate()
        case("签到") {
            if (subject is Group) reply(At(sender as Member) + TextManager.signIn(sender.id))
            else reply(TextManager.signIn(sender.id))
        }
        case("#抽奖") {
            if (subject is Group) reply(At(sender as Member) + TextManager.draw(sender.id))
            else reply(TextManager.draw(sender.id))
        }
        case("查看库存") {
            if (subject is Group) reply(At(sender as Member) + TextManager.getInventory(sender.id))
            else reply(TextManager.getInventory(sender.id))
        }
        startsWith("#查看") {
            if (subject is Group) reply(At(sender as Member) + TextManager.getItemInfo(it))
            else reply(TextManager.getItemInfo(it))
        }
        startsWith("#查询") {
            if (subject is Group) reply(At(sender as Member) + TextManager.getInfo(it))
            else reply(TextManager.getInfo(it))
        }
        startsWith("#抽奖") {
            if (subject is Group) reply(At(sender as Member) + TextManager.draw(sender.id, it))
            else reply(TextManager.draw(sender.id, it))
        }

        //查单词词根系列
        "随机词根" {
            var str = "随机十个词根(无翻译)\n"
            for (i in Word.randomWords(10))
                str += i.word + "\n"
            str.dropLast(1)
            reply(str)
        }
        startsWith("查词根") {
            val temp = Word.findByWord(it)
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
            val temp = Word.findByTranslate(it)
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
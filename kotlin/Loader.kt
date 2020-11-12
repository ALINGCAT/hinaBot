import Core.GameMain
import Core.Item
import Core.Player
import DAO.EssDao
import DAO.JDBC
import net.mamoe.mirai.Bot
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.event.subscribeMessages
import net.mamoe.mirai.join
import net.mamoe.mirai.message.data.At

@Suppress("UNUSED_VARIABLE")
suspend fun main() {
    val config = ConfigKit.getConfig("config.txt")
    val hina = config.get("qq")?.toLong()?.let {
        Bot(it, config.get("password")!!) {
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
    val game = GameMain()
    this.subscribeMessages {
        "阳菜" reply "在的说!"
        case("签到") {
            if (game.isFirstSignIn(sender.id)) reply(At(sender as Member) + " 签到成功!\n您是第一次签到,获得50枚硬币")
            else if (game.isAlreadySignIn(sender.id)) reply(At(sender as Member) + " 您今天已经签到过啦!")
            else {
                val lot = game.getLot(sender.id, true)!!
                reply(At(sender as Member) + " 签到成功!\n您今日抽到的是 ${lot.get("lot")}\n" +
                        "本次签到您获得了${lot.get("increment")}" + "枚硬币\n" +
                        "您总计签到${lot.get("accumulation")}次,额外获得${lot.get("accumulation")}枚硬币\n" +
                        "目前总计拥有${lot.get("total")}" + "枚")
            }
            JDBC.close()
        }
        case("#初始化数据") {
            game.initData()
            reply(At(sender as Member) + " 数据已初始化!")
        }
        "帮助" reply "目前已添加的功能:\n" +
                "签到 每天可以签到一次并抽签\n" +
                "抽签 消耗10硬币进行一次抽签\n" +
                "查看库存 看看自己的仓库都有啥\n" +
                "#查看<物品名字> 查看某个物品的描述\n"
                "单抽 可以抽取一次\n" +
                "十连抽 可以连续抽取十次\n" +
                "(注意每次抽奖需要消耗50硬币)"
        case("抽签") {
            val lot = game.getLot(sender.id)
            if (lot == null) reply(At(sender as Member) + " 硬币不够哦!\n单独抽签需要10枚硬币!")
            else reply(At(sender as Member) + " 抽签成功!\n您抽到的是 ${lot.get("lot")}\n获得了${lot.get("increment")}枚硬币")
        }
        case("单抽") {
            val item = game.draw(sender.id)
            if (item == null) reply(At(sender as Member) + " 您的硬币不够哦!")
            else reply(At(sender as Member) + " 抽奖成功! 消耗50硬币\n本次单抽获得: [${item.rarity}]${item.name}x1")
            JDBC.close()
        }
        case("十连抽") {
            val items = game.drawTenTimes(sender.id)
            if (items == null) reply(At(sender as Member) + "您的硬币不够哦! 一共需要500硬币")
            else {
                var str = ""
                for (e in items) str += "[${e.key.rarity}]${e.key.name}x${e.value} "
                reply(At(sender as Member) + " 抽奖成功! 消耗500硬币\n本次十连抽获得: " + str)
            }
            JDBC.close()
        }
        startsWith("#查看") {
            if (Item.instances[it] == null) reply(At(sender as Member) + " 未能找到该物品")
            else reply(At(sender as Member) + " ${Item.instances[it]!!.name}: ${Item.instances[it]!!.description}")
        }
        case("查看库存") {
            if (Player.instances[sender.id] == null) reply(At(sender as Member) + " 未检测到您的记录,签到可注册游戏!")
            else {
                var str = " 目前拥有${Player.instances[sender.id]!!.coins}枚硬币 以及..\n"
                val items = Player.instances[sender.id]!!.inventory
                if (items.isEmpty()) str += "暂时还没有其他物品哦!"
                else for (e in items) str += "[${e.key.rarity}]${e.key.name}x${e.value} "
                reply(At(sender as Member) + str)
            }
        }
        case("随机词根") {
            var str = "随机十个词根(无翻译)\n"
            for (i in Word.randomWords(10))
                str += i.word + "\n"
            str.dropLast(1)
            reply(str)
        }
        startsWith("查词根") {
            var temp = Word.findByWord(it)
            if (temp.size == 0)
                reply(At(sender as Member) + "没能找到该词根")
            else if (temp.size >= 15)
                reply(At(sender as Member) + "检索到的词根太多了,请更详细一点")
            else {
                var str = "包含" + it + "的词根有\n"
                for (i in temp)
                    str += i.word + " = " + i.translate + "\n" + i.link + "\n"
                str.dropLast(1)
                reply(str)
            }
        }
        startsWith("查释义") {
            var temp = Word.findByTranslate(it)
            if (temp.size == 0)
                reply(At(sender as Member) + "没能找到该释义")
            else if (temp.size >= 15)
                reply(At(sender as Member) + "检索到的词根太多了,请更详细一点")
            else {
                var str = "有释义 " + it + " 的词根有\n"
                for (i in temp)
                    str += i.word + " = " + i.translate + "\n" + i.link + "\n"
                str.dropLast(1)
                reply(str)
            }
        }
    }
}
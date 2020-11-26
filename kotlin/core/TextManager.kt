package core

import dao.JDBC
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.message.data.At

class TextManager {
    companion object {
        const val help = "目前已添加的功能:\n" +
                "签到 每天可以签到一次并抽签\n" +
                "抽签 消耗10硬币进行一次抽签\n" +
                "查看库存 看看自己的仓库都有啥\n" +
                "#查看<物品名字> 查看某个物品的描述\n" +
                "单抽 可以抽取一次\n" +
                "十连抽 可以连续抽取十次\n" +
                "(注意每次抽奖需要消耗50硬币)"
        fun signIn(qq: Long): String {
            if (GameMain.isFirstSignIn(qq)) return " 签到成功!\n您是第一次签到,获得50枚硬币"
            else if (GameMain.isAlreadySignIn(qq)) return " 您今天已经签到过啦!"
            val lot = GameMain.getLot(qq, true)!!
            JDBC.close()
            return " 签到成功!\n您今日抽到的是 ${lot["lot"]}\n" +
                    "本次签到您获得了${lot["increment"]}" + "枚硬币\n" +
                    "您总计签到${lot["accumulation"]}次,额外获得${lot["accumulation"]}枚硬币\n" +
                    "目前总计拥有${lot["total"]}" + "枚"
        }
        fun initDate(): String {
            GameMain.initData()
            return " 数据已初始化!"
        }
        fun getLot(qq: Long): String {
            val lot = GameMain.getLot(qq)
            return if (lot == null) " 硬币不够哦!\n单独抽签需要10枚硬币!"
            else " 抽签成功! 消耗10枚硬币\n您抽到的是 ${lot["lot"]} 获得了${lot["increment"]}枚硬币"
        }
        fun draw(qq: Long): String {
            val item = GameMain.draw(qq)
            JDBC.close()
            return if (item == null) " 您的硬币不够哦!"
            else " 抽奖成功! 消耗50硬币\n本次单抽获得: [${item.rarity}]${item.name}x1"
        }
        fun drawTenTimes(qq: Long): String {
            val items = GameMain.drawTenTimes(qq)
            JDBC.close()
            if (items == null) return "您的硬币不够哦! 一共需要500硬币"
            else {
                var str = ""
                for (e in items) str += "[${e.key.rarity}]${e.key.name}x${e.value} "
                return " 抽奖成功! 消耗500硬币\n本次十连抽获得: $str"
            }
        }
        fun getItemInfo(name: String): String {
            return if (Item.instances[name] == null) " 未能找到该物品"
            else "${Item.instances[name]!!.name}: ${Item.instances[name]!!.description}"
        }
        fun getInventory(qq: Long): String {
            if (Player.instances[qq] == null) return " 未检测到您的记录,签到可注册游戏!"
            else {
                var str = " 目前拥有${Player.instances[qq]!!.coins}枚硬币 以及..\n"
                val items = Player.instances[qq]!!.inventory
                if (items.isEmpty()) str += "暂时还没有其他物品哦!"
                else for (e in items) str += "[${e.key.rarity}]${e.key.name}x${e.value} "
                return str
            }
        }
    }
}
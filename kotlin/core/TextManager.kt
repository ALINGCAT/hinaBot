package core

import core.item.Item
import dao.JDBC
import tools.Key
import java.lang.Exception

class TextManager {
    companion object {
        const val HELP = "目前已有的一级指令:\n" +
                "公告 查看更新内容以及注意事项\n" +
                "签到 每天可以签到一次并抽签\n" +
                "查看库存 看看自己的仓库都有啥\n" +
                "关于 关于机器人项目的一些信息\n" +
                "#查看<物品名称> 查看某个物品的描述\n" +
                "#查询<功能名称> 查看某功能的详情以及二级指令\n" +
                "目前已有的功能有: 抽奖"
        private val info = mapOf(
                "抽奖" to " 每次抽奖需要消耗50硬币\n抽奖指令如下\n" +
                        "#抽奖 单抽一次\n" +
                        "#抽奖<数字> 抽奖多次"
        )
        fun getInfo(name: String): String {
            return if (info[name] != null) info[name].toString()
            else {
                var str = ""
                for (e in info) str += e.key + " "
                " 未找到该功能,目前已有的功能:\n$str"
            }
        }
        fun signIn(qq: Long): String {
            if (Player.instances[qq] == null) {
                Player.addNewPlayer(qq)
                JDBC.close()
                return " 签到成功!获得50枚硬币\n您是第一次签到,获得[S]猫猫招待卷"
            }
            val temp = Player.instances[qq]!!.signIn()
            JDBC.close()
            return if (temp == null) " 您今天已经签到过啦!"
            else " 签到成功!获得50枚硬币\n" +
                    "今天是${temp["day"]},获得${temp["reward"]}"
        }
        fun initDate(): String {
            Player.init()
            return " 数据已初始化!"
        }
        fun draw(qq: Long, Times: String = "1"): String {
            try {
                val times = Times.toInt()
                val items = Player.instances[qq]!!.draw(times)
                JDBC.close()
                val cost = times * 50
                return if (items == null) "您的硬币不够哦!\n抽${times}次需要${cost}枚硬币"
                else {
                    var str = ""
                    for (e in items) str += "[${e.key.rarity}]${e.key.name}x${e.value} "
                    " 抽奖成功! 消耗${cost}硬币\n本次${times}抽获得: $str"
                }
            } catch (e: Exception) { return info["抽奖"].toString() }
        }
        fun getItemInfo(name: String): String {
            return if (Item.instances[Key[name]] == null) " 未能找到该物品"
            else "${Item.instances[Key[name]]!!.name}: ${Item.instances[Key[name]]!!.description}"
        }
        fun getInventory(qq: Long): String {
            return if (Player.instances[qq] == null) " 未检测到您的记录,签到可注册游戏!"
            else {
                var str = ""
                val items = Player.instances[qq]!!.inventory
                if (items.isEmpty()) return " 目前拥有${Player.instances[qq]!!.coins}枚硬币"
                else for (e in items) str += "[${e.key.rarity}]${e.key.name}x${e.value} "
                " 目前拥有${Player.instances[qq]!!.coins}枚硬币 以及\n" + str
            }
        }
    }
}
package core
import core.item.Item
import tools.Key

class Reward {
    companion object {
        val perWeekSignIn = listOf(
                Item.instances[Key["猫猫招待卷"]]!!,
                Item.instances[Key["牛奶"]]!!,
                Item.instances[Key["猫眼石"]]!!,
                Item.instances[Key["牛奶"]]!!,
                Item.instances[Key["猫眼石"]]!!,
                Item.instances[Key["牛奶"]]!!,
                Item.instances[Key["猫眼石"]]!!
        )
    }
}
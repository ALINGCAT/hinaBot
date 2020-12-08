package core.item

class Other(Rarity: String, Name: String, Description: String)
    : Item(Rarity, Name, "其他", Description) {
    companion object {
        private var index = 20000
        fun init() {
            Other("S","猫猫招待卷",
                    "可用于猫猫召唤,兑换S级猫装备核心").toItem(true)
        }
    }
    override val id: Int = index++
}
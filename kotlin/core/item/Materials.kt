package core.item

class Materials(Rarity: String, Name: String, Description: String)
    : Item(Rarity, Name, "Materials", Description) {
    companion object {
        private var index = 10000
        fun init() {
            Materials("SS", "世界猫核心",
                    "海蓝色的猫猫状宝石,用于制作与重铸世界猫系列的装备").toItem(true)
            Materials("SS", "宇宙猫核心",
                    "深黑色嵌有光点的猫猫状宝石,用于制作与重铸宇宙猫系列的装备").toItem(true)
            Materials("SS", "蓬莱猫核心",
                    "彩色的猫猫状宝石,用于制作与重铸蓬莱猫系列的装备").toItem(true)
            Materials("S", "银河猫核心",
                    "银白色闪烁光明的猫猫状宝石,用于制作与重铸银河猫系列的装备").toItem(true)
            Materials("S", "以太猫核心",
                    "透明的猫猫状宝石,用于制作与重铸以太猫系列的装备").toItem(true)
            Materials("A", "地狱猫核心",
                    "火红色的猫猫状宝石,用于制作与重铸地狱猫系列的装备").toItem(true)
            Materials("A", "星光猫核心",
                    "闪烁的猫猫状宝石,用于制作与重铸星光猫系列的装备").toItem(true)
            Materials("B", "三角猫核心",
                    "尖锐的猫猫状宝石,用于制作与重铸三角猫系列的装备").toItem(true)
            Materials("C", "星砂",
                    "用途很广泛的材料").toItem(true)
            Materials("C", "星石碎片",
                    "星石的碎片,5片可以合成星石").toItem(true)

            Materials("S", "猫眼石",
                    "纹路像猫眼一样的石头").toItem()
        }
    }
    override val id: Int = index++
}
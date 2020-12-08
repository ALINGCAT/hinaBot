package core

import core.item.Item

class GameMain {
    companion object {
        fun init() {
            Item.init()
            Player.init()
        }
    }
}
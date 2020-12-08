package core.cat

import core.Player

abstract class Cat(Name: String, Type: String) {
    val type: String = Type
    val name = Name

    var love = HashMap<Player, Int>()
    var mood = 80
    var vitMax = 1200
    var vit = 1200
    var vitConsume = 10
    var heal = 1

    //per second damage = speed * damage / 10
    var hpMax = 100
    var hp = 100
    var speed = 10
    var atk = 0
    var def = 0

    var weapon = null
    var equipment = null
    var Boot = null
    var kit = null

    abstract fun tickling()
}
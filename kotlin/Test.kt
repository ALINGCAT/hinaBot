import Core.Item

fun main() {
    Item.init()
    Item.instances.get("牛乳")?.func?.invoke();
    Item.instances.get("呼符:旧日支配者")?.func?.invoke();
}
import java.io.File

class ConfigKit {
    companion object {
        fun getConfig(path:CharSequence):HashMap<String, String> {
            val table = HashMap<String,String>()
            File(path.toString()).forEachLine {
                val temp = it.split('=')
                table[temp[0]] = temp[1]
            }
            return table
        }
    }
}
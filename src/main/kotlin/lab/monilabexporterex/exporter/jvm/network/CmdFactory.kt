package lab.monilabexporterex.exporter.jvm.network

object CmdFactory {
    fun getCmdUtil(): CmdInterface {
        val os = System.getProperty("os.name").lowercase()
        return when {
            os.contains("linux") -> LinuxCmdUtil()
            os.contains("mac") -> MacCmdUtil()
            os.contains("win") -> WindowsCmdUtil()
            else -> throw UnsupportedOperationException("OS not supported: $os")
        }
    }
}

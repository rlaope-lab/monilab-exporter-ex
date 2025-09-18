package lab.monilabexporterex.exporter.jvm.network

abstract class AbstractCmdUtil : CmdInterface {

    protected abstract fun tcpConnectionsCommand(): Array<String>
    protected abstract fun tcpEstablishedCommand(): Array<String>
    protected abstract fun openSocketsCommand(): Array<String>

    override fun getTcpConnections() = runCommand(*tcpConnectionsCommand())
    override fun getTcpEstablished() = runCommand(*tcpEstablishedCommand())
    override fun getOpenSockets() = runCommand(*openSocketsCommand())

    private fun runCommand(vararg command: String): Int {
        return try {
            val process = ProcessBuilder(*command)
                .redirectErrorStream(true)
                .start()
            val output = process.inputStream.bufferedReader().readText().trim()
            process.waitFor()
            output.toIntOrNull() ?: 0
        } catch (_: Exception) {
            0
        }
    }
}

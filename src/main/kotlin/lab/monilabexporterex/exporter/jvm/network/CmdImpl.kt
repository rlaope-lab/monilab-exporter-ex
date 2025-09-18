package lab.monilabexporterex.exporter.jvm.network

class LinuxCmdUtil : AbstractCmdUtil() {
    override fun tcpConnectionsCommand() = arrayOf("sh", "-c", "netstat -nat | wc -l")
    override fun tcpEstablishedCommand() = arrayOf("sh", "-c", "netstat -nat | grep ESTABLISHED | wc -l")
    override fun openSocketsCommand() = arrayOf("sh", "-c", "lsof -i | wc -l")
}

class MacCmdUtil : AbstractCmdUtil() {
    override fun tcpConnectionsCommand() = arrayOf("sh", "-c", "netstat -nat | wc -l")
    override fun tcpEstablishedCommand() = arrayOf("sh", "-c", "netstat -nat | grep ESTABLISHED | wc -l")
    override fun openSocketsCommand() = arrayOf("sh", "-c", "lsof -i | wc -l")
}

class WindowsCmdUtil : AbstractCmdUtil() {
    override fun tcpConnectionsCommand() = arrayOf("cmd", "/c", "netstat -an | find /c \"TCP\"")
    override fun tcpEstablishedCommand() = arrayOf("cmd", "/c", "netstat -an | find /c \"ESTABLISHED\"")
    override fun openSocketsCommand() = arrayOf("cmd", "/c", "netstat -an | find /c \"\"")
}

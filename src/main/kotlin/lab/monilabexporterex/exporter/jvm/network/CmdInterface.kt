package lab.monilabexporterex.exporter.jvm.network

interface CmdInterface {
    fun getTcpConnections(): Int
    fun getTcpEstablished(): Int
    fun getOpenSockets(): Int
}

package lab.monilabexporterex.exporter

import lab.monilabexporterex.exporter.jvm.application.DefaultApplicationExporter
import lab.monilabexporterex.exporter.jvm.cpu.DefaultCpuExporter
import lab.monilabexporterex.exporter.jvm.gc.DefaultGcExporter
import lab.monilabexporterex.exporter.jvm.memory.DefaultMemoryExporter
import lab.monilabexporterex.exporter.jvm.network.DefaultNetworkExporter
import lab.monilabexporterex.exporter.jvm.thread.DefaultThreadExporter
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.Socket
import java.net.URL

@SpringBootTest
class ExporterTest {

    @Autowired
    lateinit var memoryExporter: DefaultMemoryExporter

    @Autowired
    lateinit var gcExporter: DefaultGcExporter

    @Autowired
    lateinit var threadExporter: DefaultThreadExporter

    @Autowired
    lateinit var cpuExporter: DefaultCpuExporter

    @Autowired
    lateinit var applicationExporter: DefaultApplicationExporter

    @Autowired
    lateinit var networkExporter: DefaultNetworkExporter

    private val log = LoggerFactory.getLogger(ExporterTest::class.java)

    @Test
    fun printJvmMetrics() {
        repeat(5) { i ->
            val memory = memoryExporter.getMemoryInfo()
            val gc = gcExporter.getGcInfo()
            val threads = threadExporter.getThreadInfo()
            val cpu = cpuExporter.getCpuInfo()
            val application = applicationExporter.getApplicationInfo()
            val network = networkExporter.getNetworkInfo()

            // cpu 부하
            var sum = 0L
            for (num in 1..1_000_000) {
                sum += num
            }

            // network 부하
            try {
                Socket("localhost", 9999).use { socket ->
                    val output: OutputStream = socket.getOutputStream()
                    output.write("ping\n".toByteArray())
                    output.flush()
                }
            } catch (_: Exception) {
            }

            log.info("==== Sample #$i ====")

            log.info(
                "[Memory] used={} max={} committed={} eden={} survivor={} old={} bufferPool={} maxDirect={}",
                memory.used, memory.max, memory.committed,
                memory.eden, memory.survivor, memory.old,
                memory.bufferPoolUsed, memory.maxDirectMemorySize
            )

            log.info(
                "[GC] count={} time={}ms pause={}ms allocRate={}B/s liveData={} strategy={}",
                gc.count, gc.time, gc.pause, gc.allocationRate, gc.liveDataSize, gc.gcStrategy
            )

            log.info(
                "[Threads] count={} daemon={} peak={} deadlocked={} cpuTime={} states={}",
                threads.count, threads.daemonCount, threads.peakCount,
                threads.deadlockedCount, threads.cpuTime, threads.states
            )

            log.info(
                "[CPU] processUsage={} systemUsage={} uptime={} startTime={} uptime={} openFds={}",
                cpu.processUsage, cpu.systemUsage, cpu.uptime,
                cpu.startTime, cpu.uptime, cpu.openFds
            )

            log.info(
                "[Application] httpRequestsCount={} httpLatency={} dbConnectionsActive={} dbConnectionsActive={} dbConnectionsMax={} queueTasksPending={} customMetrics={}",
                application.httpRequestsCount, application.httpLatency, application.dbConnectionsActive,
                application.dbConnectionsActive, application.dbConnectionsMax, application.queueTasksPending,
                application.customMetrics
            )

            log.info(
                "[Network] bytesSent={} bytesReceived={} preferIPv4={} openSockets={} tcpConnections={} tcpEstablished={}",
                network.bytesSent, network.bytesReceived, network.preferIPv4,
                network.openSockets, network.tcpConnections, network.tcpEstablished
            )

            Thread.sleep(1000) // 1초 간격
        }
    }
}

package lab.monilabexporterex.exporter.jvm.application

import com.zaxxer.hikari.HikariDataSource
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.search.Search
import lab.monilabexporterex.exporter.data.JvmMonitoringData
import org.springframework.stereotype.Component
import java.util.concurrent.BlockingQueue
import java.util.concurrent.TimeUnit

@Component
class DefaultApplicationExporter(
    private val meterRegistry: MeterRegistry,
    private val dataSource: HikariDataSource? = null,
    private val taskQueue: BlockingQueue<*>? = null
) {
    fun getApplicationInfo(): JvmMonitoringData.Application {
        val httpRequestsTimer = meterRegistry.find("http.server.requests").timer()

        val httpRequestsCount = httpRequestsTimer?.count() ?: 0L

        val httpLatency = httpRequestsTimer?.takeIf { it.count() > 0 }
            ?.let { it.totalTime(TimeUnit.MILLISECONDS) / it.count() }
            ?: 0.0

        val dbConnectionsActive = try {
            dataSource?.hikariPoolMXBean?.activeConnections ?: 0
        } catch (ex: Exception) {
            0
        }

        val dbConnectionsMax = try {
            dataSource?.hikariConfigMXBean?.maximumPoolSize ?: 0
        } catch (ex: Exception) {
            0
        }
        val queueTasksPending = taskQueue?.size ?: 0

        return JvmMonitoringData.Application(
            httpRequestsCount = httpRequestsCount,
            httpLatency = httpLatency,
            dbConnectionsActive = dbConnectionsActive,
            dbConnectionsMax = dbConnectionsMax,
            queueTasksPending = queueTasksPending,
            customMetrics = emptyMap()
        )
    }
}

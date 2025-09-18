package lab.monilabexporterex.exporter.jvm.cpu

import com.sun.management.OperatingSystemMXBean
import com.sun.management.UnixOperatingSystemMXBean
import java.lang.management.ManagementFactory
import org.springframework.stereotype.Component
import lab.monilabexporterex.exporter.data.JvmMonitoringData

@Component
class DefaultCpuExporter {
    fun getCpuInfo(): JvmMonitoringData.Cpu {
        val osBean = ManagementFactory.getOperatingSystemMXBean() as OperatingSystemMXBean
        val runtime = ManagementFactory.getRuntimeMXBean()

        val unixBean = osBean as? UnixOperatingSystemMXBean
        val openFds = unixBean?.openFileDescriptorCount ?: -1

        return JvmMonitoringData.Cpu(
            processUsage = osBean.processCpuLoad,
            systemUsage = osBean.systemCpuLoad,
            uptime = runtime.uptime,
            startTime = runtime.startTime,
            loadAverage = osBean.systemLoadAverage,
            openFds = openFds
        )
    }
}

package ru.mikhail.lab4_backend.configs

import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.mikhail.lab4_backend.mbeans.ClickStats
import ru.mikhail.lab4_backend.mbeans.DotStats
import java.lang.management.ManagementFactory
import javax.management.MBeanServer
import javax.management.ObjectName

@Configuration
class MBeanConfig {

    @Bean
    fun dotStats(): DotStats = DotStats()

    @Bean
    fun clickStats(): ClickStats = ClickStats()

    @Bean
    fun registerMBeans(): CommandLineRunner {
        return CommandLineRunner {
            val mbs: MBeanServer = ManagementFactory.getPlatformMBeanServer()
            mbs.registerMBean(DotStats(), ObjectName("lab4:type=DotStats"))
            mbs.registerMBean(ClickStats(), ObjectName("lab4:type=ClickStats"))
        }
    }
}
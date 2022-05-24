package training.hipster.compose.config

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.DisposableBean
import org.springframework.beans.factory.InitializingBean
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.containers.output.Slf4jLogConsumer
import java.util.Collections
import kotlin.math.roundToLong

class MongoDbTestContainer : InitializingBean, DisposableBean {

    companion object {
        private val memoryInBytes = (1024 * 1024 * 1024 * 0.6).roundToLong()
        private val memorySwapInBytes = (1024 * 1024 * 1024 * 0.8).roundToLong()
        private val nanoCpu = (1_000_000_000L * 0.1).roundToLong()
        private val log = LoggerFactory.getLogger(MongoDbTestContainer::class.java)

        @JvmStatic
        private var mongodbContainer: MongoDBContainer = MongoDBContainer("mongo:4.4.12")
            .withTmpFs(Collections.singletonMap("/testtmpfs", "rw"))
            .withCommand(
                "--nojournal --wiredTigerCacheSizeGB 0.25 --wiredTigerCollectionBlockCompressor none --slowOpSampleRate 0 --setParameter ttlMonitorEnabled=false --setParameter diagnosticDataCollectionEnabled=false --setParameter logicalSessionRefreshMillis=6000000 --setParameter enableFlowControl=false --setParameter oplogFetcherUsesExhaust=false --setParameter disableResumableRangeDeleter=true --setParameter enableShardedIndexConsistencyCheck=false --setParameter enableFinerGrainedCatalogCacheRefresh=false --setParameter readHedgingMode=off --setParameter loadRoutingTableOnStartup=false --setParameter rangeDeleterBatchDelayMS=2000000 --setParameter skipShardingConfigurationChecks=true --setParameter syncdelay=3600"
            )
            .withCreateContainerCmdModifier { cmd ->
                cmd.hostConfig!!.withMemory(memoryInBytes).withMemorySwap(memorySwapInBytes)
                    .withNanoCPUs(nanoCpu)
            }
            .withLogConsumer(Slf4jLogConsumer(log)).withReuse(true)
    }

    override fun destroy() {
        if (null != mongodbContainer && mongodbContainer.isRunning) {
            mongodbContainer.stop()
        }
    }

    override fun afterPropertiesSet() {
        if (!mongodbContainer.isRunning) {
            mongodbContainer.start()
        }
    }

    fun getMongoDBContainer() = mongodbContainer
}

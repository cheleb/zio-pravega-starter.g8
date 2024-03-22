package $package$

import zio.*
import zio.pravega.*
import zio.pravega.admin.*

import io.pravega.client.tables.KeyValueTableConfiguration
import io.pravega.client.stream.StreamConfiguration
import io.pravega.client.stream.ScalingPolicy

object CreateResourcesExample extends ZIOAppDefault {

  private val streamConfiguration = StreamConfiguration.builder
    .scalingPolicy(ScalingPolicy.fixed(8))
    .build

  val tableConfig = KeyValueTableConfiguration
    .builder()
    .partitionCount(2)
    .primaryKeyLength(4)
    .build()

  private val program = for {
    scopeCreated <- PravegaStreamManager.createScope("a-scope")

    _ <- ZIO.logInfo(s"Scope created \$scopeCreated")

    streamCreated <- PravegaStreamManager.createStream(
      "a-scope",
      "a-stream",
      streamConfiguration
    )

    _ <- ZIO.logInfo(s"Stream created: \$streamCreated")

    tableCreated <- PravegaTableManager.createTable(
      "a-scope",
      "a-table",
      tableConfig
    )

    _ <- ZIO.logInfo(s"Table created: \$tableCreated")

  } yield ()

  override def run =
    program
      .provide(
        Scope.default,
        PravegaClientConfig.live,
        PravegaStreamManager.live,
        PravegaTableManager.live
      )

}

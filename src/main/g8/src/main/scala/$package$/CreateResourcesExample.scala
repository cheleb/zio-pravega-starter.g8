package $package$

import zio._
import zio.pravega._

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
    _ <- PravegaAdmin.createScope("a-scope")
    _ <- PravegaAdmin.createStream(
      "a-scope",
      "a-stream",
      streamConfiguration
    )
    _ <- PravegaAdmin.createTable(
      "a-scope",
      "a-table",
      tableConfig
    )
  } yield ()

  override def run: ZIO[Any, Throwable, Unit] =
    program
      .provide(
        Scope.default,
        PravegaAdmin.live(PravegaClientConfig.default)
      )

}

package $package$

import zio._
import zio.pravega._

import io.pravega.client.stream.StreamConfiguration
import io.pravega.client.stream.ScalingPolicy

object CreateResourcesExample extends ZIOAppDefault {

  private val streamConfiguration = StreamConfiguration.builder
    .scalingPolicy(ScalingPolicy.fixed(8))
    .build

  private val program = for {
    _ <- PravegaAdmin.createScope("a-scope")
    _ <- PravegaAdmin.createStream(
      "a-scope",
      "a-stream",
      streamConfiguration
    )
  } yield ()

  override def run: ZIO[Any, Throwable, Unit] =
    program
      .provide(
        Scope.default,
        PravegaAdmin.live(PravegaClientConfig.default)
      )

}

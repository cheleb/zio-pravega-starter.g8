package $package$

import zio.*
import zio.pravega.*

import io.pravega.client.stream.impl.UTF8StringSerializer
import zio.pravega.admin.PravegaStreamManager
import zio.pravega.admin.PravegaReaderGroupManager

object StreamReadExample extends ZIOAppDefault {

  val stringReaderSettings =
    ReaderSettingsBuilder()
      .withSerializer(new UTF8StringSerializer)

  private val program = for {
    _ <- PravegaReaderGroupManager.createReaderGroup(
      "a-reader-group",
      "a-stream"
    )
    stream = PravegaStream.stream(
      "a-reader-group",
      stringReaderSettings
    )
    _ <- stream
      .tap(m => ZIO.debug(m.toString()))
      .take(10)
      .runFold(0)((s, _) => s + 1)

  } yield ()

  override def run =
    program.provide(
      Scope.default,
      PravegaClientConfig.live,
      PravegaReaderGroupManager.live("a-scope"),
      PravegaStream.fromScope(
        "a-scope",
        PravegaClientConfig.default
      )
    )

}

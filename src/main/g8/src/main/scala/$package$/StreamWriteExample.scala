package $package$

import zio.*
import zio.pravega.*
import zio.stream.*

import io.pravega.client.stream.impl.UTF8StringSerializer

/**
  * Example of writing to a Pravega stream.
  */
object StreamWriteExample extends ZIOAppDefault:

  // Pravega client configuration: localhost:9090
  val clientConfig = PravegaClientConfig.default

  val stringWriterSettings =
    WriterSettingsBuilder()
      .eventWriterConfigBuilder(_.enableLargeEvents(true))
      .withSerializer(new UTF8StringSerializer)

  // Create a stream of strings from a to b
  def testStream(a: Int, b: Int): ZStream[Any, Nothing, String] =
    ZStream
      .fromIterable(a to b)
      .map(i => f"\$i%04d_name \$i")

  // Main program
  val program = for

    _ <- ZIO.log("StreamWriteExample")
    
    sink = PravegaStream.sink("$demoStream$", stringWriterSettings)
    
    _ <- testStream(1, 10)
      .tap(p => ZIO.debug(p.toString()))
      .run(sink)
  yield ()

  override def run =
    program.provide(
      Scope.default,
      PravegaStream.fromScope("$demoScope$", clientConfig)
    )

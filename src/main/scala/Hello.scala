package wow.doge

import zio.kafka.consumer._
import zio.kafka.producer._
import zio.kafka.serde._
import org.apache.kafka.clients.producer.ProducerRecord
import zio.ZLayer
import zio.Task

import zio.ZIOAppDefault
import zio.Console._
import zio.kafka.consumer.Consumer.OffsetRetrieval
import zio.kafka.consumer.Consumer.AutoOffsetStrategy
import zio.Duration
import scala.concurrent.duration._

val consumerSettings: ConsumerSettings =
  ConsumerSettings(List("localhost:9092"))
    .withGroupId("group-1")
    .withOffsetRetrieval(OffsetRetrieval.Auto(AutoOffsetStrategy.Earliest))

val producerSettings: ProducerSettings = ProducerSettings(
  List("localhost:9092")
)

val consumerAndProducer =
  ZLayer.scoped(Consumer.make(consumerSettings)) ++
    ZLayer.scoped(Producer.make(producerSettings))

val consumeProduceStream = Consumer
  .subscribeAnd(Subscription.topics("events-4"))
  .plainStream(Serde.string, Serde.string)
  .groupedWithin(5, Duration.fromScala(10.seconds))
  .mapZIO(items =>
    Task.attempt(
      // items.foreach(item =>
      // (s"key: ${item.record.key()}, value: ${item.record.value()}")
      // )
      println(items.map(i => (i.record.key, i.record.value)).toString)
    )
  )
  // .map { record =>
  //   val key: Int = record.record.key()
  //   val value: Long = record.record.value()
  //   val newValue: String = value.toString

  //   val producerRecord: ProducerRecord[Int, String] =
  //     new ProducerRecord("events-2", key, newValue)
  //   (producerRecord, record.offset)
  // }
  .runDrain
  .provideSomeLayer(consumerAndProducer)

object MyApp extends ZIOAppDefault {
  def run =
    for {
      _ <- consumeProduceStream
    } yield ()
}

// @main
// def run = println("hello")

// object Hello {
//   def main(args: Array[String]) = println("hello world")
// }

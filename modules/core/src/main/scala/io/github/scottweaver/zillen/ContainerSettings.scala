package io.github.scottweaver.zillen

import zio._
import scala.annotation.nowarn

final case class ContainerSettings[-A](
  inspectContainerPromiseSettings: ReadyCheck.ContainerRunning,
  readyCheckSettings: ReadyCheck.ContainerReady
) { self =>

  def withInspectContainerPromiseSettings(
    inspectContainerPromiseSettings: ReadyCheck.ContainerRunning
  ): ContainerSettings[A] =
    copy(inspectContainerPromiseSettings = inspectContainerPromiseSettings)

  def withReadyCheckSettings(
    readyCheckSettings: ReadyCheck.ContainerReady
  ): ContainerSettings[A] =
    copy(readyCheckSettings = readyCheckSettings)

  def as[B]: ContainerSettings[B] = self.asInstanceOf[ContainerSettings[B]]
}

object ContainerSettings {
  val defaultPromisedSettings   = ReadyCheck.ContainerRunning(250.millis, 5)
  val defaultReadyCheckSettings = ReadyCheck.ContainerReady(250.millis, 5)

  def default(builder: ContainerSettings[Any] => ContainerSettings[Any] = identity) =
    ZLayer.succeed {
      builder(
        ContainerSettings[Any](
          defaultPromisedSettings,
          defaultReadyCheckSettings
        )
      )
    }

  @nowarn
  def layerFromDefault[A: Tag](builder: ContainerSettings[A] => ContainerSettings[A]) =
    ZLayer.fromZIO {
      ZIO.serviceWith[ContainerSettings[A]](cs => builder(cs.as[A]))
    }
}

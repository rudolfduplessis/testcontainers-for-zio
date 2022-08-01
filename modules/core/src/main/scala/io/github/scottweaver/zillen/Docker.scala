package io.github.scottweaver.zillen

import netty._

object Docker extends ContainerOps with ModelOps with FailureOps with ReadyCheckOps {

  object cmd extends CommandOps

  def layer(
    dockerSettingsBuilder: DockerSettings => DockerSettings = identity
  ) =
    DockerSettings.default(
      dockerSettingsBuilder
    ) >+> (nettyBootstrapLayer >>> NettyRequestHandler.layer >>> Interpreter.layer) >+>
      Network.layer ++ ReadyCheck.layer
}
package e

import _root_.zio.ZIO
import e.scala.E

package object zio {
  type EIO[+A]      = ZIO[Any, E, A]
  val EIO: ZIO.type = ZIO
}

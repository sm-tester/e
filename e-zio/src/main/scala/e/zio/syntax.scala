package e.zio

import e.scala.{E, EException}
import zio.Task

object syntax {
  implicit class EIOSyntaxE(private val e: E) {
    def toEIO[A]: EIO[A] = EIO.fail(e)
  }

  implicit class EIOSyntaxA[+A](private val a: A) {
    def toEIO: EIO[A] = EIO.effectTotal(a)
  }

  implicit class TaskSyntax[A](private val task: Task[A]) {
    def toEIO: EIO[A] =
      task.mapError {
        case EException(e) => e
        case cause         => E(cause = Some(cause))
      }
  }
}

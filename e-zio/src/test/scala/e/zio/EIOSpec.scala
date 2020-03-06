package e.zio

import e.scala.E
import e.zio.syntax._
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import zio.Exit.{Failure, Success}
import zio.internal.Platform
import zio.{Cause, ZIO, Runtime => ZIORuntime}

class EIOSpec extends AnyWordSpec with Matchers {
  def divide(a: Int, b: Int): EIO[Int] =
    if (b == 0) {
      E("divide-by-zero", "Cannot divide by 0!", data = Map("input" -> a.toString)).toEIO
    } else {
      (a / b).toEIO
    }

  implicit val runtime: ZIORuntime[Any] = ZIORuntime((), Platform.default)

  def testFailure[R, A](actual: ZIO[R, E, A], expected: E)(implicit runtime: ZIORuntime[R]): Unit = {
    runtime.unsafeRunSync(actual) shouldBe Failure(Cause.fail(expected))
  }

  def testSuccess[R, A](actual: ZIO[R, E, A], expected: A)(implicit runtime: ZIORuntime[R]): Unit = {
    runtime.unsafeRunSync(actual) shouldBe Success(expected)
  }

  "EIO" should {
    "fail with E" in {
      testFailure(divide(3, 0), E("divide-by-zero", "Cannot divide by 0!", data = Map("input" -> "3")))
    }

    "succeed with a value" in {
      testSuccess(divide(6, 2), 3)
    }
  }
}

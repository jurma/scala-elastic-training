package routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import model.Student
import service.StudentService

import scala.util.{Failure, Success}

class StudentRoutes(service: StudentService) extends JsonSupport {
  val route: Route =
    pathPrefix("students") {
      pathEndOrSingleSlash {
        get {
          onComplete(service.listStudents()) {
            case Success(students) => complete(StatusCodes.OK -> students)
            case Failure(ex) => complete(StatusCodes.InternalServerError -> s"${ex.getMessage}")
          }
        } ~
          post {
            entity(as[Student]) { student =>
              onComplete(service.createStudent(student)) {
                case Success(Right(created)) => complete(StatusCodes.Created -> created)
                case Success(Left(error)) => complete(StatusCodes.BadRequest -> error)
                case Failure(ex) => complete(StatusCodes.InternalServerError -> s"${ex.getMessage}")
              }
            }
          }
      } ~
        path(IntNumber) { id =>
          get {
            onComplete(service.getStudent(id)) {
              case Success(Right(student)) => complete(StatusCodes.OK -> student)
              case Success(Left(error)) => complete(StatusCodes.NotFound -> error)
              case Failure(ex) => complete(StatusCodes.InternalServerError -> s"${ex.getMessage}")
            }
          } ~
            put {
              entity(as[Student]) { student =>
                onComplete(service.updateStudent(id, student)) {
                  case Success(Right(updated)) => complete(StatusCodes.OK -> updated)
                  case Success(Left(error)) => complete(StatusCodes.BadRequest -> error)
                  case Failure(ex) => complete(StatusCodes.InternalServerError -> s"${ex.getMessage}")
                }
              }
            } ~
            delete {
              onComplete(service.deleteStudent(id)) {
                case Success(Right(_)) => complete(StatusCodes.NoContent)
                case Success(Left(error)) => complete(StatusCodes.NotFound -> error)
                case Failure(ex) => complete(StatusCodes.InternalServerError -> s"${ex.getMessage}")
              }
            }
        }
    }
}

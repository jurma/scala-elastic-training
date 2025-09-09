package service

import model.{Student, ErrorResponse}
import db.StudentRepository

import scala.concurrent.{ExecutionContext, Future}

class StudentService(repo: StudentRepository)(implicit ec: ExecutionContext) {
  private type ValidationResult[A] = Either[ErrorResponse, A]

  private val validateName: Student => ValidationResult[Student] = student =>
    Either.cond(student.name.nonEmpty, student, ErrorResponse("Name cannot be empty"))

  private val validateEmail: Student => ValidationResult[Student] = student =>
    Either.cond(student.email.contains("@"), student, ErrorResponse("Invalid email"))

  private val validateAge: Student => ValidationResult[Student] = student =>
    Either.cond(
      student.age >= 0 && student.age <= 120,
      student,
      ErrorResponse("Invalid age")
    )

  private def validate(student: Student): ValidationResult[Student] =
    for {
      _ <- validateName(student)
      _ <- validateEmail(student)
      valid <- validateAge(student)
    } yield valid

  def createStudent(student: Student): Future[Either[ErrorResponse, Student]] =
    validate(student) match {
      case Left(error) => Future.successful(Left(error))
      case Right(validStudent) =>
        repo.create(validStudent).map {
          case Some(created) => Right(created)
          case None => Left(ErrorResponse("Failed to create student"))
        }
    }

  def getStudent(id: Int): Future[Either[ErrorResponse, Student]] =
    repo.read(id).map {
      case Some(student) => Right(student)
      case None => Left(ErrorResponse(s"Student with id $id not found"))
    }

  def updateStudent(id: Int, student: Student): Future[Either[ErrorResponse, Student]] =
    validate(student).fold(
      error => Future.successful(Left(error)),
      validStudent => repo.update(id, validStudent).map {
        case Some(updated) => Right(updated)
        case None => Left(ErrorResponse(s"Student with id $id not found"))
      }
    )

  def deleteStudent(id: Int): Future[Either[ErrorResponse, Unit]] =
    repo.delete(id).map { deleted =>
      if (deleted) Right(()) else Left(ErrorResponse(s"Student with id $id not found"))
    }

  def listStudents(): Future[List[Student]] = repo.list()
}

package db

import model.Student

import scala.collection.mutable
import scala.concurrent.{ExecutionContext, Future}

class InMemoryDBRepository(implicit ec: ExecutionContext) extends StudentRepository {
  private val students = mutable.Map[Int, Student]()

  def create(student: Student): Future[Option[Student]] = Future {
    students.get(student.id).map { _ =>
      students.update(student.id, student)
      student
    }
  }

  def read(id: Int): Future[Option[Student]] = Future {
    students.get(id)
  }

  def update(id: Int, student: Student): Future[Option[Student]] = Future {
    students.get(id).map { _ =>
      students.put(id, student)
      student
    }
  }

  def delete(id: Int): Future[Boolean] = Future {
    students.remove(id).isDefined
  }

  def list(): Future[List[Student]] = Future {
    students.values.toList
  }
}

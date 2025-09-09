package db

import model.Student

import scala.concurrent.Future

trait StudentRepository {
  def create(student: Student): Future[Option[Student]]
  def read(id: Int): Future[Option[Student]]
  def update(id: Int, student: Student): Future[Option[Student]]
  def delete(id: Int): Future[Boolean]
  def list(): Future[List[Student]]
}

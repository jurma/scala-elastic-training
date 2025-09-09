package db

import com.sksamuel.elastic4s.ElasticDsl._
import com.sksamuel.elastic4s.{ElasticClient, Indexable}
import model.Student
import model.StudentJsonProtocol._
import spray.json._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

class ElasticsearchStudentRepository(client: ElasticClient, indexName: String = "students")(implicit ec: ExecutionContext) extends StudentRepository {
  // Elastic4s needs an Indexable for Student to serialize to JSON
  implicit val studentIndexable: Indexable[Student] = (student: Student) => student.toJson.compactPrint

  override def create(student: Student): Future[Option[Student]] = {
    client.execute {
      indexInto(indexName)
        .id(student.id.toString)
        .doc(student)
        .refreshImmediately
    }.map(resp => Option.when(resp.isSuccess)(student))
  }

  override def read(id: Int): Future[Option[Student]] = {
    client.execute {
      get(indexName, id.toString)
    }.map { resp =>
      Option.when(resp.isSuccess)(resp.result.sourceAsString)
        .flatMap(s => Try(s.parseJson.convertTo[Student]).toOption)
    }
  }

  override def update(id: Int, student: Student): Future[Option[Student]] = {
    client.execute {
      updateById(indexName, id.toString)
        .doc(student)
        .refreshImmediately
    }.map(resp => Option.when(resp.isSuccess)(student))
  }

  override def delete(id: Int): Future[Boolean] = {
    client.execute {
      deleteById(indexName, id.toString)
        .refreshImmediately
    }.map(_.isSuccess)
  }

  override def list(): Future[List[Student]] = {
    client.execute {
      search(indexName)
        .matchAllQuery()
        .size(1000)
    }.map { resp =>
      if (resp.isSuccess) {
        resp.result.hits.hits.flatMap(hit => Try(hit.sourceAsString.parseJson.convertTo[Student]).toOption).toList
      } else List.empty
    }
  }
}

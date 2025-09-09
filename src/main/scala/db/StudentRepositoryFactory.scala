package db

import com.typesafe.config.ConfigFactory
import com.sksamuel.elastic4s.ElasticClient
import com.sksamuel.elastic4s.http.JavaClient
import com.sksamuel.elastic4s.ElasticProperties

import scala.concurrent.ExecutionContext.Implicits.global

object StudentRepositoryFactory {
  private val config = ConfigFactory.load()
  private val persistenceType = config.getString("persistence.type")

  def getRepository: StudentRepository = {
    persistenceType match {
      case "inmemory" => new InMemoryDBRepository()
      case "elasticsearch" =>
        val esConfig = config.getConfig("persistence.elasticsearch")
        val host = esConfig.getString("host")
        val port = esConfig.getInt("port")
        val scheme = esConfig.getString("scheme")
        val index = esConfig.getString("index")
        val url = s"$scheme://$host:$port"
        val client = ElasticClient(JavaClient(ElasticProperties(url)))
        new ElasticsearchStudentRepository(client, index)
      case _ => throw new IllegalArgumentException(s"Unknown persistence type: $persistenceType")
    }
  }
}

package routes

import spray.json.{DefaultJsonProtocol, RootJsonFormat}
import model.{ErrorResponse, Student}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val studentFormat: RootJsonFormat[Student] = jsonFormat5(Student)
  implicit val errorFormat: RootJsonFormat[ErrorResponse] = jsonFormat1(ErrorResponse)
}

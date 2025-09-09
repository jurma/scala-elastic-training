package model

import spray.json.{DefaultJsonProtocol, RootJsonFormat}

object StudentJsonProtocol extends DefaultJsonProtocol {
  implicit val studentFormat: RootJsonFormat[Student] = jsonFormat5(Student)
  implicit val errorResponseFormat: RootJsonFormat[ErrorResponse] = jsonFormat1(ErrorResponse)
}

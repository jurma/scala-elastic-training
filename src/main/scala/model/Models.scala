package model

case class Student(
  id: Int,
  name: String,
  email: String,
  age: Int,
  enrolled: Boolean
)

case class ErrorResponse(message: String)

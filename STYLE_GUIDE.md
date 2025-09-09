### Scala Functional Code Style Guide

When generating Scala code, particularly for handling `Either` or similar data types in a functional way, adhere to the following formatting rules:

1.  **Chain `fold` on a New Line:** The `.fold` method call should be placed on a new line following the function call that returns the `Either`.
2.  **Indent the `fold` Call:** The new line with the `.fold` call should be indented one level deeper than the preceding line.
3.  **Keep `fold` Parameters on a Single Line:** The two lambda functions passed as arguments to `fold` should be kept on the same line for conciseness.

#### Example:

```scala
// Correct formatting
complete(
  service.createStudent(student)
    .fold(err => StatusCodes.BadRequest -> err, s => StatusCodes.Created -> s)
)

// Incorrect formatting
service.createStudent(student).fold(err => StatusCodes.BadRequest -> err, s => StatusCodes.Created -> s)

// Incorrect formatting
service.createStudent(student).fold(
  err => StatusCodes.BadRequest -> err,
  s   => StatusCodes.Created -> s
)
```


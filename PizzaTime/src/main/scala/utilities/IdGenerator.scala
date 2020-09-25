package utilities

object IdGenerator{
  var counter: Int = 0

  def nextId: Int = {
    counter += 1
    counter
  }
}

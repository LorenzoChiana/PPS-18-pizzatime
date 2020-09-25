package utilities

import alice.tuprolog._

object Scala2P {

  def extractTerm(t:Term, i:Integer): Term = t.asInstanceOf[Struct].getArg(i).getTerm

  implicit def stringToTerm(s: String): Term = Term.createTerm(s)
  implicit def seqToTerm[T](s: Seq[T]): Term = s.mkString("[",",","]")

  def prolog(clauses: String*): Term => Seq[Option[Point]] = {
    goal => new Iterable[Option[Point]]{
      val engine = new Prolog
      engine.setTheory(new Theory(clauses mkString " "))

      override def iterator: Iterator[Option[Point]] = new Iterator[Option[Point]]{
        var solution: Option[SolveInfo] = Some(engine.solve(goal))

        override def hasNext: Boolean = solution.isDefined && (solution.get.isSuccess || solution.get.hasOpenAlternatives)

        override def next(): Option[Point] =
          try {
            if(solution.isDefined) Some(Point(solution.get.getTerm("X").toString.toInt, solution.get.getTerm("Y").toString.toInt)) else None
          } finally {
            try {
              solution = Some(engine.solveNext)
            } catch {
              case _: NoMoreSolutionException => solution = None
            }
          }
      }
    }.toSeq
  }
}

object TryScala2P extends App {
  import Scala2P._

  val engine: Term => Seq[Option[Point]] = prolog("""
    moveAlt(X1,Y1,X2,Y2) :- X2 is X1+1, Y2 is Y1.
    moveAlt(X1,Y1,X2,Y2) :- X2 is X1-1, Y2 is Y1.
    moveAlt(X1,Y1,X2,Y2) :- Y2 is Y1+1, X2 is X1.
    moveAlt(X1,Y1,X2,Y2) :- Y2 is Y1-1, X2 is X1.
  """)

  engine("moveAlt(1,1,X,Y)").foreach(a=> println(a))
}


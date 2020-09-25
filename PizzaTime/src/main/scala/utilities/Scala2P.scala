package utilities

import alice.tuprolog._

object Scala2P {

  def extractTerm(t:Term, i:Integer): Term =
    t.asInstanceOf[Struct].getArg(i).getTerm

  implicit def stringToTerm(s: String): Term = Term.createTerm(s)
  implicit def seqToTerm[T](s: Seq[T]): Term = s.mkString("[",",","]")

  def mkPrologEngine(clauses: String*): Term => Stream[Option[Term]] = {
    goal => new Iterable[Option[Term]]{
      val engine = new Prolog
      engine.setTheory(new Theory(clauses mkString " "))

      override def iterator: Iterator[Option[Term]] = new Iterator[Option[Term]]{
        var solution: Option[SolveInfo] = Some(engine.solve(goal))

        override def hasNext: Boolean = solution.isDefined && (solution.get.isSuccess || solution.get.hasOpenAlternatives)

        override def next(): Option[Term] =
          try {
            if(solution.isDefined) Some(solution.get.getSolution) else None
          } finally {
            try {
              solution = Some(engine.solveNext)
            } catch {
              case e: NoMoreSolutionException => solution = None
            }
          }
      }
    }.toStream
  }
}

//PROVA => va in Enemy
object TryScala2P extends App {
  import Scala2P._

  val engine: Term => Stream[Option[Term]] = mkPrologEngine("""
    moveAlt(X1,Y1,X2,Y2) :- X2 is X1+1, Y2 is Y1.
    moveAlt(X1,Y1,X2,Y2) :- X2 is X1-1, Y2 is Y1.
    moveAlt(X1,Y1,X2,Y2) :- Y2 is Y1+1, X2 is X1.
    moveAlt(X1,Y1,X2,Y2) :- Y2 is Y1-1, X2 is X1.
  """)


  //Solution: moveAlt(1,1,2,1)
  //Solution: moveAlt(1,1,0,1)
  //Solution: moveAlt(1,1,1,2)
  //Solution: moveAlt(1,1,1,0)
  engine("moveAlt(1,1,X,Y)").foreach(a=> println(a))
}


import PizzaTimeManager.GameManager
import PizzaTimeManager.observers.ViewObserver

class Main {
   def start(): Unit = {
    initializeGame()
  }

  private def initializeGame(){
    val observers: Set[ViewObserver] = Set(new GameManager())
  }
}
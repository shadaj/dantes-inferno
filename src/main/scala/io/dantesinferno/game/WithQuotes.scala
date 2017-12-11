package io.dantesinferno.game

trait WithQuotes[Self <: ObjectState[Self] with WithQuotes[Self]] extends ObjectState[Self] {
  def setQuote(quote: Option[String]): Self
}

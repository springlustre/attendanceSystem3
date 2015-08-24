package controllers

/**
 * Created by lustre on 2015/8/10.
 */
import models.Result2
import play.api.libs.json._
import play.api.mvc._

object Search extends Controller {

  // Simple action - return search results as Json
  def perform(term:String) = Action {
    val m = Result2.find(term)
    Ok(Json.toJson(m))
  }
}

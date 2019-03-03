package controllers

import javax.inject.Inject
import dao.{ActorDao, PrefectureDao}
import play.api.mvc.{ AbstractController, ControllerComponents }
import scala.concurrent.ExecutionContext

class Application @Inject() (
  actorDao : ActorDao,
  prefectureDao : PrefectureDao,
  controllerComponents : ControllerComponents)(implicit executionContext: ExecutionContext) extends AbstractController(controllerComponents) {

  def index = Action.async {
    //    catDao.all().zip(dogDao.all()).map { case (cats, dogs) => Ok(views.html.index(cats, dogs)) }
    actorDao.all().map { case(actors) => Ok(views.html.index(actors)) }
  }

  def detail = Action.async {
    //actorDao.
  }

}

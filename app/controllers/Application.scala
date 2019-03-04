package controllers

import javax.inject.Inject
import dao.{ActorDao, PrefectureDao}
import models.Actor
import play.api.data.Form
import play.api.data.Forms.{sqlDate, number, longNumber, mapping, nonEmptyText, optional}
import play.api.mvc.{ AbstractController, ControllerComponents, Flash, RequestHeader }
import views.html

import scala.concurrent.ExecutionContext

class Application @Inject() (
  actorDao : ActorDao,
  prefectureDao : PrefectureDao,
  controllerComponents : ControllerComponents)(implicit executionContext: ExecutionContext) extends AbstractController(controllerComponents) {

  val actorForm = Form(
    mapping(
      "id" -> longNumber,
      "name" -> nonEmptyText,
      "height" -> number,
      "blood" -> nonEmptyText,
      "birthday" -> sqlDate("yyyy-MM-dd"),
      "birthplaceId" -> number
    )(Actor.apply)(Actor.unapply)
  )

  def index = Action.async {
    //    catDao.all().zip(dogDao.all()).map { case (cats, dogs) => Ok(views.html.index(cats, dogs)) }
    actorDao.all().map { case(actors) => Ok(views.html.index("Actor List", actors)) }
  }

  def detail(id : Long) = Action.async { implicit rs =>
    // Future[Option[Actor]]
    val actors = actorDao.findById(id)

    actors.map {
          // Option[Actor]
      actor =>
        actor match {
          case Some(a) => Ok(views.html.detail("Action Detail", a))
          case None => NotFound
        }
    }
  }

  def create = Action.async { implicit  rs =>
    Ok(views.html.create("Actor Create", actorForm))
  }

}

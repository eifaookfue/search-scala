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
    actorDao.all().map { actors => Ok(views.html.index("Actor List", actors)) }
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

  def create = Action { implicit  rs =>
    Ok(views.html.create("Actor Create", actorForm))
  }

  def save = Action { implicit rs =>
    actorForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.create("retry", formWithErrors)),
      actor => {
        actorDao.insert(actor)
        Redirect(routes.Application.index()).flashing("success" -> "actor created.")
      }
    )
  }

  def delete(id : Long) = Action.async { implicit rs =>
    for {
      _ <- actorDao.delete(id)
    } yield Redirect(routes.Application.index()).flashing("success" -> "actor.delete.success")

  }

  def init = Action { implicit rs => {
    val setup = DBIO.seq(
      actors.schema.create,
      actors ++= Seq(
        (1L, "丹波哲郎", 175, "O", "1922-07-17", 13),
        (2L, "森田健作", 175, "O", "1949-12-16", 13),
        (3L, "加藤剛", 173, null, "1949-12-16", 22),
        (4L, "島田陽子", 171, "O", "1953-05-17", 43),
        (5L, "山口果林", null, null, "1947-05-10", 13),
        (6L, "佐分利信", null, null, "1909-02-12", 1),
        (7L, "緒方拳", 173, "B", "1937-07-20", 13),
        (8L, "松山政路", 167, "A", "1947-05-21", 13),
        (9L, "加藤嘉", null, null, "1913-01-12", 13),
        (10L, "菅井きん", 155, "B", "1926-02-28", 13),
        (11L, "笠智衆", null, null, "1904-05-13", 43),
        (12L, "殿山泰司", null, null, "1915-10-17", 28),
        (13L, "渥美清", 173, "B", "1928-03-10", 13)
      )
    )
    db.run(setup)
  }
  }




  }

}

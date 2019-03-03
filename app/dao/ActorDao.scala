package dao

import java.sql.Date

import models.Actor
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile
import javax.inject.Inject

import scala.concurrent.{ExecutionContext, Future}

class ActorDao @Inject()  (protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  private val Actors = TableQuery[ActorTables]

  def all() : Future[Seq[Actor]] = db.run(Actors.result)

  def insert(actor : Actor) : Future[Unit] = db.run(Actors += actor).map { _ => () }

  private class ActorTables(tag: Tag) extends Table[Actor](tag, "Actor"){
    //case class Actor(id : Long, name : String, height : Int, blood : String, birthday : Date, birthplaceId : Int)
    def id = column[Long]("ID", O.PrimaryKey)
    def name = column[String]("NAME")
    def height = column[Int]("HEIGHT")
    def blood = column[String]("BLOOD")
    def birthday = column[Date]("BIRTHDAY")
    def birthplaceId = column[Int]("BIRTHPLACEID")

    def * = (id, name, height, blood, birthday, birthplaceId) <> (Actor.tupled, Actor.unapply)

  }


}

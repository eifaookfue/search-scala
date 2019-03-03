package dao

import java.sql.Date

import models.Prefecture
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile
import javax.inject.Inject

import scala.concurrent.{ExecutionContext,Future}

class PrefectureDao @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  private val Prefectures = TableQuery[PrefectureTables]

  def all() : Future[Seq[Prefecture]] = db.run(Prefectures.result)

  def insert(prefecture : Prefecture) : Future[Unit] = db.run(Prefectures += prefecture).map { _ => () }

  private class PrefectureTables(tag : Tag) extends Table[Prefecture](tag, "Pefecture") {
    def id = column[Int]("ID", O.PrimaryKey)
    def name = column[String]("NAME")

    def * = (id, name) <> (Prefecture.tupled, Prefecture.unapply)

  }

}

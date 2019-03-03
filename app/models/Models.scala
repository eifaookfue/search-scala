package models

import java.sql.Date

case class Actor(id : Long, name : String, height : Int, blood : String, birthday : Date, birthplaceId : Int)

case class Prefecture(id : Int, name : String)
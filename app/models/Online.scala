package models

import anorm.SqlParser._
import anorm._
import play.api.data.Form
import play.api.data.Forms._
import play.api.db.DB
import play.api.Play.current
/**
 * Created by lustre on 2015/7/23.
 */
case class Online(staff_id:String,on_time:String,off_time:String,attend_date:String)

object Online {
  val online = {

    get[String]("staff_id") ~ get[String]("on_time") ~ get[String]("off_time") ~
     get[String]("attend_date") map {
      case staff_id ~ on_time ~ off_time~attend_date =>
        Online(staff_id,on_time,off_time,attend_date)

    }
  }

  def findAllById(staff_id: String,period_start:String,period_end:String): List[Online] = {
    DB.withConnection { implicit connection =>
      SQL("select * from online_info where staff_id = {staff_id} and attend_date <= {period_end} and attend_date >= {period_start} order by id").on(
        'staff_id -> staff_id,'period_end->period_end,'period_start->period_start
      ).as(Online.online.*)
    }
  }
  def findAll(period_start:String,period_end:String): List[Online] = {

    DB.withConnection { implicit connection =>
      SQL("select * from online_info where attend_date <= {period_end} and attend_date >= {period_start} order by id").on(
        'period_end->period_end,'period_start->period_start
      ).as(Online.online.*)
    }
  }

  var onlineForm=Form(
    tuple(
      "staff_id"->nonEmptyText,
      "period_start"->nonEmptyText,
      "period_end"->nonEmptyText
    )
  )

}

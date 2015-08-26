package models

import anorm.SqlParser._
import anorm._
import play.api.Play.current
import play.api.data.Form
import play.api.data.Forms._
import play.api.db.DB

/**
 * Created by springlustre on 2015/8/20.
 */
case class Off(staff_id:String,reason:String,date:String,off_start:String,off_end:String,allow:String)

object Off{
  val off={
    get[String]("staff_id")~get[String]("reason")~get[String]("date")~get[String]("off_start")~get[String]("off_end")~get[String]("allow") map{
      case staff_id~reason~date~off_start~off_end~allow =>Off(staff_id,reason,date,off_start,off_end,allow)
    }
  }

  def createOff(staff_id:String,reason:String,date:String,off_start:String,off_end:String): Unit ={
    DB.withConnection{implicit c=>SQL("insert into off_info(staff_id,reason,date,off_start,off_end) values (" +
      "{staff_id},{reason},{date},{off_start},{off_end})").on('staff_id->staff_id,'reason->reason,'date->date,'off_start->off_start,'off_end->off_end).executeUpdate()
    }
  }

  //
  def viewById(id:String):List[Off]={
    DB.withConnection{implicit c=>SQL("select * from off_info where staff_id={id}").on(
      'id->id).as(Off.off.*)
    }
  }


  //
  def viewByApartment(apartment:String,start:String,end:String):List[Off]={
    DB.withConnection { implicit connection =>
      SQL("select * from off_info where off_info.staff_id in (select staff_id FROM staff_info where(staff_info.apartment={apartment})) and date>={start} and date<={end}").on(
        'apartment -> apartment,'start->start,'end->end
      ).as(Off.off.*)
    }
  }

  //
  def viewAll(start:String,end:String):List[Off] ={
    DB.withConnection{implicit c=>SQL("select * from off_info where date>={start} and date<={end}").on(
    'start->start,'end->end).as(Off.off.*)
    }
  }

  //
  //
  def allow(id:String,allow:String): Unit ={
    DB.withConnection{implicit c=>SQL("insert into off_info (allow) values({allow} where id={id} ) ").on(
    'id->id,'allow->allow).executeUpdate()
    }
  }


  var offForm = Form(
  tuple(
  "reason"->nonEmptyText,
  "date"->nonEmptyText,
  "off_start"->nonEmptyText,
    "off_end"->nonEmptyText
  )
  )

  var selForm = Form(
    tuple(
      "apartment"->nonEmptyText,
      "period_start"->nonEmptyText,
      "period_end"->nonEmptyText
    )
  )



}

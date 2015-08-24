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
case class Off(staff_id:String,title:String,reason:String,date:String,datetime:String,allow:String)

object Off{
  val off={
    get[String]("staff_id")~get[String]("title")~get[String]("reason")~get[String]("date")~get[String]("datetime")~get[String]("allow") map{
      case staff_id~title~reason~date~datetime~allow =>Off(staff_id,title,reason,date,datetime,allow)
    }
  }

  def createOff(staff_id:String,title:String,reason:String,date:String,datetime:String): Unit ={
    DB.withConnection{implicit c=>SQL("insert into off_info(staff_id,title,reason,date,datetime) values (" +
      "{staff_id},{title},{reason},{date},{datetime})").on('staff_id->staff_id,'title->title,'reason->reason,'date->date,'datetime->datetime).executeUpdate()
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
      SQL("select * from off_info where off_info.staff_id in (select staff_id FROM staff_info where(staff_info.apartment={apartment})) and datetime>={start} and datetime<={end}").on(
        'apartment -> apartment,'start->start,'end->end
      ).as(Off.off.*)
    }
  }

  //
  def viewAll(start:String,end:String):List[Off] ={
    DB.withConnection{implicit c=>SQL("select * from off_info where datetime>={start} and datetime<={end}").on(
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
  "title"->nonEmptyText,
  "reason"->nonEmptyText,
  "date"->nonEmptyText,
  "datetime"->nonEmptyText
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

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
case class Attendance(staff_id:String,start_time:String,end_time:String,
                      attend_date:String)

  object Attendance {

    val attendance = {

      get[String]("staff_id") ~ get[String]("start_time") ~ get[String]("end_time") ~
      get[String]("attend_date")map {
        case staff_id ~ start_time ~ end_time ~attend_date=>
          Attendance(staff_id,start_time,end_time,attend_date)
      }
    }

    def findAllById(staff_id: String,period_start:String,period_end:String): List[Attendance] = {

      DB.withConnection { implicit connection =>
        SQL("select * from attendance_info where staff_id = {staff_id} and attend_date <= {period_end} and attend_date >= {period_start} order by id").on(
          'staff_id -> staff_id,'period_end->period_end,'period_start->period_start
        ).as(Attendance.attendance.*)
      }
    }

    def findAll(period_start:String,period_end:String): List[Attendance] = {

      DB.withConnection { implicit connection =>
        SQL("select * from attendance_info where attend_date <= {period_end} and attend_date >= {period_start} order by id").on(
          'period_end->period_end,'period_start->period_start
        ).as(Attendance.attendance.*)
      }
    }


    def findAbsence(period_start:String,period_end:String):List[Attendance]={

      var tempList:List[Attendance]=Attendance.findAll(period_start,period_end)
      var list:List[Attendance]=Nil
      while (!(tempList.isEmpty))
      {
        if(Tool.isAbsence(tempList.head.start_time,tempList.head.end_time))
          {
             list=list:::List(tempList.head)
          }
        println(Tool.getlateTime(tempList.head.start_time))
        println(Tool.getTardyTime(tempList.head.end_time))
        tempList=tempList.tail
      }
      list
    }

    //获得一个人的缺勤信息
    def findAbsenceById(staff_id:String,period_start:String,period_end:String):List[Attendance]={

      var tempList:List[Attendance]=Attendance.findAllById(staff_id,period_start,period_end)
      var list:List[Attendance]=Nil
      while (!(tempList.isEmpty))
      {
        if(Tool.isAbsence(tempList.head.start_time,tempList.head.end_time))
        {
          list=list:::List(tempList.head)
        }
        println(Tool.getlateTime(tempList.head.start_time))
        println(Tool.getTardyTime(tempList.head.end_time))
        tempList=tempList.tail
      }
      list
    }







    var attendForm=Form(
      tuple(
        "staff_id"->nonEmptyText,
        "period_start"->nonEmptyText,
        "period_end"->nonEmptyText
      )
    )




}

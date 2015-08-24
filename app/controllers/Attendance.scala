package controllers

import controllers.Application._
import models.{Staff, Attendance,Online}
import play.api.mvc.{Action, Controller}
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import views._
import models.Attendance._
import models.Online._

/**
 * Created by lustre on 2015/7/23.
 */
object Attendances extends Controller{

  /*list the attendance of someone in one period by day or month or week
  * */

  //某个员工出勤
  def someOne(staff_id:String,period_start:String,period_end:String) = Action {request=>
    var staffId=request.session.get("email").getOrElse(0).toString
    if(Staff.getIdentify(staffId).equals("3")) {
      Ok(views.html.attendanceInfo(Attendance.findAllById(staff_id, period_start, period_end), staff_id, Staff.getNameById(staff_id), period_start, period_end, attendForm, "none"))
    }
    else {
      Ok(views.html.attendanceInfoOfOne(Attendance.findAllById(staff_id, period_start, period_end), staff_id, Staff.getNameById(staff_id), period_start, period_end, attendForm, "none"))
    }
    }
  //搜索出错，提示，默认显示所有员工
  def someOneErr(period_start:String,period_end:String,error:String) = Action {
    Ok(views.html.attendanceInfo(Attendance.findAll(period_start,period_end)," ","所有员工",period_start,period_end,attendForm,error))
  }
  //进入考勤页面，默认所有人今天的情况
  def allOne(period_start:String,period_end:String) = Action {
    Ok(views.html.attendanceInfo(Attendance.findAll(period_start,period_end)," ","所有员工",period_start,period_end,attendForm,"none"))
  }

  /*caculate the online data for someone through the staff_id
  * */
  //某个员工在线信息
  def onLineOfOne(staff_id:String,period_start:String,period_end:String)=Action{request=>
    var staffId=request.session.get("email").getOrElse(0).toString
    if(Staff.getIdentify(staffId).equals("3")) {
      Ok(views.html.onlineInfo(Online.findAllById(staff_id, period_start, period_end), staff_id, Staff.getNameById(staff_id), period_start, period_end, onlineForm, "none"))
    }
    else{
      Ok(views.html.onlineInfoOfOne(Online.findAllById(staff_id, period_start, period_end), staff_id, Staff.getNameById(staff_id), period_start, period_end, onlineForm, "none"))
    }
  }

  def onLineOfOneErr(period_start:String,period_end:String,error:String)=Action{
    Ok(views.html.onlineInfo(Online.findAll(period_start,period_end),"","所有员工",period_start,period_end,onlineForm,error))
  }
  //进入在线统计，默认所有人今天的情况
  def allOneOfOnline(period_start:String,period_end:String)=Action{request=>
     var staff_id=request.session.get("email").getOrElse(0).toString
    if(Staff.getIdentify(staff_id).equals("3")) {
      Ok(views.html.onlineInfo(Online.findAll(period_start, period_end), "", "所有员工", period_start, period_end, onlineForm, "none"))
    }
    else{
      Ok(views.html.onlineInfoOfOne(Online.findAllById(staff_id, period_start, period_end), staff_id, Staff.getNameById(staff_id), period_start, period_end, onlineForm, "none"))
    }

  }



}

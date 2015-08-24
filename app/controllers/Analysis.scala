package controllers

/**
 * Created by lustre on 2015/8/11.
 */

import controllers.Application._
import models.Attendance._
import models.Analyse._
import models.Analyse
import models._
import play.api._
import play.api.libs.json.Json
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import views._

object Analysis extends Controller{

  //一周平均出勤
  def analysisApartmentOfWeek=Action{implicit request=>
    var staff_id=request.session.get("email").getOrElse(0).toString
    analyseForm.bindFromRequest.fold(
    errors=>BadRequest,
    {
      case(id,period_start,period_end)=>
        if(period_start==null){
          var period_start:String = Tool.getNowWeekStart()
        }
        if(period_end == null){
          var period_end:String = Tool.getNowWeekEnd()
        }

          var dateArr = Analyse.ApartmentOfWeek(id, period_start, period_end)
          var date: Array[String] = new Array(5)
          date(0) = period_start
          for (i <- 1 to 4) {
            date(i) = Tool.getNextDay(date(i - 1))
          }
          for (i <- 0 to 4) println(date(i))
        if(Staff.getIdentify(staff_id).equals("3")) {//管理员
          Ok(views.html.attendanceAnalysis(dateArr, "week", date, analyseForm))
        }
        else{//普通员工
          Ok(views.html.attendanceAnalysisOfOne(dateArr, staff_id, date, analyseForm))
        }
    }
    )

  }

  //一月平均出勤
  def analysisApartmentOfMonth=Action{implicit request=>
    var staff_id=request.session.get("email").getOrElse(0).toString
    analyseForm.bindFromRequest.fold(
    errors=>BadRequest,
    {
      case(id,period_start,period_end)=>
        if(period_start==null){
          var period_start:String = Tool.getNowMonthStart()
        }
        if(period_end == null){
          var period_end:String = Tool.getNowMonthEnd()
        }
        var dayBwn=Tool.between(period_start,period_end)

        var dateArr=Analyse.ApartmentOfMonth(id,period_start,period_end)
        var date:Array[String]=new Array(dayBwn+1)
        date(0)=period_start
        for(i<-1 to dayBwn){
          date(i)=Tool.getNextDay(date(i-1))
        }
        if(Staff.getIdentify(staff_id).equals("3")) {
          //管理员
          Ok(views.html.attendanceAnalysis(dateArr, "month", date, analyseForm))
        }
        else{
          Ok(views.html.attendanceAnalysisOfOne(dateArr,staff_id, date, analyseForm))
        }
    }
    )
  }


  //部门所有员工出勤情况
  def analysisForStaff=Action{implicit request=>
       analyseForm.bindFromRequest.fold(
       errors=>BadRequest,
     {
        case(id,period_start,period_end)=>
        if(period_start==null){
          var period_start:String = Tool.getNowMonthStart()
        }
        if(period_end == null){
          var period_end:String = Tool.getNowMonthEnd()
        }

        var attendDataArr=Analyse.AttendOfStaff(id,period_start,period_end)
        var staffList=Analyse.getStaffByApartment(id)
          var staffArr:Array[String]=new Array(staffList.length)
        for(i<- 0 to staffList.length-1){
          staffArr(i)=staffList(i)
        }

        Ok(views.html.attendanceAnalysis(attendDataArr,"staff",staffArr,analyseForm))
    }
    )

  }



}

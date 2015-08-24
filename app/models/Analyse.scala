package models

import anorm.SqlParser._
import anorm._
import play.api.data.Form
import play.api.data.Forms._
import play.api.db.DB
import play.api.Play.current
/**
 * Created by lustre on 2015/8/11.
 */
case class Analyse(staff_id:String,start_time:String,end_time:String,attend_date:String)

object Analyse{

  var analysis=
  {
  get[String]("staff_id") ~ get[String]("start_time") ~ get[String]("end_time") ~
    get[String]("attend_date") map {
    case staff_id ~ start_time ~ end_time ~ attend_date =>
      Analyse(staff_id, start_time, end_time, attend_date)
    }
  }


  def getByApartment(apartment:String,period_start:String,period_end:String):List[Analyse] ={
    DB.withConnection { implicit connection =>
      SQL("select * from attendance_info where attendance_info.staff_id in (select staff_id FROM staff_info where(staff_info.apartment={apartment})) and attend_date >={period_start} and attend_date <= {period_end} order by id").on(
        'apartment -> apartment,'period_end->period_end,'period_start->period_start
      ).as(Analyse.analysis.*)
    }
  }

  def getByStaffId(staff_id:String,period_start:String,period_end:String):List[Analyse] ={
    DB.withConnection { implicit connection =>
      SQL("select * from attendance_info where staff_id={staff_id} and attend_date >={period_start} and attend_date <= {period_end} order by id").on(
        'staff_id -> staff_id,'period_end->period_end,'period_start->period_start
      ).as(Analyse.analysis.*)
    }
  }

  //获取一个部门一周平均出勤
  def ApartmentOfWeek(apartment:String,period_start:String,period_end:String):Array[Float]={
    var list:List[Analyse]=Nil
    if(apartment.toInt < 30) {//按部门
       list = getByApartment(apartment, period_start, period_end)
    }else{//按个人
       list = getByStaffId(apartment, period_start, period_end)
    }

    var dateList=List(period_start)
    for(i<-0 to 3){
      dateList=dateList:::List(Tool.getNextDay(dateList(i)))
    }

    var totalTime:Float=0
    var cnt=0
    var arrAve=new Array[Float](5)
    for(i<-0 to 4){
      for(j<-0 to list.length-1){
        if(list(j).attend_date.equals(dateList(i))){
          totalTime=totalTime+Tool.getTotalTime(list(j).start_time,list(j).end_time)
          cnt=cnt+1
        }
      }
      arrAve(i)=totalTime/cnt
      totalTime=0
      cnt=0
    }
    for(i<-0 to 4) println(arrAve(i))
    arrAve
  }


  //获取一个部门一个月每天平均出勤
  def ApartmentOfMonth(apartment:String,period_start:String,period_end:String):Array[Float]={
    var list:List[Analyse]=Nil
    if(apartment.toInt< 30) {
      //按部门
       list = getByApartment(apartment, period_start, period_end) //sql结果
    }
    else{
       list = getByStaffId(apartment, period_start, period_end)
    }
    println("list:"+list)
    var dateList=List(period_start)
    var dayNum=Tool.between(period_start,period_end)//间隔天数
    for(i<-0 to dayNum-1){
      dateList=dateList:::List(Tool.getNextDay(dateList(i)))
    }
    var totalTime:Float=0
    var cnt=0
    var arrAve=new Array[Float](dayNum+1)
    for(i<-0 to dayNum){
      for(j<-0 to list.length-1){
        if(list(j).attend_date.equals(dateList(i))){
          totalTime=totalTime+Tool.getTotalTime(list(j).start_time,list(j).end_time)
          cnt=cnt+1
        }
      }
      arrAve(i)=totalTime/cnt
      totalTime=0
      cnt=0
    }
    arrAve
  }

  //获取一个部门所有员工
  def getStaffByApartment(apartment:String):List[String]={
    DB.withConnection { implicit connection =>
      SQL("select staff_id from staff_info where apartment={apartment}").on(
        'apartment -> apartment
      ).as(scalar[String].*)
    }
  }

  //一个部门所有员工出勤
  def AttendOfStaff(apartment:String,period_start:String,period_end:String):Array[Float]={
    var list=getByApartment(apartment,period_start,period_end)//sql结果
    var staffList:List[String]=getStaffByApartment(apartment)
    var staffNum=staffList.length//员工人数
    var totalTime:Float=0

    var arrTotal=new Array[Float](staffNum)
    for(i<-0 to staffNum-1){
      for(j<-0 to list.length-1){
        if(list(j).staff_id.equals(staffList(i))){
          totalTime=totalTime+Tool.getTotalTime(list(j).start_time,list(j).end_time)
        }
      }
      arrTotal(i)=totalTime
      totalTime=0
    }
    arrTotal
  }


//表单
  var analyseForm=Form(
    tuple(
      "id"->nonEmptyText,
      "period_start"->nonEmptyText,
      "period_end"->nonEmptyText
    )
  )
}
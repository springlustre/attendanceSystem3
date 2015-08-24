package controllers

/**
 * Created by springlustre on 2015/8/21.
 */

import models._
import play.api.mvc._
import models.Off._


object OffManage extends Controller{

  def showOffById=Action{implicit request=>
    var staff_id=request.session.get("email").getOrElse(0).toString
    val offList=Off.viewById(staff_id)
    Ok(views.html.offManageOfOne(offList,Off.offForm))
  }

  def newOffmessage=Action{implicit request=>
    val staff_id=request.session.get("email").getOrElse(0).toString
    offForm.bindFromRequest.fold(
    error=>BadRequest,
    {

      case(title,reason,date,datetime)=>
        val off=Off.createOff(staff_id,title,reason,date,datetime)
        val offList=Off.viewById(staff_id)
        Ok(views.html.offManageOfOne(offList,Off.offForm))
    }
    )
  }

  //
  def viewAll=Action{
    val period_start=Tool.getNowWeekStart()
    val period_end=Tool.getNowWeekEnd()
    val offList=Off.viewAll(period_start,period_end)
    Ok(views.html.offManage(offList,Off.selForm))
  }

  def viewByApartment=Action{implicit request=>
    selForm.bindFromRequest.fold(
    error=>BadRequest,
    {
      case(apartment,period_start,period_end)=>
        val offList=Off.viewByApartment(apartment,period_start,period_end)
        Ok(views.html.offManage(offList,Off.selForm))
    }
    )
  }






}

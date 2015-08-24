package controllers

import models._
import models.Staff._
import models.Analyse._
import models.User._
import play.api._
import play.api.libs.json.Json
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import views._
import models.Attendance._

object Application extends Controller {

  val loginForm = Form(
    tuple(
      "email" -> text,
      "password" -> text
    ) verifying ("Invalid email or password", result => result match {
      case (email, password) => User.authenticate(email, password).isDefined
    })
  )

  def index = Action {
    Redirect(routes.Application.staff)
  }

  def username(request: RequestHeader) = request.session.get("email")
  /*def staff = Action {
    Ok(views.html.index(Staff.all(),Staff.staffForm))
  }
  */
  def staff = Action { request =>
    request.session.get("email").map { username =>
      Ok(views.html.userManage(Staff.all(), Staff.staffForm,username,User.identify(username)))
    }.getOrElse {
      Unauthorized("Oops, you are not connected")
    }
  }

  def  newStaff=Action{implicit request=>
    staffForm.bindFromRequest.fold(
    errors=>BadRequest,
    {
      case(staff_id,staff_name,apartment,staff_sex,mac_address,mobile)=>
        val staff=Staff.staff_create(staff_id,apartment,staff_name,staff_sex,mac_address,mobile)
        Redirect(routes.Application.staff)
    }
    )
  }

  //根据staff_id取出一个员工信息，Ajax返回
  def selStaff(staff_id:String)=Action {
    val m = Staff.findOne(staff_id)
    val l=List(m(0).staff_id):::List(m(0).staff_name):::List(m(0).apartment):::List(m(0).staff_sex):::List(m(0).mac_address):::List(m(0).mobile)
    Ok(Json.toJson(l))
  }


  def  modifyStaff=Action{implicit request=>
    var staff_id=request.session.get("email").getOrElse(0).toString
    staffForm.bindFromRequest.fold(
    errors=>BadRequest,
    {
      case(staff_id,staff_name,apartment,staff_sex,mac_address,mobile)=>
        val staff=Staff.staff_modify(staff_id,staff_name,apartment,staff_sex,mac_address,mobile)
        if(Staff.getIdentify(staff_id).equals("3")) {
          Redirect(routes.Application.staff)
        }else{
          Redirect(routes.Application.personalInfo())
        }
    }
    )
  }

  def  deleteStaff(id:Long)=Action{
    Staff.staff_delete(id)
    Redirect(routes.Application.staff())
  }

  def personalInfo=Action{request =>
    request.session.get("email").map { username =>
      Ok(views.html.userManageOfOne(Staff.findOne(username), Staff.staffForm,username,User.identify(username)))
    }.getOrElse {
      Unauthorized("Oops, you are not connected")
    }

  }

  /**
   * Login page.
   */
  def login = Action { implicit request =>
    Ok(views.html.login(loginForm))
    //Ok(views.html.index(Message.all(), Message.messageForm))
    //Redirect(routes.Application.messages)
  }

  /**
   * Handle login form submission.
   */
  def authenticate = Action { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => BadRequest(html.login(loginForm)),
      user => Redirect(routes.Staffs.index).withSession("email" -> user._1)
    )
  }


  /**
   * Logout and clean the session.
   */
  def logout = Action {
    Redirect(routes.Application.login).withNewSession.flashing(
      "success" -> "You've been logged out"
    )
  }

  /*考勤信息查询
  * */
  //根据某个人id进入考勤查询
  def viewAttendOfOne(staff_id:String,period_start:String,period_end:String)=Action{
      var period_start:String = Tool.getNowWeekStart()
      var period_end:String = Tool.getNowWeekEnd()
   Redirect(routes.Attendances.someOne(staff_id,period_start,period_end))
  }

  //进入考勤查询，默认显示所有
  def viewAttendOfAll()=Action{
    var period_start:String = Tool.getNowDate()
    var period_end:String = Tool.getNowDate()//默认今天
    Redirect(routes.Attendances.allOne(period_start,period_end))
  }

  //选择一个员工
  def  selOne=Action{implicit request=>
    attendForm.bindFromRequest.fold(
    errors=>BadRequest,
    {
      case(staff_id,period_start,period_end)=>
        if(period_start==null){
          var period_start:String = Tool.getNowDate()
        }
        if(period_end == null){
          var period_end:String = Tool.getNowDate()
        }
        Redirect(routes.Attendances.someOne(staff_id,period_start,period_end))
    }
    )
  }
  //结果不唯一时显示搜索结果
  def searchResult(staff_name:String) = Action { request =>
      Ok(views.html.searchResult(Staff.searchResult(staff_name)))
    }

  //按姓名或工号搜索
  def searchOne=Action{implicit request=>
    attendForm.bindFromRequest.fold(
    errors=>BadRequest,
    {
      case(staff_id,period_start,period_end)=>
          var period_start:String = Tool.getNowWeekStart()
          var period_end:String = Tool.getNowWeekEnd()//默认本周
        var getid:List[String]=Staff.getIdBySearch(staff_id)//通过输入的关键词搜索id

        if(getid.length==1){//只有一个结果
          Redirect(routes.Attendances.someOne(getid.head,period_start,period_end))
        }
        else if(getid.length==0){
          var error:String="对不起,您搜索的员工不存在"
          period_start = Tool.getNowDate()
          period_end = Tool.getNowDate()//默认今天
          Redirect(routes.Attendances.someOneErr(period_start,period_end,error))
        }
        else{//结果不唯一
          Redirect(routes.Application.searchResult(staff_id))
        }
    }
    )
  }
  /*上面是考勤信息查询
* */


  /*在线信息统计查询
  * */

  //进入在线查询，默认显示所有
  def viewOnlineOfAll()=Action{
    var period_start:String = Tool.getNowDate()
    var period_end:String = Tool.getNowDate()//默认今天
    Redirect(routes.Attendances.allOneOfOnline(period_start,period_end))
  }
  //一个人的在线信息
  def viewOnlineOfOne(staff_id:String,period_start:String,period_end:String)=Action{
    var period_start:String = Tool.getNowDate()
    var period_end:String = Tool.getNowDate()
    Redirect(routes.Attendances.onLineOfOne(staff_id,period_start,period_end))
  }
  //选择一个员工的在线信息-某一天
  def  selOneOfOnline=Action{implicit request=>
    attendForm.bindFromRequest.fold(
    errors=>BadRequest,
    {
      case(staff_id,period_start,period_end)=>
        if(period_start==null){
          var period_start:String = Tool.getNowDate()
        }
        if(period_end.equals("none")){
          var period_end:String = period_start
        }
        Redirect(routes.Attendances.onLineOfOne(staff_id,period_start,period_end))
    }
    )
  }


  //按姓名或工号搜索在线信息
  def searchOneOfOnline=Action{implicit request=>
    attendForm.bindFromRequest.fold(
    errors=>BadRequest,
    {
      case(staff_id,period_start,period_end)=>
        var period_start:String = Tool.getNowDate()
        var period_end:String = Tool.getNowDate()//默认本日
      var getid:List[String]=Staff.getIdBySearch(staff_id)//通过输入的关键词搜索id

        if(getid.length==1){//只有一个结果
          Redirect(routes.Attendances.onLineOfOne(getid.head,period_start,period_end))
        }
        else if(getid.length==0){
          var error:String="对不起,您搜索的员工不存在"
          period_start = Tool.getNowDate()
          period_end = Tool.getNowDate()//默认今天
          Redirect(routes.Attendances.onLineOfOneErr(period_start,period_end,error))
        }
        else{//结果不唯一
          Redirect(routes.Application.searchResult(staff_id))
        }
    }
    )
  }

  /*上面是在线信息查询
* */

  /*缺勤统计
  * */
  def absenceInfo=Action{implicit request=>
    var staff_id=request.session.get("email").getOrElse(0).toString
    var period_start:String = Tool.getNowWeekStart()
    var period_end:String = Tool.getNowWeekEnd()//默认本周
    //var period_start="2015-07-20"
    //var period_end="2015-08-08"
    if(Staff.getIdentify(staff_id).equals("3")) {
      Ok(views.html.absenceInfo(Attendance.findAbsence(period_start, period_end),staff_id, "所有部门", period_start, period_end, attendForm))
    }
    else{
      Ok(views.html.absenceInfoOfOne(Attendance.findAbsenceById(staff_id,period_start, period_end),staff_id,Staff.getNameById(staff_id), period_start, period_end, attendForm))
    }
  }

  def selAbsence=Action{implicit request=>
    var staff_id=request.session.get("email").getOrElse(0).toString
    attendForm.bindFromRequest.fold(
    error=>BadRequest,
    {
     case(staff_id,period_start,period_end)=>
       if(period_start==null){
         var period_start:String = Tool.getNowWeekStart()
       }
       if(period_end==null){
         var period_end:String = Tool.getNowWeekEnd()
       }
       if(Staff.getIdentify(staff_id).equals("3")) {
         Ok(views.html.absenceInfo(Attendance.findAbsence(period_start, period_end),staff_id, "所有部门", period_start, period_end, attendForm))
       }
        else{
         Ok(views.html.absenceInfoOfOne(Attendance.findAbsenceById(staff_id,period_start, period_end), staff_id,Staff.getNameById(staff_id), period_start, period_end, attendForm))
       }
    }
    )

  }

//统计分析
  def analysisIndex=Action{
  var dataAn:Array[Float]=Array(10,34,23,56,43,29)
  var date:Array[String]=Array("周一","周二","周三","周四","周五")
  Ok(views.html.attendanceAnalysis(dataAn,"",date,analyseForm))
}

  def analysisOfOneIndex=Action{request=>
    var staff_id=request.session.get("email").getOrElse(0).toString
    var dataAn:Array[Float]=Array(0,0,0,0,0)
    var date:Array[String]=Array("周一","周二","周三","周四","周五")
    Ok(views.html.attendanceAnalysisOfOne(dataAn,staff_id,date,analyseForm))
  }






}

/*
 *
 * */
trait Secured {

  /**
   * Retrieve the connected user email.
   */
  private def username(request: RequestHeader) = request.session.get("email")

  /**
   * Redirect to login if the user in not authorized.
   */
  private def onUnauthorized(request: RequestHeader) = Results.Redirect(routes.Application.login)

  // --

  /**
   * Action for authenticated users.
   */
  def IsAuthenticated(f: => String => Request[AnyContent] => Result) =
    Security.Authenticated(username, onUnauthorized)
    { user =>
      Action(request => f(user)(request))
    }

}
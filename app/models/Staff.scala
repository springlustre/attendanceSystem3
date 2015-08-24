package models

import anorm.SqlParser._
import anorm._
import play.api.data._
import play.api.data.Forms._
import play.api.db._
import play.api.Play.current
/**
 * Created by lustre on 2015/7/20.
 */
case class
Staff(id:Long,staff_id:String,staff_name:String,apartment:String,staff_sex:String,mac_address:String,mobile:String)
object Staff {
  //def all():List[Message]=messageList
  val staff={

    get[Long]("id")~ get[String]("staff_id")~get[String]("staff_name")~get[String]("apartment")~
      get[String]("staff_sex") ~ get[String]("mac_address")~get[String]("mobile") map{
      case id~staff_id~staff_name~apartment~staff_sex ~ mac_address ~ mobile=>
        Staff(id,staff_id,staff_name,apartment,staff_sex,mac_address,mobile)
    }
  }

    def all():List[Staff]=DB.withConnection{implicit c=>
      SQL("select * from staff_info").as(staff* )
    }


    def staff_create(staff_id:String,staff_name:String,apartment:String,staff_sex:String,mac_address:String,mobile:String):Unit={
      DB.withConnection{implicit  c=>
        SQL("insert into staff_info(staff_id,staff_name,apartment,staff_sex,mac_address,mobile) " +
          "values({staff_id},{staff_name},{apartment},{staff_sex},{mac_address},{mobile})").on(
          'staff_id->staff_id,'staff_name->staff_name,'apartment->apartment,'staff_sex->staff_sex,
          'mac_address->mac_address,'mobile->mobile
        ).executeUpdate()
      }
    }

   def staff_delete(id:Long){
    //messageList=messageList.filterNot(message=>message.id==id)
    DB.withConnection{implicit c=>
      SQL("delete from staff_info where id={id}").on(
        'id->id
      ).executeUpdate()
    }
  }

  def staff_modify(staff_id:String,staff_name:String,apartment:String,staff_sex:String,mac_address:String,mobile:String): Unit ={
    DB.withConnection{implicit  c=>
      SQL("UPDATE staff_info SET staff_name={staff_name},apartment={apartment},staff_sex={staff_sex},mac_address={mac_address},mobile={mobile} where staff_id={staff_id}").on(
          'staff_id->staff_id,'staff_name->staff_name,'apartment->apartment,'staff_sex->staff_sex, 'mac_address->mac_address,'mobile->mobile
        ).executeUpdate()
    }
  }

  def getNameById(staff_id:String):String={
    DB.withConnection { implicit c =>
      //var rs: String =
      SQL("select staff_name from staff_info where staff_id=({staff_id})").on('staff_id->staff_id).as(scalar[String].single)

    }
  }

  def getApartmentById(staff_id:String):String={
    DB.withConnection { implicit c =>
      //var rs: String =
      SQL("select apartment from staff_info where staff_id=({staff_id})").on('staff_id->staff_id).as(scalar[String].single)
    }
  }

  //获得身份
  def getIdentify(staff_id:String):String={
    DB.withConnection { implicit c =>
      //var rs: String =
      SQL("select identify from staff_info where staff_id=({staff_id})").on('staff_id->staff_id).as(scalar[String].single)
    }
  }

  def getIdBySearch(searchKey:String):List[String]={
    DB.withConnection { implicit c =>
      //var rs: String =
      SQL("select staff_id from staff_info where staff_id=({searchKey}) or staff_name={searchKey}").on('searchKey->searchKey).as(scalar[String].*)

    }
  }

  def searchResult(staff_name:String):List[Staff]=DB.withConnection{implicit c=>
    SQL("select * from staff_info where staff_name={staff_name}").on('staff_name->staff_name).as(staff* )
  }


  def findOne(staff_id:String)={
    DB.withConnection{implicit c=>
    SQL("select * from staff_info where staff_id={staff_id}").on('staff_id->staff_id).as(staff* )
  }
  }

  //表单
  var staffForm=Form(
    tuple(
    "staff_id"->nonEmptyText,
    "staff_name"->nonEmptyText,
    "apartment"->nonEmptyText,
    "staff_sex"->nonEmptyText,
    "mac_address"->nonEmptyText,
    "mobile"->nonEmptyText
    )
  )

}

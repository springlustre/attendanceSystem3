package models

/**
 * Created by lustre on 2015/7/16.
 */
import play.api.data._
import play.api.data.Forms._
import play.api.db._
import play.api.Play.current
import anorm._
import anorm.SqlParser._

case class Message(id:Long,label:String)
object Message {
  //def all():List[Message]=messageList
  val message={

    get[Long]("id")~
      get[String]("label") map{

      case id~label=>Message(id,label)

    }

  }
  def all():List[Message]=DB.withConnection{implicit c=>
    SQL("select * from message").as(message*)

  }
  def create(label:String): Unit ={
    //messageList=messageList:::(new Message(messageList.length+1,label)::Nil)
    DB.withConnection{implicit  c=>
      SQL("insert into message(label) values({label})").on(
        'label->label
      ).executeUpdate()
    }
  }
  def delete(id:Long){
    //messageList=messageList.filterNot(message=>message.id==id)
    DB.withConnection{implicit c=>
      SQL("delete from message where id={id}").on(
        'id->id
      ).executeUpdate()
    }
  }

  //var messageList:List[Message]=new Message(1,"第一条留言")::new Message(2,"第二条留言")::Nil
  var messageForm=Form(
    "label"->nonEmptyText
  )
}


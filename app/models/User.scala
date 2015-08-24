package models

import anorm.SqlParser._
import anorm._
import play.api.Play.current
import play.api.db._

import scala.language.postfixOps

case class User(user_id: String, name: String, password: String)

object User {

  // -- Parsers

  /**
   * Parse a User from a ResultSet
   */
  val simple = {
    get[String]("user.user_id") ~
      get[String]("user.name") ~
      get[String]("user.password") map {
      case user_id~name~password => User(user_id, name, password)
    }
  }

  // -- Queries

  /**
   * Retrieve a User from user_id.
   */
  def findByuser_id(user_id: String): Option[User] = {
    DB.withConnection { implicit connection =>
      SQL("select * from user where user_id = {user_id}").on(
        'user_id -> user_id
      ).as(User.simple.singleOpt)
    }
  }

  /**
   * Retrieve all users.
   */
  def findAll: Seq[User] = {
    DB.withConnection { implicit connection =>
      SQL("select * from user").as(User.simple *)
    }
  }

  /**
   * Authenticate a User.
   */
  def authenticate(user_id: String, password: String): Option[User] = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          select * from user where
          user_id = {user_id} and password = {password}
        """
      ).on(
          'user_id -> user_id,
          'password -> password
        ).as(User.simple.singleOpt)
    }
  }

  /**
   * Create a User.
   */
  def create(user: User): User = {
    DB.withConnection { implicit connection =>
      SQL(
        """
           insert into user values (
             {user_id}, {name}, {password}
           )
        """
      ).on(
          'user_id -> user.user_id,
          'name -> user.name,
          'password -> user.password
        ).executeUpdate()

      user

    }
  }

  def identify(staff_id:String):String= {
    DB.withConnection { implicit c =>
      //var rs: String =
        SQL("select identify from staff_info where staff_id=({staff_id})").on('staff_id->staff_id).as(scalar[String].single)
      //rs
      // def id():String={
      //  var identify:String=""
      //  if(true) identify=""
   // }
  }
  }

}

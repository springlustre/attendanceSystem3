package models

import java.text.SimpleDateFormat
import java.util.{Calendar, Date}
import java.text.DecimalFormat

/**
 * Created by lustre on 2015/7/24.
 */
case class Tool()
  object Tool{

    //
    def getStartTime():String ={
      var df:SimpleDateFormat=new SimpleDateFormat("HH:mm:ss")
      var start=df.parse("10:00:00")
      var startSet=df.format(start)
      startSet
    }
    def getEndTime():String ={
      var df:SimpleDateFormat=new SimpleDateFormat("HH:mm:ss")
      var end=df.parse("16:30:00")
      var endSet=df.format(end)
      endSet
    }
    //今天
    def getNowDate():String={
      var now:Date = new Date()
      var  dateFormat:SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");//
      var hehe = dateFormat.format( now )
      hehe
    }
    //昨天
    def getYesterday():String={
      var  dateFormat:SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");//
      var cal:Calendar=Calendar.getInstance()
      cal.add(Calendar.DATE,-1)
      var yesterday=dateFormat.format(cal.getTime())
      yesterday
    }

    //下一天
    def getNextDay(today:String):String={
      var  dateFormat:SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");//
      var cal:Calendar=Calendar.getInstance()
      cal.setTime(dateFormat.parse(today))
      cal.add(Calendar.DATE,1)
      var nextDay=dateFormat.format(cal.getTime())
      nextDay
    }
    //本周
    def getNowWeekStart():String={
      var period:String=""
      var cal:Calendar =Calendar.getInstance();
      var df:SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
      cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); //获取本周一的日期
      period=df.format(cal.getTime())
      period
    }

    def getNowWeekEnd():String={
      var period:String=""
      var cal:Calendar =Calendar.getInstance();
      var df:SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
      cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);//这种输出的是上个星期周日的日期，因为老外那边把周日当成第一天
      cal.add(Calendar.WEEK_OF_YEAR, 1)// 增加一个星期，才是我们中国人理解的本周日的日期
      period=df.format(cal.getTime())
      period
    }

    //上周
    def getLastWeekStart():String={
      var period:String=""
      var cal:Calendar =Calendar.getInstance();
      var df:SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
      cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); //获取本周一的日期
      cal.add(Calendar.WEEK_OF_YEAR,-1)
      period=df.format(cal.getTime())
      period
    }

    def getLastWeekEnd():String={
      var period:String=""
      var cal:Calendar =Calendar.getInstance();
      var df:SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
      cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);//这种输出的是上个星期周日的日期，因为老外那边把周日当成第一天
      period=df.format(cal.getTime())
      period
    }

    //本月
    def getNowMonthStart():String={
      var period:String=""
      var cal:Calendar =Calendar.getInstance();
      var df:SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
      cal.set(Calendar.DATE, 1)
      period=df.format(cal.getTime())//本月第一天
      period
    }

    def getNowMonthEnd():String={
      var period:String=""
      var cal:Calendar =Calendar.getInstance();
      var df:SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
      cal.set(Calendar.DATE, 1)
      cal.roll(Calendar.DATE,-1)
      period=df.format(cal.getTime())//本月最后一天
      period
    }

    def nextMonth(month:String):String={
      var  dateFormat:SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")
      var cal:Calendar=Calendar.getInstance()
      cal.setTime(dateFormat.parse(month))
      cal.add(Calendar.MONTH,-1)
      var nextMonth=dateFormat.format(cal.getTime())
      nextMonth
    }
    def getLastMonthStart():String={
      var nowMonth=getNowMonthStart()
      var lastMonth=nextMonth(nowMonth)
      lastMonth
    }

    def getLastMonthEnd():String={
      var nowMonth=getNowMonthEnd()
      var lastMonth=nextMonth(nowMonth)
      lastMonth
    }

    def DateFormat(time:String):String={
      var sdf:SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")
      var date:String = sdf.format(new Date((time.toLong*1000l)))
      date
    }

    def timeFormat(time:String):String={
      var sdf:SimpleDateFormat = new SimpleDateFormat("HH:mm:ss")
      var date:String = sdf.format(new Date((time.toLong*1000l)))
      date
    }

    //工作总时长，迟到早退等的的处理
    def getTotalTime0(start_time:String,end_Time:String):Float={
      var df:SimpleDateFormat=new SimpleDateFormat("HH:mm:ss")
      var begin:Date=df.parse(start_time)
      var end:Date = df.parse(end_Time)
      var between:Long=(end.getTime()-begin.getTime())/1000//转化成秒
      var hour:Float=between.toFloat/3600
      hour
    }
    def getTotalTime(start_time:String,end_Time:String):Float={
      var hour=getTotalTime0(start_time,end_Time)
      var decf:DecimalFormat=new DecimalFormat("0.00")
      decf.format(hour)
      hour
    }

    //核心工作时间，迟到早退等的的处理
    def getCoreTime0(start_time:String,end_Time:String):Float={
      var df:SimpleDateFormat=new SimpleDateFormat("HH:mm:ss")
      var begin:Date=df.parse(start_time)
      var end:Date = df.parse(end_Time)
      var beginSet=df.parse("10:00:00")
      var endSet=df.parse("16:30:00")
      var between:Long=0
      if(begin.getTime()>beginSet.getTime())//迟到
        {
          if(end.getTime()<endSet.getTime())//早退
            {
              between=(end.getTime()-begin.getTime())/1000//转化成秒
            }
          else{
            between=(endSet.getTime()-begin.getTime())/1000//转化成秒

          }
        }
      else//没迟到
        {
          if(end.getTime()<endSet.getTime())//早退
          {
            between=(end.getTime()-beginSet.getTime())/1000//转化成秒
          }
          //没有迟到早退
          else
            between=(endSet.getTime()-beginSet.getTime())/1000//转化成秒
        }

      var hour:Float=between.toFloat/3600
      hour

    }

    def getCoreTime(start_time:String,end_Time:String)={
      var hour=getCoreTime0(start_time,end_Time)
      var decf:DecimalFormat=new DecimalFormat("0.00")
      decf.format(hour)
    }


    //弹性工作时间
    def getFlexibleTime(start_time:String,end_Time:String)={
      var between=getTotalTime0(start_time,end_Time)-getCoreTime0(start_time,end_Time)
      var decf:DecimalFormat=new DecimalFormat("0.00")
      decf.format(between)
    }

    //迟到时间
    def getlateTime(start_time:String):String={
      var df:SimpleDateFormat=new SimpleDateFormat("HH:mm:ss")
      var begin:Date=df.parse(start_time)
      var beginSet=df.parse("10:00:00")
      var between:Long=0
      if(begin.getTime()>beginSet.getTime())//迟到
      {
          between=(begin.getTime()-beginSet.getTime())/1000//转化成秒
      }
      else//没吃到
      {
          between=0
      }
      var min:Float=between.toFloat/60
      var decf:DecimalFormat=new DecimalFormat("0.0")
      decf.format(min)
    }

    //早退时间
    def getTardyTime(end_Time:String):String={
      var df:SimpleDateFormat=new SimpleDateFormat("HH:mm:ss")
      var end:Date = df.parse(end_Time)
      var endSet=df.parse("16:30:00")
      var between:Long=0
      if(end.getTime()<endSet.getTime())//迟到
      {
        between=(endSet.getTime()-end.getTime())/1000//转化成秒
      }
      else//没吃到
      {
        between=0
      }
      var min:Float=between.toFloat/60
      var decf:DecimalFormat=new DecimalFormat("0.0")
      decf.format(min)
    }

    def isAbsence(start_time:String,end_time:String):Boolean={
      var df:SimpleDateFormat=new SimpleDateFormat("HH:mm:ss")
      var end:Date = df.parse(end_time)
      var endSet=df.parse("16:30:00")
      var start:Date=df.parse(start_time)
      var startSet=df.parse("10:00:00")
      var is:Boolean=false
      if(end.getTime()<endSet.getTime())//迟到
      {
       is=true
      }
        if(start.getTime > startSet.getTime)
          {
            is=true
          }
     is
    }

    //日期差天数
    def between(period_start:String,period_end:String):Int={
      var df:SimpleDateFormat=new SimpleDateFormat("yyyy-MM-dd")
      var start:Date = df.parse(period_start)
      var end:Date=df.parse(period_end)
      var bet=end.getTime()-start.getTime()
      var between = (bet /( 1000*60*60*24)).toInt
      between
    }

}

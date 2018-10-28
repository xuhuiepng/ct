package day02

object Exercise {

  def main(args: Array[String]): Unit = {

    val list0=List(1,4,7,2,5,8,3,6,9)

    val list1 = list0.map(_ * 2)
    println(list1)

    val list2 = list0.filter(_ % 2 == 0)
    println(list2)

    val list3 = list0.sorted
    println(list3)

    val list4 = list3.reverse
    println(list4)

    val list5 = list0.grouped(4)
//    println(list5.toBuffer)

    val flatten = list5.flatten
    println(flatten.toBuffer)

    val lines = List("hello java hello scala","hello spark","scala python")
//    val words = lines.map(_.split(" "))
//    println(words)
    val res = lines.flatMap(_.split(" "))
    println(res)

    //----------------------------------------
    println("----------------------------------------------------------")

    val arr = (1 to 100).toArray;

    // .par 多线程执行
//    val sum = arr.sum
    val sum1 = arr.par.sum
    println(sum1)

    //聚合
    val i = arr.reduce(_+_)
    println(i)
  }
}

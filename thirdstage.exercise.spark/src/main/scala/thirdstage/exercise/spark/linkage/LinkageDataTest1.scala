package thirdstage.exercise.spark.linkage

import org.scalatest.BeforeAndAfter
import org.zeroturnaround.zip.ZipUtil
import com.holdenkarau.spark.testing.SharedSparkContext
import org.scalatest.FunSuite
import org.apache.spark.rdd.RDD

class LinkageDataTest1 extends FunSuite with SharedSparkContext {

  var block1: RDD[String] = _

  override def beforeAll {
    super.beforeAll()

    val workDir = System.getProperty("workDir")
    if (workDir == null || workDir.isEmpty()) {
      throw new IllegalStateException("The 'workDir' should be provided at command-line")
    }

    val block1Csv = new java.io.File(workDir + raw"\block_1.csv")
    if (block1Csv.isDirectory()) {
      throw new IllegalStateException("The directory 'block_1.csv' exists!!, which is never expected.")
    }

    if (!block1Csv.exists()) {
      val dataDir = System.getProperty("dataDir")
      if (dataDir == null || dataDir.isEmpty()) {
        throw new IllegalStateException("The 'dataDir' should be provided at command-line")
      }
      val block1Zip = new java.io.File(dataDir + raw"\linkage\block_1.zip")

      ZipUtil.unpack(block1Zip, new java.io.File(workDir))
    }

    this.block1 = sc.textFile(block1Csv.getAbsolutePath)

  }

  test("The total rows of the block 1 is more than tens of thousands") {
    assert(this.block1.count() > 10000)
  }

  test("Taking just leading 10 rows from the block") {
    val head = this.block1.take(10)
    assert(head.length == 10)
  }

  test("The block 1 has just one header line") {
    val header = this.block1.filter(x => x.contains("id_1"))
    assert(header.count() == 1)
  }

  test("The 1st data line of block 1 goes like '37291', '53113', ..., 'TRUE'") {

    val items1 = this.block1.take(5)(1).split(',')
    assert(items1(0).toInt == 37291)
    assert(items1(1).toInt == 53113)
    assert(items1.last.toBoolean)
  }

  test("The scores of 1st data line from block 1 goes like '0.833333333333333', NaN, '1.0', NaN") {
    val scores1 = this.block1.take(5)(1).split(',').slice(2, 11)
      .map(x => if ("?".equals(x)) Double.NaN else x.toDouble)
    assert(scores1(0) == 0.833333333333333)
    assert(scores1(1).equals(Double.NaN))
    assert(scores1(2) == 1.0)

  }

  def parseToMatchRecord(line: String) = {
    val items = line.split(',')
    MatchRecord(items(0).toInt,
      items(1).toInt,
      items.slice(2, 11).map(x => if ("?".equals(x)) Double.NaN else x.toDouble),
      items.last.toBoolean)
  }

  test("The 2nd data line of block 1 goes like '39086','47614','1','?'...") {
    val items2 = this.block1.take(5)(2)

    val record2 = parseToMatchRecord(items2)
    assert(record2.id1.equals(39086))
    assert(record2.id2.equals(47614))
    assert(record2.matched)

  }

  test("The hisotram of block 1 data on 'matched' attribute shows 2093 trues and 572820 falses") {

    val data1 = block1.filter(x => !x.contains("id_1")) //data lines
    val cnt1 = data1.count()

    //@Note Do NOT call 'parseToMatchRecord' which is not serializable
    val records1 = data1.map(line => {
      val items = line.split(',')
      MatchRecord(items(0).toInt,
        items(1).toInt,
        items.slice(2, 11).map(x => if ("?".equals(x)) Double.NaN else x.toDouble),
        items.last.toBoolean)
    })
    val counts1 = records1.map(r => r.matched).countByValue()

    val trues = counts1.get(true)
    val falses = counts1.get(false)

    assert(trues.get + falses.get == cnt1)
    assert(trues.get == 2093)
    assert(falses.get == 572820)

  }
}

case class MatchRecord(id1: Int, id2: Int, scores: Array[Double], matched: Boolean) extends java.io.Serializable

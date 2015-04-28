package edu.banda.coel.business.netpic

import java.{util=>ju,lang=>jl}

import com.banda.network.domain.TopologicalNode
import edu.banda.coel.business.netpic.NetworkRunSpatialPicData
import edu.banda.coel.core.svg.SVGUtil
import java.awt.BasicStroke
import java.awt.Color
import org.apache.batik.svggen.SVGGraphics2D
import scala.collection.mutable.ListBuffer
import com.banda.core.domain.TimeRunTrace
import scala.collection.JavaConversions._
import java.awt.Graphics2D
import java.awt.image.RenderedImage
import java.awt.image.BufferedImage
import edu.banda.coel.core.util.ImageUtils
import com.banda.core.util.RenderedImageUtil
import java.awt.Dimension

/**
 * @author © Peter Banda
 * @since 2013
 */
trait NetworkRunSpatialPicGenerator[T]{

    val svgUtil = new SVGUtil

    def generateRenderedImages(
        transparent : Boolean,
        nodesInOrder : Iterable[TopologicalNode],
    	runTrace : Iterable[(BigDecimal, Seq[T])]
    ) : ju.List[RenderedImage]

    def generateRenderedImages(
        transparent : Boolean,
    	runTrace : TimeRunTrace[T, TopologicalNode]
    ) : ju.List[RenderedImage] = generateRenderedImages(transparent, runTrace.components, convertTimeRunTrace(runTrace))

    def generateRenderedStrings(
        nodesInOrder : Iterable[TopologicalNode],
    	runTrace : Iterable[(BigDecimal, Seq[T])],
    	format : String
    ) : ju.List[String] = generateRenderedImages(format != "jpg", nodesInOrder, runTrace).map(ImageUtils.toBase64String(_, format))

    def generateRenderedStrings(
    	runTrace : TimeRunTrace[T, TopologicalNode],
    	format : String
    ) : ju.List[String] = generateRenderedImages(format != "jpg", runTrace).map(ImageUtils.toBase64String(_, format))

    def generateSVGImages(
        nodesInOrder : Iterable[TopologicalNode],
    	runTrace : Iterable[(BigDecimal, Seq[T])]
    ) : ju.List[SVGGraphics2D]

    def generateSVGImages(
    	runTrace : TimeRunTrace[T, TopologicalNode]
    ) : ju.List[SVGGraphics2D] = generateSVGImages(runTrace.components, convertTimeRunTrace(runTrace))

    def generateSVGStrings(
        nodesInOrder : Iterable[TopologicalNode],
    	runTrace : Iterable[(BigDecimal, Seq[T])]
    ) : ju.List[String] = generateSVGImages(nodesInOrder, runTrace).map(svgUtil.exportToString(_, true))

    def generateSVGStrings(
    	runTrace : TimeRunTrace[T, TopologicalNode]
    ) : ju.List[String] = generateSVGImages(runTrace).map(svgUtil.exportToString(_, true))

    def generateImageData(
        runTrace : TimeRunTrace[T, TopologicalNode],
    	format : String,
    	base64 : Boolean
    ) : NetworkRunSpatialPicData = {
        val data = new NetworkRunSpatialPicData
        data.format = format

        if (format == "svg") {
            val images =  generateSVGImages(runTrace)
            if (!images.isEmpty) {
                val size = images.get(0).getSVGCanvasSize
                data.width = size.getWidth
            	data.height = size.getHeight
            	data.images = if (base64)
            	    images.map(svgUtil.exportToBase64WoSpaceString(_, true))
            	else
            	    images.map(svgUtil.exportToString(_, true)) 
            }
        } else {
            val images = generateRenderedImages(format != "jpg", runTrace)
            if (!images.isEmpty) {
            	data.width = images.get(0).getWidth
            	data.height = images.get(0).getHeight
            	data.images = if (base64)
            	    images.map(ImageUtils.toBase64String(_, format))
            	else
            	    images.map(RenderedImageUtil.getImageAsString(_, format))
            }
        }
        data
    }

    private def convertTimeRunTrace(runTrace : TimeRunTrace[T, _]) =
        runTrace.timeStates.map(timeState => ((timeState.time : Double) : BigDecimal, timeState.state : Seq[T]))
}

private abstract class AbstractNetworkRunSpatialPicGenerator[T, L](
    zoom : Int,
    palette : Map[T, Color],
    startx : Int = 0,
    starty : Int = 0
    ) extends NetworkRunSpatialPicGenerator[T] {

    override def generateRenderedImages(
        transparent : Boolean,
        nodesInOrder : Iterable[TopologicalNode],
    	runTrace : Iterable[(BigDecimal, Seq[T])]
    ) = {
        val images = new ListBuffer[RenderedImage]
        val locationMap = createLocationMap(nodesInOrder)
        def newCanvas() = {
            val canvasSizes = canvasSize(nodesInOrder, locationMap)
        	val image = new BufferedImage(canvasSizes._1, canvasSizes._2, if (transparent) BufferedImage.TYPE_INT_ARGB else BufferedImage.TYPE_INT_RGB)
        	images += image
        	val canvas = image.createGraphics
        	if (!transparent) {
        	    canvas.setColor(Color.WHITE)
                canvas.fillRect(0, 0, canvasSizes._1, canvasSizes._2);
            }
            canvas
        }

        generateGraphics(newCanvas)(nodesInOrder, locationMap, runTrace)
        images
    }

    override def generateSVGImages(
        nodesInOrder : Iterable[TopologicalNode],
    	runTrace : Iterable[(BigDecimal, Seq[T])]
    ) = {
        val images = new ListBuffer[SVGGraphics2D]
        val locationMap = createLocationMap(nodesInOrder)
        def newCanvas() = {
            val canvasSizes = canvasSize(nodesInOrder, locationMap)
        	val image = svgUtil.createBlankCanvas
        	image.setSVGCanvasSize(new Dimension(canvasSizes._1, canvasSizes._2))
        	images += image
        	image
        }

        generateGraphics(newCanvas)(nodesInOrder, locationMap, runTrace)
        images
    }

    private def generateGraphics[C <: Graphics2D](
        newCanvas : () => C)(
    	nodesInOrder : Iterable[TopologicalNode],
    	locationMap : Map[TopologicalNode, L],
    	runTrace : Iterable[(BigDecimal, Seq[T])]
    ) : Unit = {
        var canvas : C = null.asInstanceOf[C]

        def switchToNextCanvas = {
        	canvas = newCanvas()
    		canvas.setStroke(new BasicStroke(1))
        }

	    runTrace.foreach{ timeStates => {
	    	val time = timeStates._1.toInt
	    	if (isNextCanvasNeeded(time) || canvas == null) switchToNextCanvas
	        (nodesInOrder, timeStates._2).zipped.foreach{case (node, state) => {
	            val color = palette.get(state).get
	            if (color != null) {
	            	canvas.setPaint(color)
	            	drawPoint(canvas, time, node, locationMap)
	            }
	        }}
	    }}
	}

    private[netpic] def drawPoint(
    	canvas : Graphics2D,
    	time : Int,
    	node : TopologicalNode,
    	locations : Map[TopologicalNode, L])

    private[netpic] def createLocationMap(nodesInOrder : Iterable[TopologicalNode]) : Map[TopologicalNode, L]

    private[netpic] def isNextCanvasNeeded(time : Int) : Boolean

    private[netpic] def canvasSize(
    	nodesInOrder : Iterable[TopologicalNode],
    	locationMap : Map[TopologicalNode, L]
    ) : (Int, Int) 
}

private abstract class NetworkRun1DPicGenerator[T](
    zoom : Int,
    palette : Map[T, Color],
    singleCanvasHeight : Option[Int] = None,
    startx : Int = 0,
    starty : Int = 0
    ) extends AbstractNetworkRunSpatialPicGenerator[T, Int](zoom, palette, startx, starty) {

    override private[netpic] def drawPoint(
        canvas : Graphics2D,
        time : Int,
        node : TopologicalNode,
        xlocations : Map[TopologicalNode, Int]
    ) = {
        val y = if (singleCanvasHeight.isDefined) time % singleCanvasHeight.get else time
        canvas.fillRect(startx + xlocations(node) * zoom, starty + y * zoom, zoom, zoom)
    }

    //node.getLocation().get(0)

    override private[netpic] def isNextCanvasNeeded(time : Int) = if (singleCanvasHeight.isDefined) time % singleCanvasHeight.get == 0 else false

    override private[netpic] def canvasSize(
    	nodesInOrder : Iterable[TopologicalNode],
    	locationMap : Map[TopologicalNode, Int]
    ) = (startx + zoom * nodesInOrder.size, 
            starty + zoom * (if (singleCanvasHeight.isDefined) singleCanvasHeight.get else nodesInOrder.size))
}

final private class SpatialNetworkRun1DPicGenerator[T](
    zoom : Int,
    palette : Map[T, Color],
    singleCanvasHeight : Option[Int] = None,
    startx : Int = 0,
    starty : Int = 0
    ) extends NetworkRun1DPicGenerator[T](zoom, palette, singleCanvasHeight, startx, starty) {

    private[netpic] def createLocationMap(nodesInOrder : Iterable[TopologicalNode]) = 
        nodesInOrder.map(node => (node, node.getLocation().get(0) : Int)).toMap
}

final private class NonSpatialNetworkRun1DPicGenerator[T](
    zoom : Int,
    palette : Map[T, Color],
    singleCanvasHeight : Option[Int] = None,
    startx : Int = 0,
    starty : Int = 0
    ) extends NetworkRun1DPicGenerator[T](zoom, palette, singleCanvasHeight, startx, starty) {

    private[netpic] def createLocationMap(nodesInOrder : Iterable[TopologicalNode]) = nodesInOrder.zipWithIndex.toMap
}

private abstract class NetworkRun2DPicGenerator[T](
    zoom : Int,
    palette : Map[T, Color],
    startx : Int = 0,
    starty : Int = 0
    ) extends AbstractNetworkRunSpatialPicGenerator[T, (Int, Int)](zoom, palette, startx, starty) {

    override private[netpic] def drawPoint(
        canvas : Graphics2D,
        time : Int,
        node : TopologicalNode,
        xylocations : Map[TopologicalNode, (Int, Int)]
    ) = {
        val location = xylocations(node)
        canvas.fillRect(startx + location._1 * zoom, starty + location._2 * zoom, zoom, zoom)
    }

    override private[netpic] def isNextCanvasNeeded(time : Int) = true

    override private[netpic] def canvasSize(
    	nodesInOrder : Iterable[TopologicalNode],
    	locationMap : Map[TopologicalNode, (Int, Int)]
    ) = {
        val maxx = 1 + locationMap.maxBy(_._2._1)._2._1
        val maxy = 1 + locationMap.maxBy(_._2._2)._2._2
        (startx + zoom * maxx, starty + zoom * maxy)
    }
}

final private class SpatialNetworkRun2DPicGenerator[T](
    zoom : Int,
    palette : Map[T, Color],
    startx : Int = 0,
    starty : Int = 0
    ) extends NetworkRun2DPicGenerator[T](zoom, palette, startx, starty) {

    private[netpic] def createLocationMap(nodesInOrder : Iterable[TopologicalNode]) = 
        nodesInOrder.map(node => (node, (node.getLocation().get(0) : Int, node.getLocation().get(1) : Int))).toMap
}

final private class NonSpatialNetworkRun2DPicGenerator[T](
    zoom : Int,
    palette : Map[T, Color],
    startx : Int = 0,
    starty : Int = 0
    ) extends NetworkRun2DPicGenerator[T](zoom, palette, startx, starty) {

    private[netpic] def createLocationMap(nodesInOrder : Iterable[TopologicalNode]) = {
        val size = math.sqrt(nodesInOrder.size).toInt
        nodesInOrder.zipWithIndex.map{ case (node, index) => (node, (index % size, index / size))}.toMap
    }
}

object NetworkRunSpatialPicGenerator {

    private val booleanPalette = Map(true -> Color.BLACK, false -> null)
    private val javaBooleanPalette = Map(jl.Boolean.TRUE -> Color.BLACK, jl.Boolean.FALSE -> null)

    def createSpatialJavaBoolean1D(
        singleCanvasHeight : Option[Int] = None,
    	zoom : Int = 1,
    	startx : Int = 0,
    	starty : Int = 0
    ) : NetworkRunSpatialPicGenerator[jl.Boolean] = 
        new SpatialNetworkRun1DPicGenerator(zoom, javaBooleanPalette, singleCanvasHeight, startx, starty)

    def createNonSpatialJavaBoolean1D(
        singleCanvasHeight : Option[Int] = None,
    	zoom : Int = 1,
    	startx : Int = 0,
    	starty : Int = 0
    ) : NetworkRunSpatialPicGenerator[jl.Boolean] = 
        new NonSpatialNetworkRun1DPicGenerator(zoom, javaBooleanPalette, singleCanvasHeight, startx, starty)

    def createSpatialJavaBoolean2D(
    	zoom : Int = 1,
    	startx : Int = 0,
    	starty : Int = 0
    ) : NetworkRunSpatialPicGenerator[jl.Boolean] =
        new SpatialNetworkRun2DPicGenerator(zoom, javaBooleanPalette, startx, starty)

    def createNonSpatialJavaBoolean2D(
    	zoom : Int = 1,
    	startx : Int = 0,
    	starty : Int = 0
    ) : NetworkRunSpatialPicGenerator[jl.Boolean] =
        new NonSpatialNetworkRun2DPicGenerator(zoom, javaBooleanPalette, startx, starty)

    def createSpatialBoolean1D(
        singleCanvasHeight : Option[Int] = None,
    	zoom : Int = 1,
    	startx : Int = 0,
    	starty : Int = 0
    ) : NetworkRunSpatialPicGenerator[Boolean] =
        new SpatialNetworkRun1DPicGenerator(zoom, booleanPalette, singleCanvasHeight, startx, starty)

    def createNonSpatialBoolean1D(
        singleCanvasHeight : Option[Int] = None,
    	zoom : Int = 1,
    	startx : Int = 0,
    	starty : Int = 0
    ) : NetworkRunSpatialPicGenerator[Boolean] =
        new NonSpatialNetworkRun1DPicGenerator(zoom, booleanPalette, singleCanvasHeight, startx, starty)

    def createSpatialBoolean2D(
    	zoom : Int = 1,
    	startx : Int = 0,
    	starty : Int = 0
    ) : NetworkRunSpatialPicGenerator[Boolean] =
        new SpatialNetworkRun2DPicGenerator(zoom, booleanPalette, startx, starty)

    def createNonSpatialBoolean2D(
    	zoom : Int = 1,
    	startx : Int = 0,
    	starty : Int = 0
    ) : NetworkRunSpatialPicGenerator[Boolean] =
        new NonSpatialNetworkRun2DPicGenerator(zoom, booleanPalette, startx, starty)
}

object JavaNetworkRunSpatialPicGenerator {

    private def initSingleCanvasHeight(singleCanvasHeight : jl.Integer) =
        if (singleCanvasHeight == null) None else Some(singleCanvasHeight : Int)

    private def initZoom(zoom : jl.Integer) =
        if (zoom == null) 1 else zoom : Int

    def createSpatialJavaBoolean1D(
        singleCanvasHeight : jl.Integer,
    	zoom : jl.Integer
    ) = NetworkRunSpatialPicGenerator.createSpatialJavaBoolean1D(initSingleCanvasHeight(singleCanvasHeight), initZoom(zoom))

    def createSpatialJavaBoolean2D(
    	zoom : jl.Integer
    ) = NetworkRunSpatialPicGenerator.createSpatialJavaBoolean2D(initZoom(zoom))

    def createSpatialBoolean1D(
        singleCanvasHeight : jl.Integer,
    	zoom : jl.Integer
    ) = NetworkRunSpatialPicGenerator.createSpatialBoolean1D(initSingleCanvasHeight(singleCanvasHeight), initZoom(zoom))

    def createSpatialBoolean2D(
    	zoom : jl.Integer
    ) = NetworkRunSpatialPicGenerator.createSpatialBoolean2D(initZoom(zoom))

    def createNonSpatialJavaBoolean1D(
        singleCanvasHeight : jl.Integer,
    	zoom : jl.Integer
    ) = NetworkRunSpatialPicGenerator.createNonSpatialJavaBoolean1D(initSingleCanvasHeight(singleCanvasHeight), initZoom(zoom))

    def createNonSpatialJavaBoolean2D(
    	zoom : jl.Integer
    ) = NetworkRunSpatialPicGenerator.createNonSpatialJavaBoolean2D(initZoom(zoom))

    def createNonSpatialBoolean1D(
        singleCanvasHeight : jl.Integer,
    	zoom : jl.Integer
    ) = NetworkRunSpatialPicGenerator.createNonSpatialBoolean1D(initSingleCanvasHeight(singleCanvasHeight), initZoom(zoom))

    def createNonSpatialBoolean2D(
    	zoom : jl.Integer
    ) = NetworkRunSpatialPicGenerator.createNonSpatialBoolean2D(initZoom(zoom))
}
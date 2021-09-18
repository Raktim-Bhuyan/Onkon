package rb.example.onkon

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
//Path class  encapsulates compound geometric paths consisting of st line
//segments,quadratic curves and cubic curves.It can be drawn with canvas.drawPath
//(path,paint),either filled or stroke,or it can be used to draw text on a path

class DrawingView(context: Context,attrs:AttributeSet): View(context,attrs) {
    private var mDrawPath:CustomPath?=null //obj of class CustomPath
    private var mCanvasBitmap:Bitmap?=null
    private var mDrawPaint: Paint?=null
    private var mCanvasPaint:Paint?=null
    private var mBrushSize:Float=0.toFloat()
    //thickness nothing 0
    private var color= Color.BLACK
    private var canvas:Canvas?=null
    //line to make the drawing stay on the screen
    private val mPaths = ArrayList<CustomPath>()

    init{
        setUpDrawing()
    }
    private fun setUpDrawing(){
        mDrawPaint= Paint()
        mDrawPath = CustomPath(color,mBrushSize)
        mDrawPaint!!.color=color
        mDrawPaint!!.style=Paint.Style.STROKE
        mDrawPaint!!.strokeJoin= Paint.Join.ROUND
        mDrawPaint!!.strokeCap= Paint.Cap.ROUND
        mCanvasPaint= Paint(Paint.DITHER_FLAG)
        //mBrushSize = 20.toFloat()
    }
    //as onSizeChanged exists inside view class and our DrawingView
    //is inheriting from view class

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mCanvasBitmap = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888)
        canvas = Canvas(mCanvasBitmap!!)
    }

    //Change Canvas to Canvas? if fails
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(mCanvasBitmap!!, 0f, 0f, mCanvasPaint)

        for(path in mPaths){
            mDrawPaint!!.strokeWidth = path.brushThickness
            mDrawPaint!!.color = path.color
            canvas.drawPath(path, mDrawPaint!!)
        }
        if (!mDrawPath!!.isEmpty) {
            mDrawPaint!!.strokeWidth = mDrawPath!!.brushThickness
            mDrawPaint!!.color = mDrawPath!!.color
            canvas.drawPath(mDrawPath!!, mDrawPaint!!)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val touchX = event?.x
        val touchY = event?.y

        when(event?.action){
            MotionEvent.ACTION_DOWN ->{
                mDrawPath!!.color= color
                mDrawPath!!.brushThickness = mBrushSize

                mDrawPath!!.reset()
                mDrawPath!!.moveTo(touchX!!,touchY!!)
            }
            MotionEvent.ACTION_MOVE ->{
                mDrawPath!!.lineTo(touchX!!,touchY!!)
            }
            MotionEvent.ACTION_UP ->{
                mPaths.add(mDrawPath!!)
                mDrawPath = CustomPath(color,mBrushSize)
            }
            else -> return false
        }
        invalidate()

        return true

    }
    //Use of this long code is to fit different screen size
    fun setSizeForBrush(newSize:Float){
        mBrushSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP
        ,newSize,resources.displayMetrics)
        mDrawPaint!!.strokeWidth= mBrushSize
    }


    internal inner class CustomPath(var color:Int,
    var brushThickness:Float): Path() {

    }
}
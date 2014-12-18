package gustavo.mywine.app.model;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;


public class IntroductionView extends View {

    Paint paint; // for design options.
    Canvas canvas; // responsible for drawing images.
    Context contexto; //context

    public IntroductionView(Context context) {
        super(context);
        this.paint 		 	 = new Paint();
        this.contexto    	 = context;

    }

    @Override
    public void onDraw(Canvas canvas) {
        this.canvas	= canvas;
        drawBar();
    }

    public void drawBar(){

        this.paint.setStrokeWidth(20);
        this.paint.setAntiAlias(true);
        this.paint.setColor(Color.rgb(111, 53, 126));

        float radiusCircle = 80;
        float initialX = 260;
        float initialY = 200;

        float marginCircleLeftLine = 170;
        float marginCircleYLine = 160;
        float marginCircleXLine = 85;

        float initialPositionXCircleLine1 = initialX + marginCircleXLine;
        float initialPositionYCircleLine1 = initialY + marginCircleYLine;

        float initialPositionXCircleLine2 = initialPositionXCircleLine1 + marginCircleXLine;
        float initialPositionYCircleLine2 = initialPositionYCircleLine1 + marginCircleYLine;

        float initialPositionXCircleLine3 = initialPositionXCircleLine2 + marginCircleXLine;
        float initialPositionYCircleLine3 = initialPositionYCircleLine2 + marginCircleYLine;

        this.canvas.drawCircle(initialPositionXCircleLine1,initialPositionYCircleLine1,radiusCircle,this.paint);
        initialPositionXCircleLine1 = initialPositionXCircleLine1 + marginCircleLeftLine;
        this.canvas.drawCircle(initialPositionXCircleLine1,initialPositionYCircleLine1,radiusCircle,this.paint);
        initialPositionXCircleLine1 = initialPositionXCircleLine1 + marginCircleLeftLine;
        this.canvas.drawCircle(initialPositionXCircleLine1,initialPositionYCircleLine1,radiusCircle,this.paint);

        this.canvas.drawCircle(initialPositionXCircleLine2,initialPositionYCircleLine2,radiusCircle,this.paint);
        initialPositionXCircleLine2 = initialPositionXCircleLine2 + marginCircleLeftLine;
        this.canvas.drawCircle(initialPositionXCircleLine2,initialPositionYCircleLine2,radiusCircle,this.paint);

        this.canvas.drawCircle(initialPositionXCircleLine3,initialPositionYCircleLine3,radiusCircle,this.paint);

        float Xline1 = initialX - 65;
        float Yline1 = initialY + 100;
        float XMaxline1 = initialX + 255;
        float YMaxline1 = initialY + 655;

        float Xline2 = initialX + 575;
        float Yline2 = Yline1;
        float XMaxline2 = XMaxline1;
        float YMaxline2 = YMaxline1;

        float Xline3 = XMaxline2;
        float Yline3 = YMaxline2;
        float XMaxline3 = XMaxline2;
        float YMaxline3 = YMaxline2+200;

        float Xline4 = XMaxline3;
        float Yline4 = YMaxline3;
        float XMaxline4 = Xline4 - 120;
        float YMaxline4 = Yline4;

        float Xline5 = Xline4;
        float Yline5 = Yline4;
        float XMaxline5 = Xline5 + 120;
        float YMaxline5 = Yline5;

        this.canvas.drawLine(Xline1, Yline1,XMaxline1,YMaxline1,this.paint);
        this.canvas.drawLine(Xline2,Yline2,XMaxline2,YMaxline2,this.paint);
        this.canvas.drawLine(Xline3, Yline3,XMaxline3,YMaxline3,this.paint);
        this.canvas.drawLine(Xline4,Yline4,XMaxline4,YMaxline4,this.paint);
        this.canvas.drawLine(Xline5,Yline5,XMaxline5,YMaxline5,this.paint);

        this.paint.setColor(Color.rgb(181, 182, 49));

        float Xline6 = initialX +20 ;
        float Yline6 = initialY ;
        float XMaxline6 = Xline6 + 340;
        float YMaxline6 = Yline6;

        this.canvas.drawLine(Xline6,Yline6,XMaxline6,YMaxline6,this.paint);
        this.canvas.drawRect(XMaxline6,YMaxline6-100,XMaxline6+200,YMaxline6+50,this.paint);

        this.paint.setStyle(Paint.Style.FILL);

        this.paint.reset();
    }



}
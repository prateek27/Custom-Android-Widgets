package com.example.upclosesettings;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class SliderView extends View {
	protected KnobValuesChangedListener knobValuesChangedListener;	
	private Knob[] knobs = new Knob[2]; // array that holds the knobs
	private int balID = 0; // variable to know what knob is being dragged  
	private Point pointKnobStart, pointKnobEnd;
	private boolean initialisedSlider;
	private int startKnobValue, endKnobValue;//value to know the knob position e.g: 0,40,..,100
	private int startKnobValueTmp, endKnobValueTmp;//the position on the X axis
	private double sliderWidth, sliderHeight;//real size of the view that holds the slider
	private Paint paintSelected, paintNotSelected, paintText;
	private Rect rectangleSelected, rectangleNotSelected1, rectangleNotSelected2;
	private int startX, endX, startY, endY;//variables for the rectangles
	
	public SliderView(Context context) {
		super(context);
		setFocusable(true);
	}
	   
	public SliderView(Context context, AttributeSet attrs) {
		super(context, attrs);	   
		setFocusable(true);    
		pointKnobStart = new Point();
		pointKnobEnd = new Point();
		initialisedSlider = false;	 
	}

	@Override 
	protected void onDraw(Canvas canvas) {
		//background for slider
		canvas.drawColor(Color.parseColor("#ebebeb"));
		//initialise data for knobs , slider
		if(!initialisedSlider) {
			initialisedSlider = true;
			sliderWidth = getMeasuredWidth();
			sliderHeight = getMeasuredHeight();
			
			pointKnobStart.x = (int)((sliderWidth - sliderWidth/12)/6);
			pointKnobStart.y = (int)(sliderHeight/2.0);
			pointKnobEnd.x = (int)(7*(sliderWidth - sliderWidth/12)/12);
			pointKnobEnd.y = (int)(sliderHeight/2.0);
		    
			knobs[0] = new Knob(getContext(),R.drawable.knob, pointKnobStart);
			knobs[1] = new Knob(getContext(),R.drawable.knob, pointKnobEnd);
			knobs[0].setID(1);
			knobs[1].setID(2);
			setStartKnobValue(15);
			setEndKnobValue(65);
			knobValuesChanged(true, true, getStartKnobValue(), getEndKnobValue());
		
			paintSelected = new Paint();//the paint between knobs
			paintSelected.setColor(0xFF009900);
			paintNotSelected = new Paint();//the paint outside knobs
			paintNotSelected.setColor(0xFF66CC00);
			paintText = new Paint();//the paint for the slider data(the values) 
			paintText.setColor(Color.WHITE);
			
			//rectangles that define the line between and outside of knob
			rectangleSelected = new Rect();
			rectangleNotSelected1 = new Rect(); 
			rectangleNotSelected2 = new Rect();
		}
		
		/*//the slider data
		for(int i=0;i<=10;i++) {
			canvas.drawText(i*10+".", (float)((i+1)*(sliderWidth - sliderWidth/12)/12), (float)(sliderHeight/3), paintText);
			canvas.drawLine((float)((i+1)*(sliderWidth - sliderWidth/12)/12), (float)(sliderHeight/2.8), 
							(float)((i+1)*(sliderWidth - sliderWidth/12)/12), (float)(sliderHeight/2.3), paintText);
		}*/
		
		//rectangle between knobs
		startX = knobs[0].getX()+knobs[0].getBitmap().getWidth();
		endX = knobs[1].getX();
		startY = (int)(sliderHeight/2) + knobs[0].getY()/4;
		endY = (int)(sliderHeight/2) + (int)(3.0*knobs[0].getY()/4);
		rectangleSelected.set(startX, startY, endX, endY);
		
		//rectangle from left margin to first knob
		startX = (int)((sliderWidth - sliderWidth/12)/12);		
		endX = knobs[0].getX();
		rectangleNotSelected1.set(startX, startY, endX, endY);
		
		//rectangle from second knob to right margin
		startX = knobs[1].getX()+knobs[0].getBitmap().getWidth();		
		endX = (int)(11*(sliderWidth - sliderWidth/12)/12);
		rectangleNotSelected2.set(startX, startY, endX, endY);

		canvas.drawRect(rectangleSelected, paintSelected);
		canvas.drawRect(rectangleNotSelected1, paintNotSelected);
		canvas.drawRect(rectangleNotSelected2, paintNotSelected);		
		canvas.drawBitmap(knobs[0].getBitmap(), knobs[0].getX(), knobs[0].getY(), null);
		canvas.drawBitmap(knobs[1].getBitmap(), knobs[1].getX(), knobs[1].getY(), null);
	}
	    
	public boolean onTouchEvent(MotionEvent event) {
		int eventaction = event.getAction();     
		int X = (int)event.getX(); 
		int Y = (int)event.getY();
	
		switch (eventaction) { 
			//Touch down to check if the finger is on a knob
			case MotionEvent.ACTION_DOWN:
				balID = 0;
				for (Knob knob : knobs) {				
					// check if inside the bounds of the knob(circle)
					// get the centre of the knob
					int centerX = knob.getX() + knob.getBitmap().getHeight();
					int centerY = knob.getY() + knob.getBitmap().getHeight();
	        		// calculate the radius from the touch to the centre of the knob
					double radCircle  = Math.sqrt( (double) (((centerX-X)*(centerX-X)) + (centerY-Y)*(centerY-Y)));
	        		// if the radius is smaller then 33 (radius of a knob is 22), then it must be on the ball
					if (radCircle < 3*knob.getBitmap().getHeight()/2.0){
						balID = knob.getID();
					}
				}
				break; 
			
			 //Touch drag with the knob
	        case MotionEvent.ACTION_MOVE:	
	        	startKnobValueTmp = 0;
	        	endKnobValueTmp = 0;
	        	
	        	// left and right bound of slider and the difference
	        	int left_bound = (int)((sliderWidth - sliderWidth/12)/12);
	        	int right_bound = (int)(11*(sliderWidth - sliderWidth/12)/12);
	        	int delta_bound = right_bound - left_bound;
	        	
	        	// start and end value from the slider and the difference
	        	int val_max = 100;
	        	int val_min = 0;
	        	int delta_val = val_max-val_min;
	        	
	        	 /*
	        	  * The relative ratio which is later used
	        	  * to calculate the value of the knob using it's position on the X axis
	        	  */
	        	double ratio = (double)delta_bound/delta_val;
	        	
	        	int radiusKnob = knobs[0].getBitmap().getHeight()/2;

	        	 // knob position from centre
	        	int left_knob = knobs[0].getX() + radiusKnob;
	        	int right_knob = knobs[1].getX() + radiusKnob;
	        	
	        	// The calculated knob value using
	        	// the bounds, ratio, and actual knob position 
	        	startKnobValueTmp = (int)((val_max*ratio - right_bound + left_knob)/ratio);
	        	endKnobValueTmp = (int)((val_max*ratio - right_bound + right_knob)/ratio);

	        	//the first knob should be between the left bound and the second knob
	        	if(balID == 1) {
	        		if(X < left_bound) 
	        			X = left_bound;
	        		if(X >= knobs[1].getX()-radiusKnob)
	        			X = knobs[1].getX()-radiusKnob;
	        		knobs[0].setX(X-radiusKnob);
	        		
	        		//if the start value has changed then we pass it to the listener
	        		if(startKnobValueTmp != getStartKnobValue()) {
	        			setStartKnobValue(startKnobValueTmp);
		        		knobValuesChanged(true, false, getStartKnobValue(), getEndKnobValue());	            	
		        	}	            
	        	}
	        	//the second knob should between the first knob and the right bound
	        	if(balID == 2) {
	        		if(X > right_bound) 
	        			X = right_bound;
	        		if(X <= knobs[0].getX() + 3*radiusKnob)
	        			X = knobs[0].getX() + 3*radiusKnob;
	        		knobs[1].setX(X-radiusKnob);	
	        		
	        		//if the end value has changed then we pass it to the listener
		        	if(endKnobValueTmp != getEndKnobValue()) {
	        			setEndKnobValue(endKnobValueTmp);
		        		knobValuesChanged(false, true, getStartKnobValue(), getEndKnobValue());	            	
		        	}
	        	}
	        	break;
	        	
	        // Touch drop - actions after knob is released are performed   
	        case MotionEvent.ACTION_UP:
	        	break; 
		}	        
		
		 // Redraw the canvas
		invalidate();  
		return true; 
	}

	public int getStartKnobValue() {
		return startKnobValue;
	}

	public void setStartKnobValue(int startKnobValue) {
		this.startKnobValue = startKnobValue;
	}

	public int getEndKnobValue() {
		return endKnobValue;
	}

	public void setEndKnobValue(int endKnobValue) {
		this.endKnobValue = endKnobValue;
	}
	
	/**
	 * Interface which defines the knob values changed listener method
	 */
	public interface KnobValuesChangedListener {
		void onValuesChanged(boolean knobStartChanged, boolean knobEndChanged, int knobStart, int knobEnd);
	}
	
	/**
	 * Method applied to the instance of SliderView
	 */
	public void setOnKnobValuesChangedListener (KnobValuesChangedListener l) {
		knobValuesChangedListener = l;
	}
	    
	/**
	 * Method used by knob values changed listener
	 */
	private void knobValuesChanged(boolean knobStartChanged, boolean knobEndChanged, int knobStart, int knobEnd) {
		if(knobValuesChangedListener != null)
			knobValuesChangedListener.onValuesChanged(knobStartChanged, knobEndChanged, knobStart, knobEnd);
	}
}
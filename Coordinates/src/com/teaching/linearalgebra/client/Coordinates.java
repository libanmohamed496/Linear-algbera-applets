package com.teaching.linearalgebra.client;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.user.client.Random;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.RootPanel;

public class Coordinates implements EntryPoint {

private TextBox s11TextBox = new TextBox();
private TextBox s12TextBox = new TextBox();
private TextBox s21TextBox = new TextBox();
private TextBox s22TextBox = new TextBox();
private TextBox v1TextBox = new TextBox();
private TextBox v2TextBox = new TextBox();

private Label vS1Label = new Label();
private Label vS2Label = new Label();

private Label debugLabel = new Label();

private Canvas SCanvas;
private Context2d SContext;
private Canvas SCoCanvas;
private Context2d SCoContext;

private static final int canvasHeight = 250;
private static final int canvasWidth = 250;
private static final int scale = 25;

private final double[][] STD = new double[][] {{1,0},{0,1}};
private double[][] S = new double[][] {{1,0},{0,1}};
private double[][] v = new double[][] {{4},{2}};
private double[][] vS;

private boolean s1Active = false;
private boolean s2Active = false;
private boolean vSActive = false;

private boolean s1Selected = false;
private boolean s2Selected = false;
private boolean vSelected = false;

/*
 * I: Initialization functions
 */ 

public void onModuleLoad() {
  makePictures();
  makeLayout();
  update();
}

public void makePictures() {
  SCanvas = Canvas.createIfSupported();
  SContext = SCanvas.getContext2d();
  initializeCanvas(SCanvas,SContext);

  SCoCanvas = Canvas.createIfSupported();
  SCoContext = SCoCanvas.getContext2d();
  initializeCanvas(SCoCanvas,SCoContext);

  SCanvas.addMouseMoveHandler(new MouseMoveHandler() {
    public void onMouseMove(MouseMoveEvent event) {
      if(s1Selected || s2Selected || vSelected) {
        moveVector((MouseEvent) event);
        update();
      }
      else {
        double x=(double)(event.getX()-canvasWidth/2)/scale;
        double y=(double)(canvasHeight/2-event.getY())/scale;

        if(Math.pow(v[0][0]-x,2)+Math.pow(v[1][0]-y,2)<.16)
          vSActive = true;
        else if(Math.pow(S[0][0]-x,2)+Math.pow(S[1][0]-y,2)<.16)
          s1Active = true;
        else if(Math.pow(S[0][1]-x,2)+Math.pow(S[1][1]-y,2)<.16)
          s2Active = true;
        else {
          vSActive = false;
          s1Active = false;
          s2Active = false;
        }
      draw();
      }
    }
  });
}

public void makeLayout() {
  VerticalPanel mainPanel = new VerticalPanel();

  VerticalPanel SPanel = new VerticalPanel();
  Label SLabel = new Label("S as seen by the world:");  
  SPanel.add(SLabel);
  SPanel.add(SCanvas);

  VerticalPanel SCoPanel = new VerticalPanel();
  Label SCoLabel = new Label("The world as seen by S:");  
  SCoPanel.add(SCoLabel);
  SCoPanel.add(SCoCanvas);

  HorizontalPanel canvasPanel = new HorizontalPanel();
  canvasPanel.add(SPanel);
  Label whitespace = new Label("whitespace");
  canvasPanel.add(whitespace);
  canvasPanel.add(SCoPanel);

  HorizontalPanel BPanel = new HorizontalPanel();
  Label s1Label = new Label("s1: ");
  VerticalPanel LB1Panel = new VerticalPanel();
  Label b1Label = new Label("[");
  Label b2Label = new Label("[");
  LB1Panel.add(b1Label);
  LB1Panel.add(b2Label);
  VerticalPanel s1Panel= new VerticalPanel();
  s1Panel.add(s11TextBox);
  s1Panel.add(s12TextBox);
  VerticalPanel RB1Panel = new VerticalPanel();
  Label b3Label = new Label("]");
  Label b4Label = new Label("]");
  RB1Panel.add(b3Label);
  RB1Panel.add(b4Label);
  Label s2Label = new Label("s2: ");
  VerticalPanel LB2Panel = new VerticalPanel();
  Label b5Label = new Label("[");
  Label b6Label = new Label("[");
  LB2Panel.add(b5Label);
  LB2Panel.add(b6Label);
  VerticalPanel s2Panel= new VerticalPanel();
  s2Panel.add(s21TextBox);
  s2Panel.add(s22TextBox);
  VerticalPanel RB2Panel = new VerticalPanel();
  Label b7Label = new Label("]");
  Label b8Label = new Label("]");
  RB2Panel.add(b7Label);
  RB2Panel.add(b8Label);
  BPanel.add(s1Label);
  BPanel.add(LB1Panel);
  BPanel.add(s1Panel);
  BPanel.add(RB1Panel);
  BPanel.add(s2Label);
  BPanel.add(LB2Panel);
  BPanel.add(s2Panel);
  BPanel.add(RB2Panel);

  HorizontalPanel vPanel = new HorizontalPanel();
  Label vLabel = new Label("v: ");
  VerticalPanel vLBPanel = new VerticalPanel();
  Label b9Label = new Label("[");
  Label b10Label = new Label("[");
  vLBPanel.add(b9Label);
  vLBPanel.add(b10Label);
  VerticalPanel vCPanel = new VerticalPanel();
  vCPanel.add(v1TextBox);
  vCPanel.add(v2TextBox);
  VerticalPanel vRBPanel = new VerticalPanel();
  Label b11Label = new Label("]");
  Label b12Label = new Label("]");
  vRBPanel.add(b11Label);
  vRBPanel.add(b12Label);
  vPanel.add(vLabel);
  vPanel.add(vLBPanel);
  vPanel.add(vCPanel);
  vPanel.add(vRBPanel);
  Label vSLabel = new Label("v_S: ");
  VerticalPanel vSLBPanel = new VerticalPanel();
  Label b13Label = new Label("[");
  Label b14Label = new Label("[");
  vSLBPanel.add(b13Label);
  vSLBPanel.add(b14Label);
  VerticalPanel vSCPanel = new VerticalPanel();
  vSCPanel.add(vS1Label);
  vSCPanel.add(vS2Label);
  VerticalPanel vSRBPanel = new VerticalPanel();
  Label b15Label = new Label("]");
  Label b16Label = new Label("]");
  vSRBPanel.add(b15Label);
  vSRBPanel.add(b16Label);
  vPanel.add(vLabel);
  vPanel.add(vLBPanel);
  vPanel.add(vCPanel);
  vPanel.add(vRBPanel);
  vPanel.add(vSLabel);
  vPanel.add(vSLBPanel);
  vPanel.add(vSCPanel);
  vPanel.add(vSRBPanel);

  Button computeButton = new Button("Input");
  computeButton.addClickHandler(new ClickHandler() {
    public void onClick(ClickEvent event) {
      updateFromInput();
      update();
    }
  });

  VerticalPanel inputPanel = new VerticalPanel();
  Label inputLabel = new Label("Manual input:");
  inputPanel.add(inputLabel);
  inputPanel.add(BPanel);
  inputPanel.add(vPanel);
  inputPanel.add(computeButton);

  VerticalPanel metricsPanel = new VerticalPanel();
//  metricsPanel.add(vSLabel);

  mainPanel.add(canvasPanel);
  mainPanel.add(metricsPanel);
  mainPanel.add(inputPanel);
  mainPanel.add(debugLabel);

  RootPanel.get("applet").add(mainPanel);

//  s11TextBox.setFocus(true);
}

public void initializeCanvas(Canvas canvas, Context2d context) {
  canvas.setWidth(canvasWidth + "px");
  canvas.setCoordinateSpaceWidth(canvasWidth);
  canvas.setHeight(canvasHeight + "px");      
  canvas.setCoordinateSpaceHeight(canvasHeight);
  context.setTransform(1,0,0,-1,canvasWidth/2,canvasHeight/2);

  canvas.addMouseDownHandler(new MouseDownHandler() {
    public void onMouseDown(MouseDownEvent event) {
      makeActive();
      moveVector((MouseEvent) event);
    }
  });
  canvas.addMouseUpHandler(new MouseUpHandler() {
    public void onMouseUp(MouseUpEvent event) {
      clearSelected();
    }
  });
}

/*
 * II: Update functions
 */

public void moveVector(MouseEvent event) {
  if(s1Selected) {
    S[0][0]=(double)(event.getX()-canvasWidth/2)/scale;
    S[1][0]=(double)(canvasWidth/2-event.getY())/scale;
  } else if(s2Selected) {
    S[0][1]=(double)(event.getX()-canvasWidth/2)/scale;
    S[1][1]=(double)(canvasWidth/2-event.getY())/scale;
  } else if(vSelected) {
    v[0][0]=(double)(event.getX()-canvasWidth/2)/scale;
    v[1][0]=(double)(canvasWidth/2-event.getY())/scale;
  }
}

public void makeActive() {
  if(s1Active)
    s1Selected = true;
  else if(s2Active)
    s2Selected = true;
  else if(vSActive)
    vSelected = true;
}

public void clearSelected() {
  s1Selected = false;
  s2Selected = false;
  vSelected = false;
}

private void updateFromInput() {
  S[0][0] = Double.parseDouble(s11TextBox.getText());
  S[1][0] = Double.parseDouble(s12TextBox.getText());
  S[0][1] = Double.parseDouble(s21TextBox.getText());
  S[1][1] = Double.parseDouble(s22TextBox.getText());
  v[0][0] = Double.parseDouble(v1TextBox.getText());
  v[1][0] = Double.parseDouble(v2TextBox.getText());

  update();
  }

private void update() {
  compute();
  draw();
}

private void compute() {

  s11TextBox.setText(""+S[0][0]);
  s12TextBox.setText(""+S[1][0]);
  s21TextBox.setText(""+S[0][1]);
  s22TextBox.setText(""+S[1][1]);
  v1TextBox.setText(""+v[0][0]);
  v2TextBox.setText(""+v[1][0]);

  vS = multiply(inverse(S),v);
  vS1Label.setText(left(""+vS[0][0],5));
  vS2Label.setText(left(""+vS[1][0],5));
  }

private void draw() {
  drawContext(SContext,STD,S);
  drawContext(SCoContext,S,S);
  if(vSActive)
    drawHead(SContext,v[0][0],v[1][0]);
  else if(s1Active)
    drawHead(SContext,S[0][0],S[1][0]);
  else if(s2Active)
    drawHead(SContext,S[0][1],S[1][1]);
  }

/*
 * III: Drawing functions
 */

private void drawContext(Context2d context, double[][] perspective, double[][] basis) {
  context.clearRect(-canvasWidth/2,-canvasHeight/2,canvasWidth,canvasHeight);
  drawCoordinates(context,perspective,basis);
  drawAxes(context,perspective);
  drawVectors(context,perspective,basis,new String[] {"aqua","lime"});
  drawVectors(context,perspective,v,new String[] {"red"});
}

private void drawCoordinates(Context2d context, double[][] perspective, double[][] basis) {
  context.save();
  double[][] transform;
  transform = multiply(inverse(perspective),basis);
  context.transform(transform[0][0],transform[1][0],transform[0][1],transform[1][1],0,0);

  double[][] inverseTransform = inverse(transform);
  int maxX =  (int) Math.max(Math.abs(multiply(inverseTransform,new double[][] {{canvasWidth/(2*scale)},{canvasHeight/(2*scale)}})[0][0]),Math.abs(multiply(inverseTransform,new double[][] {{-canvasWidth/(2*scale)},{canvasHeight/(2*scale)}})[0][0]));
  int maxY = (int) Math.max(Math.abs(multiply(inverseTransform,new double[][] {{canvasWidth/(2*scale)},{canvasHeight/(2*scale)}})[1][0]),Math.abs(multiply(inverseTransform,new double[][] {{-canvasWidth/(2*scale)},{canvasHeight/(2*scale)}})[1][0]));

  context.setStrokeStyle("grey");
  context.beginPath();
  for(int j=-maxX; j<=maxX; j++) {
    context.moveTo(j*scale,-(maxY+1)*scale);
    context.lineTo(j*scale,(maxY+1)*scale);
    }
  for(int j=-maxY; j<=maxY; j++) {
    context.moveTo(-(maxX+1)*scale,j*scale);
    context.lineTo((maxX+1)*scale,j*scale);
    }
  context.closePath();
  context.stroke();

  context.restore();
  }

private void drawAxes(Context2d context,double[][] perspective) {
  context.save();
  double[][] transform = inverse(perspective);
  context.transform(transform[0][0],transform[1][0],transform[0][1],transform[1][1],0,0);

  double[][] inverseTransform = inverse(transform);
  int maxX =  (int) (1+Math.max(Math.abs(multiply(inverseTransform,new double[][] {{canvasWidth/(2*scale)},{canvasHeight/(2*scale)}})[0][0]),Math.abs(multiply(inverseTransform,new double[][] {{-canvasWidth/(2*scale)},{canvasHeight/(2*scale)}})[0][0])));
  int maxY = (int) (1+Math.max(Math.abs(multiply(inverseTransform,new double[][] {{canvasWidth/(2*scale)},{canvasHeight/(2*scale)}})[1][0]),Math.abs(multiply(inverseTransform,new double[][] {{-canvasWidth/(2*scale)},{canvasHeight/(2*scale)}})[1][0])));


  context.setStrokeStyle("black");
  context.beginPath();
  context.moveTo(-maxX*scale,0);
  context.lineTo(maxX*scale,0);
  context.moveTo(0,-maxY*scale);
  context.lineTo(0,maxY*scale);
  context.closePath();
  context.stroke();
  context.restore();
  }

private void drawVectors(Context2d context, double[][] perspective, double[][] vectors, String[] colors) {
  context.save();
  double[][] transform = inverse(perspective);
  context.transform(transform[0][0],transform[1][0],transform[0][1],transform[1][1],0,0);

  for(int j=0; j<vectors.length; j++) {
    context.setStrokeStyle(colors[j]);
    context.beginPath();
    context.moveTo(0,0);
    context.lineTo(vectors[0][j]*scale,vectors[1][j]*scale);
    context.closePath();
    context.stroke();
  }
  context.restore();
}

public void drawHead(Context2d context, double x, double y) {
//  debugLabel.setText(""+x+","+y);
  context.save();
  context.setFillStyle("yellow");
  context.beginPath();
  context.arc(x*scale,y*scale,3,0,2*Math.PI);
  context.closePath();
  context.fill();
  context.restore();
}

/*
 * IV: Utility functions
 */

private double[][] inverse(double[][] A) {
  double det = det(A);
  return new double[][] {{A[1][1]/det,-A[0][1]/det},{-A[1][0]/det,A[0][0]/det}};
  }

private double det(double[][] A) {
  return A[0][0]*A[1][1]-A[0][1]*A[1][0];
  }

private double[][] multiply(double[][] A, double[][] B) {
  double[][] AB = new double[A.length][B[0].length];
  for(int j=0; j<A.length; j++)
    for(int k=0; k<B[0].length; k++) {
      double ABjk=0;
      for(int l=0; l<A[0].length; l++)
        ABjk+=A[j][l]*B[l][k];
      AB[j][k] = ABjk;
    }
  return AB;
  }

private boolean equalArrays(double[][] A, double[][] B) {
  for(int j=0; j<A.length; j++)
    for(int k=0; k<A[j].length; k++)
      if(A[j][k]!=B[j][k])
        return false;
  return true;
}

private String writeDoubleArray(double[][] A) {
  String out = "[";
  for(int j=0; j<A.length; j++) {
    out+="[";
    for(int k=0; k<A[0].length; k++)
      out+=Double.toString(A[j][k]).substring(0,4)+",";
    out+="],";
    }
  out+="]";
  return out;
}

private String left(String s, int index) {
  return s.substring(0,index);
}


}

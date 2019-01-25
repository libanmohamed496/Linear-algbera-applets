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

public class TransitionMatrices implements EntryPoint {

private TextBox s11TextBox = new TextBox();
private TextBox s12TextBox = new TextBox();
private TextBox s21TextBox = new TextBox();
private TextBox s22TextBox = new TextBox();
private TextBox t11TextBox = new TextBox();
private TextBox t12TextBox = new TextBox();
private TextBox t21TextBox = new TextBox();
private TextBox t22TextBox = new TextBox();
private TextBox v1TextBox = new TextBox();
private TextBox v2TextBox = new TextBox();

private Label vS1Label = new Label();
private Label vS2Label = new Label();
private Label vT1Label = new Label();
private Label vT2Label = new Label();
private Label PTS11Label = new Label();
private Label PTS12Label = new Label();
private Label PTS21Label = new Label();
private Label PTS22Label = new Label();

private Label debugLabel = new Label();

private Canvas SCanvas;
private Context2d SContext;
private Canvas TCanvas;
private Context2d TContext;
private Canvas SCoCanvas;
private Context2d SCoContext;
private Canvas TCoCanvas;
private Context2d TCoContext;

private static final int canvasHeight = 250;
private static final int canvasWidth = 250;
private static final int scale = 25;

private final double[][] STD = new double[][] {{1,0},{0,1}};
private double[][] S = new double[][] {{1,0},{0,1}};
private double[][] T = new double[][] {{1,0},{0,1}};
private double[][] v = new double[][] {{4},{2}};
private double[][] vS;
private double[][] vT;
private double[][] PTS;

private boolean s1Active = false;
private boolean s2Active = false;
private boolean t1Active = false;
private boolean t2Active = false;
private boolean vSActive = false;
private boolean vTActive = false;

private boolean s1Selected = false;
private boolean s2Selected = false;
private boolean t1Selected = false;
private boolean t2Selected = false;
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

  TCanvas = Canvas.createIfSupported();
  TContext = TCanvas.getContext2d();
  initializeCanvas(TCanvas,TContext);

  SCoCanvas = Canvas.createIfSupported();
  SCoContext = SCoCanvas.getContext2d();
  initializeCanvas(SCoCanvas,SCoContext);

  TCoCanvas = Canvas.createIfSupported();
  TCoContext = TCoCanvas.getContext2d();
  initializeCanvas(TCoCanvas,TCoContext);

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
  TCanvas.addMouseMoveHandler(new MouseMoveHandler() {
    public void onMouseMove(MouseMoveEvent event) {
      if(t1Selected || t2Selected || vSelected) {
        moveVector((MouseEvent) event);
        update();
      }
      else {
        double x=(double)(event.getX()-canvasWidth/2)/scale;
        double y=(double)(canvasHeight/2-event.getY())/scale;

        if(Math.pow(v[0][0]-x,2)+Math.pow(v[1][0]-y,2)<.16)
          vTActive = true;
        else if(Math.pow(T[0][0]-x,2)+Math.pow(T[1][0]-y,2)<.16)
          t1Active = true;
        else if(Math.pow(T[0][1]-x,2)+Math.pow(T[1][1]-y,2)<.16)
          t2Active = true;
        else {
          vTActive = false;
          t1Active = false;
          t2Active = false;
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
  Label SCoLabel = new Label("The world as seen by S:");  
  SPanel.add(SCoLabel);
  SPanel.add(SCoCanvas);

  VerticalPanel TPanel = new VerticalPanel();
  Label TLabel = new Label("T as seen by the world:");
  TPanel.add(TLabel);
  TPanel.add(TCanvas);
  Label TCoLabel = new Label("The world as seen by T:");  
  TPanel.add(TCoLabel);
  TPanel.add(TCoCanvas);

  HorizontalPanel canvasPanel = new HorizontalPanel();
  canvasPanel.add(SPanel);
  canvasPanel.add(new Label("whitespace"));
  canvasPanel.add(TPanel);

  HorizontalPanel SIPanel = new HorizontalPanel();
  Label s1Label = new Label("s1: ");
  VerticalPanel s1LBPanel = new VerticalPanel();
  Label b1Label = new Label("[");
  Label b2Label = new Label("[");
  s1LBPanel.add(b1Label);
  s1LBPanel.add(b2Label);
  VerticalPanel s1CPanel = new VerticalPanel();
  s1CPanel.add(s11TextBox);
  s1CPanel.add(s12TextBox);
  VerticalPanel s1RBPanel = new VerticalPanel();
  Label b3Label = new Label("]");
  Label b4Label = new Label("]");
  s1RBPanel.add(b3Label);
  s1RBPanel.add(b4Label);
  SIPanel.add(s1Label);
  SIPanel.add(s1LBPanel);
  SIPanel.add(s1CPanel);
  SIPanel.add(s1RBPanel);

  Label s2Label = new Label("s2: ");
  VerticalPanel s2LBPanel = new VerticalPanel();
  Label b5Label = new Label("[");
  Label b6Label = new Label("[");
  s2LBPanel.add(b5Label);
  s2LBPanel.add(b6Label);
  VerticalPanel s2CPanel = new VerticalPanel();
  s2CPanel.add(s21TextBox);
  s2CPanel.add(s22TextBox);
  VerticalPanel s2RBPanel = new VerticalPanel();
  Label b7Label = new Label("]");
  Label b8Label = new Label("]");
  s2RBPanel.add(b7Label);
  s2RBPanel.add(b8Label);
  SIPanel.add(s2Label);
  SIPanel.add(s2LBPanel);
  SIPanel.add(s2CPanel);
  SIPanel.add(s2RBPanel);

  HorizontalPanel TIPanel = new HorizontalPanel();
  Label t1Label = new Label("t1: ");
  VerticalPanel t1LBPanel = new VerticalPanel();
  Label b9Label = new Label("[");
  Label b10Label = new Label("[");
  t1LBPanel.add(b9Label);
  t1LBPanel.add(b10Label);
  VerticalPanel t1CPanel = new VerticalPanel();
  t1CPanel.add(t11TextBox);
  t1CPanel.add(t12TextBox);
  VerticalPanel t1RBPanel = new VerticalPanel();
  Label b11Label = new Label("]");
  Label b12Label = new Label("]");
  t1RBPanel.add(b11Label);
  t1RBPanel.add(b12Label);
  TIPanel.add(t1Label);
  TIPanel.add(t1LBPanel);
  TIPanel.add(t1CPanel);
  TIPanel.add(t1RBPanel);

  Label t2Label = new Label("t2: ");
  VerticalPanel t2LBPanel = new VerticalPanel();
  Label b13Label = new Label("[");
  Label b14Label = new Label("[");
  t2LBPanel.add(b13Label);
  t2LBPanel.add(b14Label);
  VerticalPanel t2CPanel = new VerticalPanel();
  t2CPanel.add(t21TextBox);
  t2CPanel.add(t22TextBox);
  VerticalPanel t2RBPanel = new VerticalPanel();
  Label b15Label = new Label("]");
  Label b16Label = new Label("]");
  t2RBPanel.add(b15Label);
  t2RBPanel.add(b16Label);
  TIPanel.add(t2Label);
  TIPanel.add(t2LBPanel);
  TIPanel.add(t2CPanel);
  TIPanel.add(t2RBPanel);

  HorizontalPanel vPanel = new HorizontalPanel();
  Label vLabel = new Label("v: ");
  VerticalPanel vLBPanel = new VerticalPanel();
  Label b17Label = new Label("[");
  Label b18Label = new Label("[");
  vLBPanel.add(b17Label);
  vLBPanel.add(b18Label);
  VerticalPanel vCPanel = new VerticalPanel();
  vCPanel.add(v1TextBox);
  vCPanel.add(v2TextBox);
  VerticalPanel vRBPanel = new VerticalPanel();
  Label b19Label = new Label("]");
  Label b20Label = new Label("]");
  vRBPanel.add(b19Label);
  vRBPanel.add(b20Label);
  vPanel.add(vLabel);
  vPanel.add(vLBPanel);
  vPanel.add(vCPanel);
  vPanel.add(vRBPanel);

  Label vSLabel = new Label("[v]_S: ");
  VerticalPanel vSLBPanel = new VerticalPanel();
  Label b21Label = new Label("[");
  Label b22Label = new Label("[");
  vSLBPanel.add(b21Label);
  vSLBPanel.add(b22Label);
  VerticalPanel vSCPanel = new VerticalPanel();
  vSCPanel.add(vS1Label);
  vSCPanel.add(vS2Label);
  VerticalPanel vSRBPanel = new VerticalPanel();
  Label b23Label = new Label("]");
  Label b24Label = new Label("]");
  vSRBPanel.add(b23Label);
  vSRBPanel.add(b24Label);
  vPanel.add(vSLabel);
  vPanel.add(vSLBPanel);
  vPanel.add(vSCPanel);
  vPanel.add(vSRBPanel);

  Label vTLabel = new Label("[v]_T: ");
  VerticalPanel vTLBPanel = new VerticalPanel();
  Label b25Label = new Label("[");
  Label b26Label = new Label("[");
  vTLBPanel.add(b25Label);
  vTLBPanel.add(b26Label);
  VerticalPanel vTCPanel = new VerticalPanel();
  vTCPanel.add(vT1Label);
  vTCPanel.add(vT2Label);
  VerticalPanel vTRBPanel = new VerticalPanel();
  Label b27Label = new Label("]");
  Label b28Label = new Label("]");
  vTRBPanel.add(b27Label);
  vTRBPanel.add(b28Label);
  vPanel.add(vTLabel);
  vPanel.add(vTLBPanel);
  vPanel.add(vTCPanel);
  vPanel.add(vTRBPanel);

  HorizontalPanel PTSPanel = new HorizontalPanel();
  Label PTSLabel = new Label("P_TS: ");
  VerticalPanel PTSLBPanel = new VerticalPanel();
  Label b29Label = new Label("[");
  Label b30Label = new Label("[");
  PTSLBPanel.add(b29Label);
  PTSLBPanel.add(b30Label);
  VerticalPanel PTSC1Panel = new VerticalPanel();
  PTSC1Panel.add(PTS11Label);
  PTSC1Panel.add(PTS21Label);
  VerticalPanel PTSC2Panel = new VerticalPanel();
  PTSC2Panel.add(PTS12Label);
  PTSC2Panel.add(PTS22Label);
  VerticalPanel PTSRBPanel = new VerticalPanel();
  Label b31Label = new Label("]");
  Label b32Label = new Label("]");
  PTSRBPanel.add(b31Label);
  PTSRBPanel.add(b32Label);
  PTSPanel.add(PTSLabel);
  PTSPanel.add(PTSLBPanel);
  PTSPanel.add(PTSC1Panel);
  PTSPanel.add(PTSC2Panel);
  PTSPanel.add(PTSRBPanel);



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
  inputPanel.add(SIPanel);
  inputPanel.add(TIPanel);
  inputPanel.add(vPanel);
  inputPanel.add(PTSPanel);
  inputPanel.add(computeButton);

//  VerticalPanel metricsPanel = new VerticalPanel();

  mainPanel.add(canvasPanel);
//  mainPanel.add(metricsPanel);
  mainPanel.add(inputPanel);
  mainPanel.add(debugLabel);

  RootPanel.get("transitionmatrices").add(mainPanel);

  s11TextBox.setFocus(true);
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
  } else if(t1Selected) {
    T[0][0]=(double)(event.getX()-canvasWidth/2)/scale;
    T[1][0]=(double)(canvasWidth/2-event.getY())/scale;
  } else if(t2Selected) {
    T[0][1]=(double)(event.getX()-canvasWidth/2)/scale;
    T[1][1]=(double)(canvasWidth/2-event.getY())/scale;
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
  else if(t1Active)
    t1Selected = true;
  else if(t2Active)
    t2Selected = true;
  else if(vSActive)
    vSelected = true;
  else if(vTActive)
    vSelected = true;
}

public void clearSelected() {
  s1Selected = false;
  s2Selected = false;
  t1Selected = false;
  t2Selected = false;
  vSelected = false;
}

private void updateFromInput() {
  S[0][0] = Double.parseDouble(s11TextBox.getText());
  S[1][0] = Double.parseDouble(s12TextBox.getText());
  S[0][1] = Double.parseDouble(s21TextBox.getText());
  S[1][1] = Double.parseDouble(s22TextBox.getText());
  T[0][0] = Double.parseDouble(t11TextBox.getText());
  T[1][0] = Double.parseDouble(t12TextBox.getText());
  T[0][1] = Double.parseDouble(t21TextBox.getText());
  T[1][1] = Double.parseDouble(t22TextBox.getText());
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
  t11TextBox.setText(""+T[0][0]);
  t12TextBox.setText(""+T[1][0]);
  t21TextBox.setText(""+T[0][1]);
  t22TextBox.setText(""+T[1][1]);
  v1TextBox.setText(""+v[0][0]);
  v2TextBox.setText(""+v[1][0]);

  vS = multiply(inverse(S),v);
  vT = multiply(inverse(T),v);
  PTS = multiply(inverse(T),S);
  vS1Label.setText(left(""+vS[0][0],5));
  vS2Label.setText(left(""+vS[1][0],5));
  vT1Label.setText(left(""+vT[0][0],5));
  vT2Label.setText(left(""+vT[1][0],5));
  PTS11Label.setText(left(""+PTS[0][0],5));
  PTS12Label.setText(left(", "+PTS[0][1],5));
  PTS21Label.setText(left(""+PTS[1][0],5));
  PTS22Label.setText(left(", "+PTS[1][1],5));
  }

private void draw() {
  drawContext(SContext,STD,S);
  drawContext(TContext,STD,T);
  drawContext(SCoContext,S,S);
  drawContext(TCoContext,T,T);
  if(vSActive)
    drawHead(SContext,v[0][0],v[1][0]);
  else if(vTActive)
    drawHead(TContext,v[0][0],v[1][0]);
  else if(s1Active)
    drawHead(SContext,S[0][0],S[1][0]);
  else if(s2Active)
    drawHead(SContext,S[0][1],S[1][1]);
  else if(t1Active)
    drawHead(TContext,T[0][0],T[1][0]);
  else if(t2Active)
    drawHead(TContext,T[0][1],T[1][1]);
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
 // debugLabel.setText(""+x+","+y);
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

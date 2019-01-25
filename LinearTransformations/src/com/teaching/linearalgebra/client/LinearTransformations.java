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

public class LinearTransformations implements EntryPoint {

private TextBox Ts11TextBox = new TextBox();
private TextBox Ts12TextBox = new TextBox();
private TextBox Ts21TextBox = new TextBox();
private TextBox Ts22TextBox = new TextBox();
private TextBox v1TextBox = new TextBox();
private TextBox v2TextBox = new TextBox();

private Label Tv1Label = new Label();
private Label Tv2Label = new Label();

private Label debugLabel = new Label();

private Canvas DCanvas;
private Context2d DContext;
private Canvas DCoCanvas;
private Context2d DCoContext;

private static final int canvasHeight = 250;
private static final int canvasWidth = 250;
private static final int scale = 25;

private final double[][] STD = new double[][] {{1,0},{0,1}};
private double[][] T = new double[][] {{1,0},{0,1}};
private double[][] v = new double[][] {{4},{2}};
private double[][] Tv = multiply(T,v);

private boolean Ts1Active = false;
private boolean Ts2Active = false;
private boolean vActive = false;

private boolean Ts1Selected = false;
private boolean Ts2Selected = false;
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
  DCanvas = Canvas.createIfSupported();
  DContext = DCanvas.getContext2d();
  initializeCanvas(DCanvas,DContext);

  DCoCanvas = Canvas.createIfSupported();
  DCoContext = DCoCanvas.getContext2d();
  initializeCanvas(DCoCanvas,DCoContext);

  DCanvas.addMouseMoveHandler(new MouseMoveHandler() {
    public void onMouseMove(MouseMoveEvent event) {
      if(vSelected) {
        moveVector((MouseEvent) event);
        update();
      }
      else {
        double x=(double)(event.getX()-canvasWidth/2)/scale;
        double y=(double)(canvasHeight/2-event.getY())/scale;

        if(Math.pow(v[0][0]-x,2)+Math.pow(v[1][0]-y,2)<.16)
          vActive = true;
        else {
          vActive = false;
          Ts1Active = false;
          Ts2Active = false;
        }
      draw();
      }
    }
  });

    DCoCanvas.addMouseMoveHandler(new MouseMoveHandler() {
    public void onMouseMove(MouseMoveEvent event) {
      if(Ts1Selected || Ts2Selected) {
        moveVector((MouseEvent) event);
        update();
      }
      else {
        double x=(double)(event.getX()-canvasWidth/2)/scale;
        double y=(double)(canvasHeight/2-event.getY())/scale;

        if(Math.pow(T[0][0]-x,2)+Math.pow(T[1][0]-y,2)<.16)
          Ts1Active = true;
        else if(Math.pow(T[0][1]-x,2)+Math.pow(T[1][1]-y,2)<.16)
          Ts2Active = true;
        else {
          vActive = false;
          Ts1Active = false;
          Ts2Active = false;
        }
      draw();
      }
    }
  });

}

public void makeLayout() {
  VerticalPanel mainPanel = new VerticalPanel();

  VerticalPanel DPanel = new VerticalPanel();
  Label DLabel = new Label("The domain of T:");  
  DPanel.add(DLabel);
  DPanel.add(DCanvas);

  VerticalPanel DCoPanel = new VerticalPanel();
  Label DCoLabel = new Label("The codomain of T:");  
  DCoPanel.add(DCoLabel);
  DCoPanel.add(DCoCanvas);

  HorizontalPanel canvasPanel = new HorizontalPanel();
  canvasPanel.add(DPanel);
  Label whitespace = new Label("whitespace");
  canvasPanel.add(whitespace);
  canvasPanel.add(DCoPanel);

  HorizontalPanel APanel = new HorizontalPanel();
  Label ALabel= new Label("A = ");
  VerticalPanel LBPanel = new VerticalPanel();
  VerticalPanel C1Panel = new VerticalPanel();
  VerticalPanel C2Panel = new VerticalPanel();
  VerticalPanel RBPanel = new VerticalPanel();
  Label b1Label = new Label("[");
  Label b2Label = new Label("]");
  Label b3Label = new Label("[");
  Label b4Label = new Label("]");
  LBPanel.add(b1Label);
  LBPanel.add(b3Label);
  C1Panel.add(Ts11TextBox);
  C2Panel.add(Ts21TextBox);
  C1Panel.add(Ts12TextBox);
  C2Panel.add(Ts22TextBox);
  RBPanel.add(b2Label);
  RBPanel.add(b4Label);
  APanel.add(ALabel);
  APanel.add(LBPanel);
  APanel.add(C1Panel);
  APanel.add(C2Panel);
  APanel.add(RBPanel);

  HorizontalPanel vPanel = new HorizontalPanel();
  Label vLabel = new Label("v = ");
  VerticalPanel vLBPanel = new VerticalPanel();
  VerticalPanel CPanel = new VerticalPanel();
  VerticalPanel vRBPanel = new VerticalPanel();
  Label b5Label = new Label("[");
  Label b6Label = new Label("]");
  Label b7Label = new Label("[");
  Label b8Label = new Label("]");
  vLBPanel.add(b5Label);
  vLBPanel.add(b7Label);
  CPanel.add(v1TextBox);
  CPanel.add(v2TextBox);
  vRBPanel.add(b6Label);
  vRBPanel.add(b8Label);
  vPanel.add(vLabel);
  vPanel.add(vLBPanel);
  vPanel.add(CPanel);
  vPanel.add(vRBPanel);
  Label TvLabel = new Label("A(v) = ");
  VerticalPanel TvLBPanel = new VerticalPanel();
  VerticalPanel CTPanel = new VerticalPanel();
  VerticalPanel TvRBPanel = new VerticalPanel();
  Label b9Label = new Label("[");
  Label b10Label = new Label("]");
  Label b11Label = new Label("[");
  Label b12Label = new Label("]");
  TvLBPanel.add(b9Label);
  TvLBPanel.add(b11Label);
  CTPanel.add(Tv1Label);
  CTPanel.add(Tv2Label);
  TvRBPanel.add(b10Label);
  TvRBPanel.add(b12Label);
  vPanel.add(TvLabel);
  vPanel.add(TvLBPanel);
  vPanel.add(CTPanel);
  vPanel.add(TvRBPanel);



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
  inputPanel.add(APanel);
  inputPanel.add(vPanel);
  inputPanel.add(computeButton);

  VerticalPanel metricsPanel = new VerticalPanel();

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
  if(Ts1Selected) {
    T[0][0]=(double)(event.getX()-canvasWidth/2)/scale;
    T[1][0]=(double)(canvasWidth/2-event.getY())/scale;
  } else if(Ts2Selected) {
    T[0][1]=(double)(event.getX()-canvasWidth/2)/scale;
    T[1][1]=(double)(canvasWidth/2-event.getY())/scale;
  } else if(vSelected) {
    v[0][0]=(double)(event.getX()-canvasWidth/2)/scale;
    v[1][0]=(double)(canvasWidth/2-event.getY())/scale;
  }
}

public void makeActive() {
  if(Ts1Active)
    Ts1Selected = true;
  else if(Ts2Active)
    Ts2Selected = true;
  else if(vActive)
    vSelected = true;
}

public void clearSelected() {
  Ts1Selected = false;
  Ts2Selected = false;
  vSelected = false;
}

private void updateFromInput() {
  T[0][0] = Double.parseDouble(Ts11TextBox.getText());
  T[1][0] = Double.parseDouble(Ts12TextBox.getText());
  T[0][1] = Double.parseDouble(Ts21TextBox.getText());
  T[1][1] = Double.parseDouble(Ts22TextBox.getText());
  v[0][0] = Double.parseDouble(v1TextBox.getText());
  v[1][0] = Double.parseDouble(v2TextBox.getText());

  update();
  }

private void update() {
  compute();
  draw();
}

private void compute() {

  Ts11TextBox.setText(""+T[0][0]);
  Ts12TextBox.setText(""+T[1][0]);
  Ts21TextBox.setText(""+T[0][1]);
  Ts22TextBox.setText(""+T[1][1]);
  v1TextBox.setText(""+v[0][0]);
  v2TextBox.setText(""+v[1][0]);
  Tv=multiply(T,v);
  Tv1Label.setText(left(""+Tv[0][0],5));
  Tv2Label.setText(left(""+Tv[1][0],5));
  }

private void draw() {
  drawContext(DContext,STD);
  drawVectors(DContext,STD,STD,new String[] {"aqua","lime"});
  drawVectors(DContext,STD,v,new String[] {"red"});
  drawContext(DCoContext,STD);
  drawVectors(DCoContext,STD,T,new String[] {"aqua","lime"});
  drawVectors(DCoContext,STD,Tv,new String[] {"red"});
  if(vActive)
    drawHead(DContext,v[0][0],v[1][0]);
  else if(Ts1Active)
    drawHead(DCoContext,T[0][0],T[1][0]);
  else if(Ts2Active)
    drawHead(DCoContext,T[0][1],T[1][1]);
  }

/*
 * III: Drawing functions
 */

private void drawContext(Context2d context, double[][] perspective) {
  context.clearRect(-canvasWidth/2,-canvasHeight/2,canvasWidth,canvasHeight);
  drawCoordinates(context,perspective,STD);
  drawAxes(context,perspective);
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
  //debugLabel.setText(""+x+","+y);
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
  return s.substring(0, index);
}


}

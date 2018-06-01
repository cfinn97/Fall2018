import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.*;
import com.jogamp.opengl.util.gl2.GLUT;

/**
 * CPSC 424, Fall 2015, Lab 4: Some objects in 3D. The arrow keys can be used to
 * rotate the object. The number keys 1 through 6 select the object. The space
 * bar toggles the use of anaglyph stereo.
 */
public class Lab4 extends GLJPanel implements GLEventListener, KeyListener {

	/**
	 * A main routine to create and show a window that contains a panel of type
	 * Lab4. The program ends when the user closes the window.
	 */
	public static void main(String[] args) {
		JFrame window = new JFrame("Some Objects in 3D");
		Lab4 panel = new Lab4();
		window.setContentPane(panel);
		window.pack();
		window.setResizable(false);
		window.setLocation(50, 50);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
	}

	/**
	 * Constructor for class Lab4.
	 */
	public Lab4() {
		super(new GLCapabilities(null)); // Makes a panel with default OpenGL "capabilities".
		setPreferredSize(new Dimension(700, 700));
		addGLEventListener(this); // This panel will respond to OpenGL events.
		addKeyListener(this); // The panel will respond to key events.
	}

	// -------------------Data for stellated dodecahedron ------------------

	private final static double[][] dodecVertices =
		{ 
			{ -0.650000, 0.000000, -0.248278 },
			{ 0.401722, 0.401722, 0.401722 }, // This array contains the coordinates
			{ 0.650000, 0.000000, 0.248278 }, // for the vertices of the polyhedron
			{ 0.401722, -0.401722, 0.401722 }, // known as a stellated dodecahedron
			{ 0.000000, -0.248278, 0.650000 },
			{ 0.000000, 0.248278, 0.650000 }, // Each row of the 2D array contains
			{ 0.650000, 0.000000, -0.248278 }, // the xyz-coordinates for one of
			{ 0.401722, 0.401722, -0.401722 }, // the vertices.
			{ 0.248278, 0.650000, 0.000000 },
			{ -0.248278, 0.650000, 0.000000 },
			{ -0.401722, 0.401722, -0.401722 },
			{ 0.000000, 0.248278, -0.650000 },
			{ 0.401722, -0.401722, -0.401722 },
			{ 0.248278, -0.650000, 0.000000 },
			{ -0.248278, -0.650000, 0.000000 },
			{ -0.650000, 0.000000, 0.248278 },
			{ -0.401722, 0.401722, 0.401722 },
			{ -0.401722, -0.401722, -0.401722 },
			{ 0.000000, -0.248278, -0.650000 },
			{ -0.401722, -0.401722, 0.401722 },
			{ 0.000000, 1.051722, 0.650000 },
			{ -0.000000, 1.051722, -0.650000 },
			{ 1.051722, 0.650000, -0.000000 },
			{ 1.051722, -0.650000, -0.000000 },
			{ -0.000000, -1.051722, -0.650000 },
			{ -0.000000, -1.051722, 0.650000 },
			{ 0.650000, 0.000000, 1.051722 },
			{ -0.650000, 0.000000, 1.051722 },
			{ 0.650000, -0.000000, -1.051722 },
			{ -0.650000, 0.000000, -1.051722 }, 
			{ -1.051722, 0.650000, -0.000000 },
			{ -1.051722, -0.650000, 0.000000 }
			};

	private static int[][] dodecTriangles =
		{
			{ 16, 9, 20 },
			{ 9, 8, 20 },
			{ 8, 1, 20 }, // This array specifies the faces of
			{ 1, 5, 20 }, // the stellated dodecahedron.
			{ 5, 16, 20 },
			{ 9, 10, 21 }, // Each row in the 2D array is a list
			{ 10, 11, 21 }, // of three integers. The integers
			{ 11, 7, 21 }, // are indices into the vertex array,
			{ 7, 8, 21 }, // dodecVertices. The vertices at
			{ 8, 9, 21 }, // at those indices are the vertices
			{ 8, 7, 22 }, // of one of the triangular faces of
			{ 7, 6, 22 }, // the polyhedron.
			{ 6, 2, 22 },
			{ 2, 1, 22 }, // For example, the first row, {16,9,20},
			{ 1, 8, 22 }, // means that vertices number 16, 9, and
			{ 6, 12, 23 }, // 20 are the vertices of a face.
			{ 12, 13, 23 },
			{ 13, 3, 23 }, // There are 60 faces.
			{ 3, 2, 23 },
			{ 2, 6, 23 },
			{ 18, 17, 24 },
			{ 17, 14, 24 },
			{ 14, 13, 24 },
			{ 13, 12, 24 },
			{ 12, 18, 24 },
			{ 14, 19, 25 },
			{ 19, 4, 25 },
			{ 4, 3, 25 },
			{ 3, 13, 25 },
			{ 13, 14, 25 },
			{ 4, 5, 26 },
			{ 5, 1, 26 },
			{ 1, 2, 26 },
			{ 2, 3, 26 },
			{ 3, 4, 26 },
			{ 15, 16, 27 },
			{ 16, 5, 27 },
			{ 5, 4, 27 },
			{ 4, 19, 27 },
			{ 19, 15, 27 },
			{ 7, 11, 28 },
			{ 11, 18, 28 },
			{ 18, 12, 28 },
			{ 12, 6, 28 },
			{ 6, 7, 28 },
			{ 10, 0, 29 },
			{ 0, 17, 29 },
			{ 17, 18, 29 },
			{ 18, 11, 29 },
			{ 11, 10, 29 },
			{ 0, 10, 30 },
			{ 10, 9, 30 },
			{ 9, 16, 30 },
			{ 16, 15, 30 },
			{ 15, 0, 30 },
			{ 17, 0, 31 },
			{ 0, 15, 31 },
			{ 15, 19, 31 },
			{ 19, 14, 31 },
			{ 14, 17, 31 }
			};

	// ------------------- TODO: Complete this section! ---------------------

	private int objectNumber = 0; // Which object to draw (1 ,2, 3, 4, 5, or 6)?
									// (Controlled by number keys.)

	private boolean useAnaglyph = false; // Should anaglyph stereo be used?
											// (Controlled by space bar.)

	private int rotateX = 0; // Rotations of the cube about the axes.
	private int rotateY = 0; // (Controlled by arrow, PageUp, PageDown keys;
	private int rotateZ = 0; // Home key sets all rotations to 0.)

	private GLUT glut = new GLUT(); // An object for drawing GLUT shapes.

	/**
	 * The method that draws the current object, with its modeling transformation.
	 */
	private void draw(GL2 gl2) {

		gl2.glRotatef(rotateZ, 0, 0, 1); // Apply rotations to complete object.
		gl2.glRotatef(rotateY, 0, 1, 0);
		gl2.glRotatef(rotateX, 1, 0, 0);

		// TODO: Draw the currently selected object, number 1, 2, 3, 4, 5, or 6.
		// (Objects should lie in the cube with x, y, and z coordinates in the
		// range -5 to 5.)
		// draw outline of 2 trapezoids together: a sinple 2D shape
		if (objectNumber == 1) {
			gl2.glBegin(gl2.GL_TRIANGLE_STRIP);
			// Vertices called from right corner to left corner
			gl2.glColor3f(1, 0, 1);
			gl2.glVertex2f(5, 5);
			gl2.glColor3f(0, 1, 1);
			gl2.glVertex2f(-5, 5);
			gl2.glColor3f(1, 1, 0);
			gl2.glVertex2f(3, 0);
			gl2.glColor3f(1, 0, 0);
			gl2.glVertex2f(-3, 0);
			gl2.glColor3f(0, 1, 0);
			gl2.glVertex2f(5, -5);
			gl2.glColor3f(0, 0, 1);
			gl2.glVertex2f(-5, -5);
			gl2.glEnd();
		}
		// wireframe polyhedron: stellated dodecahedron; has 60 faces
		else if (objectNumber == 2) {
			gl2.glDisable(gl2.GL_LIGHTING);
			gl2.glScaled(4, 4, 4);
			gl2.glColor3f(0, 0, 1);
			for(int face = 0; face < dodecTriangles.length; face++) {
				// face: list of faces (60 faces) / index of dodecTriangles.
			gl2.glBegin(gl2.GL_LINE_LOOP); // primative type to just draw the lines/boarder of teh faces
			for(int v = 0; v < dodecTriangles[face].length; v ++) {
				// dodecTriangles is 2d integer array, an array of faces, each with their own array
				// indicating what three vertexes from the dodecVertices array are the verticies for that face
			int vertexNum = dodecTriangles[face][v]; // vertex number for vertex v of face face
			double [] vertexCoords = dodecVertices[vertexNum]; // vertex coordinates (x, y, z) of vertex
			// each index of dodecVertices is a vertex. Each vertex has its own double array, containing the coordinates of that vertex
			gl2.glVertex3dv(vertexCoords, 0);
			}
			gl2.glEnd();
			}
			gl2.glEnable(gl2.GL_LIGHTING);
		}
		
		// Use GLUT and Transforms
		// a solid 3D object consisting of a green cone sitting on top of a brown cylinder (a tree)
		// center of objects bases begin at 0,0,0
		// want center of tree at 0,0,0 (move location of built) change z coordinate
		else if(objectNumber == 3){
			// glut.glutSolidCylinder (radius, height, slices, stacks)
			// glut.glutSolidCone(radius, height, slices, stacks)
			gl2.glPushMatrix();
			gl2.glTranslatef(0, 0, -6);
			gl2.glColor3f(0.75f, 0.45f, 0.2f);
			glut.glutSolidCylinder(1.5, 5, 32, 8); // brown cylinder = base/trunk of tree
			gl2.glTranslatef(0, 0, 4);
			gl2.glColor3f(0, 0.7f, 0);
			glut.glutSolidCone(3.5, 8, 32, 8); // green cone
			gl2.glPopMatrix();
		}
		
		// Object numbers 4, 5, 6 are stages in building a "cage" out of cylinders and spheres
		// object 6 has 8 spheres at verticies of a cube
		// centers of points: (-4,-4,-4), (-4,-4,4), (-4,4,-4), (-4,4,4), (4,-4,-4), (4,-4,4), (4,4,-4), (4,4,4)
		// a narrow cylinder lying along each edge of cube
		// diff colors for cylinders and spheres
		// sphere: glut.glutSolidSphere(radius, 32, 32)

		// a "bar" consisting of a cylinder with a translated sphere at each end
		// cylinder lies along the x-axis
		// center of spheres are ar (-4, 0, 0) and (4, 0, 0)
		// should write a subroutine to draw bar
		// need to pass gl2 as parameter
		else if(objectNumber == 4){
			bar(gl2);
		}
		
		// a square shape that can be make out of 2 translated copies of
		//the bar plus 2 additional cylinders
		// square lies in xy - plane
		// centers of the 4 spheres: (-4,4,0), (4,4,0), (-4,-4,0), (4,-4,0)
		// square != 4 barks
		// write a subroutine
		else if(objectNumber == 5){
			square(gl2);
		}
		// full cage: mafe from 2 translated copies of square plus 4 more cylinders
		//centered ar (0,0,0)
		else if(objectNumber == 6){
			cage(gl2);
		}
	}
	/**
	 * Draw a bar consisting of a cylinder with a translated sphere at each end.
	 * The cylinder lies along the x-axis.
	 * @param g
	 */
	public void bar(GL2 g){
		g.glPushMatrix();
		g.glTranslatef(-4f, 0, 0); // cylinder axis along the x-axis
		g.glRotatef(90, 0, 1, 0);
		g.glColor3f(1, 0, 1);
		glut.glutSolidCylinder(0.25, 8, 32, 8);
		g.glPopMatrix();
		g.glPushMatrix();
		g.glTranslatef(-4, 0, 0);
		g.glColor3f(1, 1, 0);
		glut.glutSolidSphere(1, 32, 32);
		g.glTranslatef(8, 0, 0);
		glut.glutSolidSphere(1, 32, 32);
		g.glPopMatrix();
	}
	/**
	 * Draw a 4 bars to form the shaoe of a square with a sphere at each corner, connecting the bars; using the barmethod twice and creating 2 more cylinders
	 * @param g
	 */
	public void square(GL2 g){
		g.glPushMatrix();
		g.glTranslatef(-4, -4, 0);
		g.glRotatef(90, -1, 0, 0);
		g.glColor3f(1, 0, 1);
		glut.glutSolidCylinder(0.25, 8, 32, 8);
		g.glTranslatef(8, 0, 0);
		glut.glutSolidCylinder(0.25, 8, 32, 8);
		g.glPopMatrix();
		g.glPushMatrix();
		g.glTranslatef(0, -4, 0);
		bar(g);
		g.glTranslatef(0, 8, 0);
		bar(g);
		g.glPopMatrix();
	}
	/**
	 * Draw a cage (a wire cube) using the square method twice and creating 4 more cylinders
	 * @param g
	 */
	public void cage(GL2 g){
		g.glPushMatrix();
		g.glColor3f(1, 0, 1);
		g.glTranslatef(-4, -4, -4);
		glut.glutSolidCylinder(0.25, 8, 32, 8);
		g.glTranslatef(0, 8, 0);
		glut.glutSolidCylinder(0.25, 8, 32, 8);
		g.glTranslatef(8, 0, 0);
		glut.glutSolidCylinder(0.25, 8, 32, 8);
		g.glTranslatef(0, -8, 0);
		glut.glutSolidCylinder(0.25, 8, 32, 8);
		g.glPopMatrix();
		g.glPushMatrix();
		g.glTranslated(0, 0, -4);
		square(g);
		g.glTranslated(0, 0, 8);
		square(g);
		g.glPopMatrix();
	}
	
	// -------------------- Draw the Scene -------------------------

	/**
	 * The display method is called when the panel needs to be drawn. It's called
	 * when the window opens and it is called by the keyPressed method when the user
	 * hits a key that modifies the scene.
	 */
	public void display(GLAutoDrawable drawable) {

		GL2 gl2 = drawable.getGL().getGL2(); // The object that contains all the OpenGL methods.

		if (useAnaglyph) {
			gl2.glDisable(GL2.GL_COLOR_MATERIAL); // in anaglyph mode, everything is drawn in white
			gl2.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, new float[] { 1, 1, 1, 1 }, 0);
		} else {
			gl2.glEnable(GL2.GL_COLOR_MATERIAL); // in non-anaglyph mode, glColor* is respected
		}
		gl2.glNormal3f(0, 0, 1); // (Make sure normal vector is correct for object 1.)

		gl2.glClearColor(0, 0, 0, 1); // Background color (black).
		gl2.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

		if (useAnaglyph == false) {
			gl2.glLoadIdentity(); // Make sure we start with no transformation!
			gl2.glTranslated(0, 0, -15); // Move object away from viewer (at (0,0,0)).
			draw(gl2);
		} else {
			gl2.glLoadIdentity(); // Make sure we start with no transformation!
			gl2.glColorMask(true, false, false, true);
			gl2.glRotatef(4, 0, 1, 0);
			gl2.glTranslated(1, 0, -15);
			draw(gl2); // draw the current object!
			gl2.glColorMask(true, false, false, true);
			gl2.glClear(GL2.GL_DEPTH_BUFFER_BIT);
			gl2.glLoadIdentity();
			gl2.glRotatef(-4, 0, 1, 0);
			gl2.glTranslated(-1, 0, -15);
			gl2.glColorMask(false, true, true, true);
			draw(gl2);
			gl2.glColorMask(true, true, true, true);
		}

	} // end display()

	/**
	 * The init method is called once, before the window is opened, to initialize
	 * OpenGL. Here, it sets up a projection, turns on some lighting, and enables
	 * the depth test.
	 */
	public void init(GLAutoDrawable drawable) {
		GL2 gl2 = drawable.getGL().getGL2();
		gl2.glMatrixMode(GL2.GL_PROJECTION);
		gl2.glFrustum(-3.5, 3.5, -3.5, 3.5, 5, 25);
		gl2.glMatrixMode(GL2.GL_MODELVIEW);
		gl2.glEnable(GL2.GL_LIGHTING);
		gl2.glEnable(GL2.GL_LIGHT0);
		gl2.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, new float[] { 0.7f, 0.7f, 0.7f }, 0);
		gl2.glLightModeli(GL2.GL_LIGHT_MODEL_TWO_SIDE, 1);
		gl2.glEnable(GL2.GL_DEPTH_TEST);
		gl2.glLineWidth(3); // make wide lines for the stellated dodecahedron.
	}

	public void dispose(GLAutoDrawable drawable) {
		// called when the panel is being disposed
	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		// called when user resizes the window
	}

	// ---------------- Methods from the KeyListener interface --------------

	/**
	 * Responds to keypressed events. The four arrow keys control the rotations
	 * about the x- and y-axes. The PageUp and PageDown keys control the rotation
	 * about the z-axis. The Home key resets all rotations to zero. The number keys
	 * 1, 2, 3, 4, 5, and 6 select the current object number. Pressing the space bar
	 * toggles anaglyph stereo on and off. The panel is redrawn to reflect the
	 * change.
	 */
	public void keyPressed(KeyEvent evt) {
		int key = evt.getKeyCode();
		boolean repaint = true;
		if (key == KeyEvent.VK_LEFT)
			rotateY -= 6;
		else if (key == KeyEvent.VK_RIGHT)
			rotateY += 6;
		else if (key == KeyEvent.VK_DOWN)
			rotateX += 6;
		else if (key == KeyEvent.VK_UP)
			rotateX -= 6;
		else if (key == KeyEvent.VK_PAGE_UP)
			rotateZ += 6;
		else if (key == KeyEvent.VK_PAGE_DOWN)
			rotateZ -= 6;
		else if (key == KeyEvent.VK_HOME)
			rotateX = rotateY = rotateZ = 0;
		else if (key == KeyEvent.VK_1)
			objectNumber = 1;
		else if (key == KeyEvent.VK_2)
			objectNumber = 2;
		else if (key == KeyEvent.VK_3)
			objectNumber = 3;
		else if (key == KeyEvent.VK_4)
			objectNumber = 4;
		else if (key == KeyEvent.VK_5)
			objectNumber = 5;
		else if (key == KeyEvent.VK_6)
			objectNumber = 6;
		else if (key == KeyEvent.VK_SPACE)
			useAnaglyph = !useAnaglyph;
		else
			repaint = false;
		if (repaint)
			repaint();
	}

	public void keyReleased(KeyEvent evt) {
	}

	public void keyTyped(KeyEvent evt) {
	}

} // end class Lab4

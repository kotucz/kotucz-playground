package hypergame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import javax.media.opengl.GL2;
import org.jbox2d.collision.CircleShape;
import org.jbox2d.collision.PolygonShape;
import org.jbox2d.collision.Shape;
import org.jbox2d.collision.ShapeType;
import org.jbox2d.common.Color3f;
import org.jbox2d.common.Settings;
import org.jbox2d.common.Vec2;
import org.jbox2d.common.XForm;
import org.jbox2d.dynamics.Body;

/**
 *
 * @author Kotuc
 */
public class Entity {

    Game game;
    Body body;
    Color color = Color.gray;

    public Entity() {
    }


    public Entity(Body body) {
        this.body = body;
    }

    void update(float timestep) {
        
    }

    void draw(GL2 gl) {

        XForm xf = body.getXForm();
        for (Shape s = body.getShapeList(); s != null; s = s.getNext()) {
            if (s.isSensor()) {
                continue;
            }
//            Color3f color
            if (body.isStatic()) {
                drawShape(gl, s, xf, new Color3f(0.5f, 0.9f, 0.5f));
            } else if (body.isSleeping()) {
                drawShape(gl, s, xf, new Color3f(0.5f, 0.5f, 0.9f));
            } else {
                drawShape(gl, s, xf, new Color3f(0.9f, 0.9f, 0.9f));
            }
            

        }

    }

    /** For internal use */
    public void drawShape(GL2 gl, Shape shape, XForm xf, Color3f color) {
//        Color3f coreColor = new Color3f(255f * 0.9f, 255f * 0.6f, 255f * 0.6f);

        if (shape.getType() == ShapeType.CIRCLE_SHAPE) {
            CircleShape circle = (CircleShape) shape;

            Vec2 center = XForm.mul(xf, circle.getLocalPosition());
            float radius = circle.getRadius();
            Vec2 axis = xf.R.col1;

            drawCircle(gl, center, radius, color);

//    			drawSolidCircle(center, radius, axis, color);
//
//    			if (core) {
//    				m_debugDraw.drawCircle(center, radius - Settings.toiSlop, coreColor);
//    			}

        } else if (shape.getType() == ShapeType.POLYGON_SHAPE) {
            PolygonShape poly = (PolygonShape) shape;
            int vertexCount = poly.getVertexCount();
            Vec2[] localVertices = poly.getVertices();

            assert (vertexCount <= Settings.maxPolygonVertices);
            Vec2[] vertices = new Vec2[vertexCount];

            for (int i = 0; i < vertexCount; ++i) {
                vertices[i] = XForm.mul(xf, localVertices[i]);
            }


//            TextureCoords coords = texture.getImageTexCoords();
            gl.glColor3f(color.x, color.y, color.z);

//            gl.glBegin(GL2.GL_TRIANGLES_ADJACENCY_ARB);
//            gl.glColor3f(0, 0, 1);
            gl.glBegin(GL2.GL_QUADS);
//            gl.glTexCoord2f(coords.left(), coords.bottom());
//            gl.glColor3f(0, 0, 1);
            gl.glVertex3f(vertices[0].x, vertices[0].y, 0);
//            gl.glTexCoord2f(coords.right(), coords.bottom());
//            gl.glColor3f(1, 0, 0);
            gl.glVertex3f(vertices[1].x, vertices[1].y, 0);
//            gl.glTexCoord2f(coords.right(), coords.top());            
//            gl.glColor3f(0, 1, 0);
            gl.glVertex3f(vertices[2].x, vertices[2].y, 0);
//            gl.glTexCoord2f(coords.left(), coords.top());
//            gl.glColor3f(1, 1, 0);
            gl.glVertex3f(vertices[3].x, vertices[3].y, 0);
            gl.glEnd();

//            m_debugDraw.drawSolidPolygon(vertices, vertexCount, color);

//    			if (core) {
//    				Vec2[] localCoreVertices = poly.getCoreVertices();
//    				for (int i = 0; i < vertexCount; ++i) {
//    					vertices[i] = XForm.mul(xf, localCoreVertices[i]);
//    				}
//    				m_debugDraw.drawPolygon(vertices, vertexCount, coreColor);
//    			}

        }
    }
    // from ProcessingDebugDraw
    public void drawCircle(GL2 gl, Vec2 center, float radius, Color3f color) {
        float k_segments = 16.0f;
        float k_increment = 2.0f * (float) Math.PI / k_segments;
        float theta = 0.0f;
        gl.glColor3f(color.x, color.y, color.z);
        gl.glBegin(GL2.GL_POLYGON);        
        for (int i = 0; i <= k_segments; ++i) {
            float vx = center.x + radius * (float) Math.cos(theta);
            float vy = center.y + radius * (float) Math.sin(theta);
            gl.glVertex3f(vx, vy, 0);
            theta += k_increment;
        }
        gl.glEnd();
    }

    void paint(Graphics g) {
        XForm xf = body.getXForm();
        for (Shape s = body.getShapeList(); s != null; s = s.getNext()) {
            paintShape(g, s, xf);    
        }        
    }

    final int scale = 200;
    final int xoff = 350;
    final int yoff = 450;

//    final int scale = 1;
//    final int xoff = 0;
//    final int yoff = 0;

    Point toPoint(Vec2 vec) {
        return new Point((int)(vec.x*scale)+xoff, (int)(-vec.y*scale)+yoff);
    }
    
    private void paintShape(Graphics g, Shape shape, XForm xf) {
        Graphics2D g2 = (Graphics2D)g;
        g.setColor(color);
        if (shape.getType() == ShapeType.CIRCLE_SHAPE) {
            CircleShape circle = (CircleShape) shape;
            
            Vec2 cent = XForm.mul(xf, circle.getLocalPosition());

            Point a = toPoint(cent.add(xf.R.col1.mul(circle.getRadius())));

            int r = (int)(circle.getRadius()*scale);

            Point c = toPoint(cent);

            g.fillOval(c.x-r, c.y-r, 2*r, 2*r);
            g.setColor(Color.black);
            g.drawOval(c.x-r, c.y-r, 2*r, 2*r);
            g.drawLine(c.x, c.y, a.x, a.y);
//    			drawSolidCircle(center, radius, axis, color);
//
//    			if (core) {
//    				m_debugDraw.drawCircle(center, radius - Settings.toiSlop, coreColor);
//    			}

        } else if (shape.getType() == ShapeType.POLYGON_SHAPE) {
            PolygonShape poly = (PolygonShape) shape;
            int vertexCount = poly.getVertexCount();
            Vec2[] localVertices = poly.getVertices();

            assert (vertexCount <= Settings.maxPolygonVertices);
            Point[] points = new Point[vertexCount];
            int[] vertx = new int[vertexCount];
            int[] verty = new int[vertexCount];

            for (int i = 0; i < vertexCount; ++i) {
                points[i] = toPoint(XForm.mul(xf, localVertices[i]));
                vertx[i] = points[i].x;
                verty[i] = points[i].y;
            }
            
            g.fillPolygon(vertx, verty, vertexCount);
            g.setColor(Color.black);
            g.drawPolygon(vertx, verty, vertexCount);


        }
    }
}

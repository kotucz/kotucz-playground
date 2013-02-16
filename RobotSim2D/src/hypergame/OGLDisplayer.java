package hypergame;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.ShapeType;
import org.jbox2d.common.Color3f;
import org.jbox2d.common.Settings;
import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;

import javax.media.opengl.GL2;

/**
* @author Kotuc
*/
public class OGLDisplayer /* TODO implements Displayer */ {


    public void drawBody(GL2 gl, Body body) {

        Transform xf = body.getTransform();
        for (Fixture s = body.getFixtureList(); s != null; s = s.getNext()) {
            if (s.isSensor()) {
                continue;
            }
//            Color3f color
            // TODO colors
//            if (body.isStatic()) {
//                drawShape(gl, s, xf, new Color3f(0.5f, 0.9f, 0.5f));
//            } else if (body.isSleeping()) {
//                drawShape(gl, s, xf, new Color3f(0.5f, 0.5f, 0.9f));
//            } else {
            drawShape(gl, s, xf, new Color3f(0.9f, 0.9f, 0.9f));
//            }


        }

    }

    /** For internal use */
    public void drawShape(GL2 gl, Fixture shape, Transform xf, Color3f color) {
//        Color3f coreColor = new Color3f(255f * 0.9f, 255f * 0.6f, 255f * 0.6f);

        if (shape.getType() == ShapeType.CIRCLE) {
            CircleShape circle = (CircleShape) shape.getShape();

//            / TODO Vec2 center = Transform.mul(xf, shape.getLocalPosition());
            Vec2 center = Transform.mul(xf, shape.getBody().getLocalCenter());
            float radius = circle.m_radius;
            Vec2 axis = xf.R.col1;

            drawCircle(gl, center, radius, color);

//    			drawSolidCircle(center, radius, axis, color);
//
//    			if (core) {
//    				m_debugDraw.drawCircle(center, radius - Settings.toiSlop, coreColor);
//    			}

        } else if (shape.getType() == ShapeType.POLYGON) {
            PolygonShape poly = (PolygonShape) shape.getShape();
            int vertexCount = poly.getVertexCount();
            Vec2[] localVertices = poly.getVertices();

            assert (vertexCount <= Settings.maxPolygonVertices);
            Vec2[] vertices = new Vec2[vertexCount];

            for (int i = 0; i < vertexCount; ++i) {
                vertices[i] = Transform.mul(xf, localVertices[i]);
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


}

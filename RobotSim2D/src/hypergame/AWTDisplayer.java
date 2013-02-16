package hypergame;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.ShapeType;
import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;

/**
 * @author Kotuc
 */
public class AWTDisplayer extends Displayer {

    final Graphics2D g2;

    public AWTDisplayer(Graphics2D g2) {
        this.g2 = g2;
    }

    @Override
    public void paintBody(Body body) {
        Transform xf = body.getTransform();
        for (Fixture s = body.getFixtureList(); s != null; s = s.getNext()) {
            paintShape(s, xf);
        }
    }

    private void paintShape(Fixture shape, Transform xf) {
        if (shape.getType() == ShapeType.CIRCLE) {
            CircleShape circles = (CircleShape) shape.getShape();

//          TODO  Vec2 cent = Transform.mul(xf, circles.getLocalPosition());
            Vec2 cent = Transform.mul(xf, shape.getBody().getLocalCenter().add(circles.m_p));

            {
//                Point a = toPoint(cent.add(xf.R.col1.mul(circles.getRadius())));
//
//                int r = (int) (circles.getRadius() * scale);
//
//                Point c = toPoint(cent);
//
//                g.fillOval(c.x - r, c.y - r, 2 * r, 2 * r);
//                g.setColor(Color.black);
//                g.drawOval(c.x - r, c.y - r, 2 * r, 2 * r);
//                g.drawLine(c.x, c.y, a.x, a.y);
            }
            {
                double r = circles.m_radius;
                Ellipse2D.Double circle = new Ellipse2D.Double(cent.x - r, cent.y - r, 2 * r, 2 * r);

                g2.fill(circle);
                Color color = g2.getColor();
                setColor(Color.black);
                g2.draw(circle);
                setColor(color);
            }

//    			drawSolidCircle(center, radius, axis, color);
//
//    			if (core) {
//    				m_debugDraw.drawCircle(center, radius - Settings.toiSlop, coreColor);
//    			}

        } else if (shape.getType() == ShapeType.POLYGON) {
            PolygonShape poly = (PolygonShape) shape.getShape();
            int vertexCount = poly.getVertexCount();
            Vec2[] localVertices = poly.getVertices();
            {
//                assert (vertexCount <= Settings.maxPolygonVertices);
//                Point[] points = new Point[vertexCount];
//                int[] vertx = new int[vertexCount];
//                int[] verty = new int[vertexCount];
//
//                for (int i = 0; i < vertexCount; ++i) {
//                    points[i] = toPoint(XForm.mul(xf, localVertices[i]));
//                    vertx[i] = points[i].x;
//                    verty[i] = points[i].y;
//                }
//
//                g.fillPolygon(vertx, verty, vertexCount);
//                g.setColor(Color.black);
//                g.drawPolygon(vertx, verty, vertexCount);
            }

            {

                GeneralPath polygon = new GeneralPath();

                Vec2 mul0 = Transform.mul(xf, localVertices[0]);
                polygon.moveTo(mul0.x, mul0.y);

                for (int i = 1; i < vertexCount; ++i) {
                    Vec2 mul = Transform.mul(xf, localVertices[i]);
                    polygon.lineTo(mul.x, mul.y);
                }

                polygon.closePath();

                g2.fill(polygon);
                Color color = g2.getColor();
                g2.setColor(Color.black);
                g2.draw(polygon);
                g2.setColor(color);
            }

        }
    }


    @Override
    public void setColor(Color green) {
        g2.setColor(green);
    }

    @Override
    public void drawLine(Vec2 start, Vec2 end) {
        g2.draw(new Line2D.Float(start.x, start.y, end.x, end.y));
    }

    @Override
    public void drawVector(Vec2 pos, Vec2 vec) {
        drawLine(pos, pos.add(vec));
    }

}

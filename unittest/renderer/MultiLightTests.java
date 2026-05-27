package renderer;

import java.util.List;

import geometries.api.Geometry;
import geometries.impl.Sphere;
import geometries.impl.Triangle;
import lighting.BeamLight;
import lighting.DirectionalLight;
import lighting.LightSource;
import lighting.PointLight;
import lighting.SpotLight;
import org.junit.jupiter.api.Test;
import primitives.Color;
import primitives.Material;
import primitives.Point;
import primitives.Vector;
import scene.Scene;

import static java.awt.Color.BLACK;

/**
 * Test rendering a simple image with one geometry and a couple light sources
 * @author Dvir Farkash
 */
public class MultiLightTests {
    /**
     * Constant for tests resolution
     */
    private static final int RESOLUTION = 1500;
    
    /**
     * First scene for sphere
     */
    private final Scene _scene1 =
            new Scene("Sphere multi light scene");
    /**
     * Second scene for triangle
     */
    private final Scene _scene2 = new Scene("Triangle multi light scene");
    
    /**
     * First camera builder for sphere
     */
    private final Camera.Builder _camera1 = Camera.getBuilder()
            .setRayTracer(_scene1, RayTracerType.SIMPLE)
            .setLocation(new Point(0, 0, 1000))
            .setDirection(Point.ZERO, Vector.AXIS_Y)
            .setVpSize(150, 150).setVpDistance(1000);
    
    /**
     * Second camera builder for triangle
     */
    private final Camera.Builder _camera2 = Camera.getBuilder()
            .setRayTracer(_scene2, RayTracerType.SIMPLE)
            .setLocation(new Point(0, 0, 1000))
            .setDirection(Point.ZERO, Vector.AXIS_Y)
            .setVpSize(200, 200).setVpDistance(1000);
    
    /**
     * Shininess value for most of the geometries in the tests
     */
    private static final int SHININESS = 250;
    /**
     * Diffusion attenuation factor
     */
    private static final double KD = 0.5;
    /**
     * Specular attenuation factor
     */
    private static final double KS = 0.5;
    
    /**
     * Material for some of the geometries in the tests
     */
    private static final Material MATERIAL = new Material().setKD(KD).setKS(KS)
            .setShininess(SHININESS);
    /**
     * Color of the sphere
     */
    private static final Color SPHERE_COLOR = new Color(BLACK);
    
    /**
     * Center of the sphere
     */
    private static final Point SPHERE_CENTER = new Point(0, 0, -50);
    /**
     * Radius of the sphere
     */
    private static final double SPHERE_RADIUS = 50D;
    
    /**
     * The triangles' vertices for the tests with triangles
     */
    private static final Point[] VERTICES =
            {
                    // the shared left-bottom:
                    new Point(-110, -110, -150),
                    // the shared right-top:
                    new Point(95, 100, -150),
                    // the right-bottom
                    new Point(110, -110, -150),
                    // the left-top
                    new Point(-75, 78, 100)
            };
    /**
     * Position of the lights in the test with sphere
     */
    private static final Point SPHERE_POINT_LIGHT_POSITION = new Point(-10, -35, 15);
    private static final Point SPHERE_SPOT_LIGHT_POSITION = new Point(10, 40, 5);
    private static final Point SPHERE_BEAM_LIGHT_POSITION = new Point(30, 20, 10);
    
    /**
     * Direction of the lights in the test with sphere
     */
    private static final Vector SPHERE_DIRECTIONAL_LIGHT_DIRECTION = new Vector(1, 0, 0);
    private static final Vector SPHERE_SPOT_LIGHT_DIRECTION = new Vector(-40, 40, -35);
    private static final Vector SPHERE_BEAM_LIGHT_DIRECTION = new Vector(-3, -2, -6);
    
    /**
     * Colors of the lights in the test with sphere
     */
    private static final Color SPHERE_DIRECTIONAL_LIGHT_COLOR = new Color(219, 80, 4).scale(2);
    private static final Color SPHERE_POINT_LIGHT_COLOR = new Color(28, 42, 103).scale(5);
    private static final Color SPHERE_SPOT_LIGHT_COLOR = new Color(245, 217, 46).scale(4);
    private static final Color SPHERE_BEAM_LIGHT_COLOR = new Color(33, 237, 142).scale(2);
    
    /**
     * Light's attenuation factors for sphere
     */
    private static final double SPHERE_KL = 0.0001;
    private static final double SPHERE_KQ = 0.0004;
    
    /**
     * The lights in the test with sphere
     */
    private static final LightSource SPHERE_DIRECTIONAL_LIGHT = new DirectionalLight(
            SPHERE_DIRECTIONAL_LIGHT_COLOR,
            SPHERE_DIRECTIONAL_LIGHT_DIRECTION
    );
    private static final LightSource SPHERE_POINT_LIGHT = new PointLight(
            SPHERE_POINT_LIGHT_COLOR,
            SPHERE_POINT_LIGHT_POSITION
    ).setKl(SPHERE_KL).setKq(SPHERE_KQ);
    private static final LightSource SPHERE_SPOT_LIGHT = new SpotLight(
            SPHERE_SPOT_LIGHT_COLOR,
            SPHERE_SPOT_LIGHT_POSITION,
            SPHERE_SPOT_LIGHT_DIRECTION
    ).setKl(SPHERE_KL).setKq(SPHERE_KQ);
    private static final LightSource SPHERE_BEAM_LIGHT = new BeamLight(
            SPHERE_BEAM_LIGHT_COLOR,
            SPHERE_BEAM_LIGHT_POSITION,
            SPHERE_BEAM_LIGHT_DIRECTION
    ).setKl(SPHERE_KL).setKq(SPHERE_KQ).setNarrowBeam(10);
    
    /**
     * Position of the lights in the test with triangle
     */
    private static final Point TRIANGLE_POINT_LIGHT_POSITION = new Point(80, 50, -80);
    private static final Point TRIANGLE_SPOT_LIGHT_POSITION = new Point(-50, -80, -80);
    private static final Point TRIANGLE_BEAM_LIGHT_POSITION = new Point(0, 0, -80);
    
    /**
     * Direction of the lights in the test with triangle
     */
    private static final Vector TRIANGLE_DIRECTIONAL_LIGHT_DIRECTION = new Vector(-1, 0, -1);
    private static final Vector TRIANGLE_SPOT_LIGHT_DIRECTION = new Vector(-10, -10, -10);
    private static final Vector TRIANGLE_BEAM_LIGHT_DIRECTION = new Vector(10, -10, -9);
    
    /**
     * Colors of the lights in the test with triangle
     */
    private static final Color TRIANGLE_DIRECTIONAL_LIGHT_COLOR = new Color(219, 80, 4);
    private static final Color TRIANGLE_POINT_LIGHT_COLOR = new Color(28, 42, 103).scale(5);
    private static final Color TRIANGLE_SPOT_LIGHT_COLOR = new Color(245, 217, 46).scale(3);
    private static final Color TRIANGLE_BEAM_LIGHT_COLOR = new Color(33, 237, 142).scale(4);
    
    /**
     * Light's attenuation factors for triangle
     */
    private static final double TRIANGLE_KL = 0.00001;
    private static final double TRIANGLE_KQ = 0.0001;
    
    /**
     * The lights in the test with triangle
     */
    private static final LightSource TRIANGLE_DIRECTIONAL_LIGHT = new DirectionalLight(
            TRIANGLE_DIRECTIONAL_LIGHT_COLOR,
            TRIANGLE_DIRECTIONAL_LIGHT_DIRECTION
    );
    private static final LightSource TRIANGLE_POINT_LIGHT = new PointLight(
            TRIANGLE_POINT_LIGHT_COLOR,
            TRIANGLE_POINT_LIGHT_POSITION
    ).setKl(TRIANGLE_KL).setKq(TRIANGLE_KQ);
    private static final LightSource TRIANGLE_SPOT_LIGHT = new SpotLight(
            TRIANGLE_SPOT_LIGHT_COLOR,
            TRIANGLE_SPOT_LIGHT_POSITION,
            TRIANGLE_SPOT_LIGHT_DIRECTION
    ).setKl(TRIANGLE_KL).setKq(TRIANGLE_KQ);
    private static final LightSource TRIANGLE_BEAM_LIGHT = new BeamLight(
            TRIANGLE_BEAM_LIGHT_COLOR,
            TRIANGLE_BEAM_LIGHT_POSITION,
            TRIANGLE_BEAM_LIGHT_DIRECTION
    ).setKl(TRIANGLE_KL).setKq(TRIANGLE_KQ).setNarrowBeam(10);
    
    /**
     * The sphere in appropriate tests
     */
    private static final Geometry SPHERE = new Sphere(SPHERE_CENTER, SPHERE_RADIUS)
            .setEmission(SPHERE_COLOR).setMaterial(MATERIAL);
    /**
     * The first triangle in appropriate tests
     */
    private static final Geometry TRIANGLE1 = new Triangle(VERTICES[0], VERTICES[1], VERTICES[2])
            .setMaterial(MATERIAL);
    /**
     * The first triangle in appropriate tests
     */
    private static final Geometry TRIANGLE2 = new Triangle(VERTICES[0], VERTICES[1], VERTICES[3])
            .setMaterial(MATERIAL);
    
    /**
     * Produce a picture with a sphere and different light sources with different colors
     */
    @Test
    void testSphereMultiLight() {
        _scene1.geometries.add(SPHERE);
        _scene1.lights.addAll(List.of(
                SPHERE_DIRECTIONAL_LIGHT,
                SPHERE_POINT_LIGHT,
                SPHERE_SPOT_LIGHT,
                SPHERE_BEAM_LIGHT
        ));
        
        _camera1.setResolution(RESOLUTION, RESOLUTION)
                .build()
                .renderImage()
                .writeToImage("lightSphereMultiLights");
    }
    
    /**
     * Produce a picture with a triangles and different light sources with different colors
     */
    @Test
    void testTrianglesMultiLight() {
        _scene2.geometries.add(TRIANGLE1, TRIANGLE2);
        _scene2.lights.addAll(List.of(
                TRIANGLE_DIRECTIONAL_LIGHT,
                TRIANGLE_POINT_LIGHT,
                TRIANGLE_SPOT_LIGHT,
                TRIANGLE_BEAM_LIGHT
        ));
        
        _camera2.setResolution(RESOLUTION, RESOLUTION)
                .build()
                .renderImage()
                .writeToImage("lightTriangleMultiLights");
    }
}

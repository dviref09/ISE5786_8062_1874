package renderer;

import java.io.FileWriter;
import java.io.IOException;

import geometries.impl.Plane;
import geometries.impl.Sphere;
import geometries.impl.Triangle;
import lighting.AmbientLight;
import lighting.BeamLight;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import primitives.Color;
import primitives.Double3;
import primitives.Material;
import primitives.Point;
import primitives.Vector;
import scene.Scene;

/**
 * Creates images for demonstrating minip-1 features
 * @author Dvir Farkash
 */
@TestMethodOrder(OrderAnnotation.class)
class Minip1Tests {
    /**
     * The scene object
     */
    private final Scene scene = setScene();
    
    /**
     * The resolution of the images
     */
    private static final int RESOLUTION = 1000;
    /**
     * The number of rays per side for ss
     */
    private static final int ssRays = 33;
    /**
     * Camera location parts
     */
    private static final double cameraY = 100;
    private static final double cameraZ = 1000;
    
    /**
     * Produces a custom 3D rendered scene without soft shadows
     */
    @Test
    @Order(1)
    void testWizardSceneNoSS() {
        // ----------------- Camera Setup (Builder Pattern) -----------------
        Camera camera = Camera.getBuilder()
                              .setLocation(new Point(0, cameraY, cameraZ))
                              .setDirection(new Point(0, 0, 0))
                              .setVpSize(220, 220)
                              .setVpDistance(Math.sqrt(cameraY * cameraY + cameraZ * cameraZ))
                              .setResolution(RESOLUTION, RESOLUTION)
                              .setRayTracer(scene, RayTracerType.SIMPLE)
                              .setMultithreading(-1)
                              .setDebugPrint(0.1)
                              .build();
        
        // ----------------- Execute Rendering and Image Compilation -----------------
        long startTime = System.currentTimeMillis();
        camera.renderImage().writeToImage("Minip1ImageWithoutSS");
        long endTime = System.currentTimeMillis();
        
        writeMeasurement(startTime, endTime, false, false);
    }
    
    /**
     * Produces a custom 3D rendered scene
     * illustrating soft shadow alongside all the previous features
     */
    @Test
    @Order(2)
    void testWizardSceneSS() {
        // ----------------- Camera Setup (Builder Pattern) -----------------
        Camera camera = Camera.getBuilder()
                              .setLocation(new Point(0, cameraY, cameraZ))
                              .setDirection(new Point(0, 0, 0))
                              .setVpSize(220, 220)
                              .setVpDistance(Math.sqrt(cameraY * cameraY + cameraZ * cameraZ))
                              .setResolution(RESOLUTION, RESOLUTION)
                              .setRayTracer(scene, RayTracerType.SIMPLE)
                              .setSSRays(ssRays)
                              .setMultithreading(-1)
                              .setDebugPrint(0.1)
                              .build();
        
        // ----------------- Execute Rendering and Image Compilation -----------------
        long startTime = System.currentTimeMillis();
        camera.renderImage().writeToImage("Minip1ImageWithSS");
        long endTime = System.currentTimeMillis();
        
        writeMeasurement(startTime, endTime, true, true);
    }
    
    /**
     * Helper method for writing the running time to text file
     * @param startTime The start time of the measurement in ms
     * @param endTime The end time of the measurement in ms
     * @param append Whether to append to the file or overwrite it
     * @param withSS Whether the measurement includes soft shadows
     */
    void writeMeasurement(long startTime, long endTime, boolean append, boolean withSS) {
        long timeMilliSeconds = endTime - startTime;
        long totalTimeSeconds = timeMilliSeconds / 1000;
        long hours = totalTimeSeconds / 3600;
        long minutes = totalTimeSeconds % 3600 / 60;
        long seconds = totalTimeSeconds % 60;
        long milliSeconds = timeMilliSeconds % 1000;
        try {
            FileWriter measurementWriter = new FileWriter("measurements/MP1_measurements.txt", append);
            measurementWriter.write("With" + (withSS ? " " : "out ") + "soft shadows, creation time: " +
                    (hours != 0 ? hours + ":" : "") + (minutes != 0 ? minutes + ":" : "") +
                    seconds + "." + milliSeconds + "\n");
            measurementWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Helper method for setting the scene
     */
    Scene setScene() {
        Scene scene = new Scene("Wizard's Laboratory Table");
        // ----------------- Scene Background and Ambient Light -----------------
        scene.setBackground(new Color(10, 15, 25));
        scene.setAmbientLight(new AmbientLight(new Color(13)));
        
        // ----------------- Materials Definition -----------------
        
        // 1. Purely reflective metal/mirror material (Reflection ONLY, no Transparency)
        Material mirrorMat = new Material().setKD(new Double3(0.1))
                                           .setKS(new Double3(0.9))
                                           .setShininess(100)
                                           .setKR(new Double3(0.85));
        
        // 2. Matte floor surface to perfectly display the projected shadows
        Material floorMat = new Material().setKD(new Double3(0.7)).setKS(new Double3(0.1)).setShininess(10);
        
        // 4. Another variation for the table base if needed
        Material tableMat = new Material().setKD(new Double3(0.5)).setKS(new Double3(0.2)).setShininess(15);
        
        // 5. The material for the mirror frame
        Material frameMat = new Material().setKD(new Double3(0.4))
                                          .setKS(new Double3(0.2))
                                          .setShininess(80)
                                          .setKT(new Double3(0.4));
        
        // Material for Pyramid 1: Semi-translucent glass
        Material glassMat1 = new Material().setKD(new Double3(0.15))
                                           .setKS(new Double3(0.85))
                                           .setShininess(90)
                                           .setKT(new Double3(0.65));
        
        // Material for Pyramid 2: Highly transparent glass
        Material glassMat2 = new Material().setKD(new Double3(0.05))
                                           .setKS(new Double3(0.95))
                                           .setShininess(120)
                                           .setKT(new Double3(0.95));
        
        // Material for Pyramid 3: Dense frosted-like glass
        Material glassMat3 = new Material().setKD(new Double3(0.25))
                                           .setKS(new Double3(0.75))
                                           .setShininess(70)
                                           .setKT(new Double3(0.45));
        
        // ----------------- Geometries Setup -----------------
        
        // 1. The Main Floor Plane
        scene.geometries.add(new Plane(new Point(0, -60, 0), new Vector(0, 1, 0)).setEmission(new Color(40, 40, 40))
                                                                                 .setMaterial(floorMat));
        
        // 4-5. Medium-Sized Mirror on the Back-Left
        Point pLMirrorTopLeft = new Point(-120, 50, -140);
        Point pLMirrorTopRight = new Point(30, 50, -170);
        Point pLMirrorBottomLeft = new Point(-120, -30, -150);
        Point pLMirrorBottomRight = new Point(30, -30, -180);
        
        scene.geometries.add(new Triangle(pLMirrorBottomLeft, pLMirrorTopRight, pLMirrorTopLeft).setEmission(new Color(10, 10, 10))
                                                                                                .setMaterial(mirrorMat));
        scene.geometries.add(new Triangle(pLMirrorBottomLeft, pLMirrorBottomRight, pLMirrorTopRight).setEmission(new Color(10, 10, 10))
                                                                                                    .setMaterial(mirrorMat));
        
        // --- COMPLETE FRAME FOR THE MIRROR (8 Triangles) ---
        Point fTopLeft = new Point(-126, 55, -139);
        Point fTopRight = new Point(36, 55, -169);
        Point fBottomLeft = new Point(-126, -35, -149);
        Point fBottomRight = new Point(36, -35, -179);
        
        // 1-2. TOP BORDER (2 Triangles forming a thin tilted rectangle)
        scene.geometries.add(new Triangle(fTopLeft, fTopRight, pLMirrorTopRight).setEmission(new Color(40, 60, 80))
                                                                                .setMaterial(frameMat));
        scene.geometries.add(new Triangle(fTopLeft, pLMirrorTopRight, pLMirrorTopLeft).setEmission(new Color(40, 60,
                                                                                              80))
                                                                                      .setMaterial(frameMat));
        
        // 3-4. BOTTOM BORDER (2 Triangles forming a thin tilted rectangle on the floor)
        scene.geometries.add(new Triangle(fBottomLeft, pLMirrorBottomRight, fBottomRight).setEmission(new Color(40,
                                                                                                 60, 80))
                                                                                         .setMaterial(frameMat));
        scene.geometries.add(new Triangle(fBottomLeft, pLMirrorBottomLeft, pLMirrorBottomRight).setEmission(new Color(40, 60, 80))
                                                                                               .setMaterial(frameMat));
        
        // 5-6. LEFT BORDER (2 Triangles forming a thin tilted rectangle)
        scene.geometries.add(new Triangle(fTopLeft, pLMirrorTopLeft, pLMirrorBottomLeft).setEmission(new Color(30, 50
                                                                                                , 70))
                                                                                        .setMaterial(frameMat));
        scene.geometries.add(new Triangle(fTopLeft, pLMirrorBottomLeft, fBottomLeft).setEmission(new Color(30, 50, 70))
                                                                                    .setMaterial(frameMat));
        
        // 7-8. RIGHT BORDER (2 Triangles forming a thin tilted rectangle closer to the center)
        scene.geometries.add(new Triangle(pLMirrorTopRight, fTopRight, fBottomRight).setEmission(new Color(30, 50, 70))
                                                                                    .setMaterial(frameMat));
        scene.geometries.add(new Triangle(pLMirrorTopRight, fBottomRight, pLMirrorBottomRight).setEmission(new Color(30, 50, 70))
                                                                                              .setMaterial(frameMat));
        
        // 6. Flanking Left Mirror Sphere (Reflection ONLY)
        scene.geometries.add(new Sphere(new Point(-65, -20, -50), 22d).setEmission(new Color(20, 20, 20))
                                                                      .setMaterial(mirrorMat));
        
        // 7. Flanking Right Mirror Sphere (Reflection ONLY)
        scene.geometries.add(new Sphere(new Point(65, -20, -50), 22d).setEmission(new Color(20, 20, 20))
                                                                     .setMaterial(mirrorMat));
        
        // --- CENTERPIECE: THE SIMPLE TABLE (Solid) ---
        
        // 8-9. Table Top Surface (2 Triangles forming a small rectangle at Y = -25)
        Point tTopLeft = new Point(-30, -25, -60);
        Point tTopRight = new Point(30, -25, -60);
        Point tBotLeft = new Point(-30, -25, -20);
        Point tBotRight = new Point(30, -25, -20);
        
        scene.geometries.add(new Triangle(tBotLeft, tTopRight, tTopLeft).setEmission(new Color(70, 45, 30))
                                                                        .setMaterial(tableMat));
        scene.geometries.add(new Triangle(tBotLeft, tBotRight, tTopRight).setEmission(new Color(70, 45, 30))
                                                                         .setMaterial(tableMat));
        
        // 10-12. Table Legs
        scene.geometries.add(new Triangle(new Point(-28, -25, -22), new Point(-25, -25, -22), new Point(-28, -60,
                -22)).setEmission(new Color(50, 30, 20))
                     .setMaterial(tableMat));
        scene.geometries.add(new Triangle(new Point(25, -25, -22), new Point(28, -25, -22), new Point(28, -60, -22)).setEmission(new Color(50, 30, 20))
                                                                                                                    .setMaterial(tableMat));
        scene.geometries.add(new Triangle(new Point(-28, -25, -58), new Point(-25, -25, -58), new Point(-28, -60,
                -58)).setEmission(new Color(40, 20, 10))
                     .setMaterial(tableMat));
        scene.geometries.add(new Triangle(new Point(25, -25, -58), new Point(28, -25, -58), new Point(28, -60,
                -58)).setEmission(new Color(40, 20, 10)).setMaterial(tableMat));
        
        // --- CENTERPIECE: GLASS PYRAMIDS SET (Diverse Materials & Non-Cyan Colors) ---
        
        // PYRAMID 1: Main Large Deep Burgundy Glass Pyramid (Center-Back on the table)
        Point pyr1Front = new Point(0, -25, -45);
        Point pyr1Left = new Point(-12, -25, -55);
        Point pyr1Right = new Point(12, -25, -55);
        Point pyr1Apex = new Point(0, 0, -50);
        
        scene.geometries.add(new Triangle(pyr1Front, pyr1Left, pyr1Apex).setEmission(new Color(130, 20, 40))
                                                                        .setMaterial(glassMat1));
        scene.geometries.add(new Triangle(pyr1Front, pyr1Right, pyr1Apex).setEmission(new Color(130, 20, 40))
                                                                         .setMaterial(glassMat1));
        scene.geometries.add(new Triangle(pyr1Left, pyr1Right, pyr1Apex).setEmission(new Color(110, 15, 30))
                                                                        .setMaterial(glassMat1));
        
        // PYRAMID 2: Medium Emerald Green Glass Pyramid (Front-Left on the table)
        Point pyr2Front = new Point(-14, -25, -30);
        Point pyr2Left = new Point(-22, -25, -38);
        Point pyr2Right = new Point(-8, -25, -38);
        Point pyr2Apex = new Point(-14, -10, -35);
        
        scene.geometries.add(new Triangle(pyr2Front, pyr2Left, pyr2Apex).setEmission(new Color(20, 120, 60))
                                                                        .setMaterial(glassMat2));
        scene.geometries.add(new Triangle(pyr2Front, pyr2Right, pyr2Apex).setEmission(new Color(20, 120, 60))
                                                                         .setMaterial(glassMat2));
        scene.geometries.add(new Triangle(pyr2Left, pyr2Right, pyr2Apex).setEmission(new Color(15, 100, 50))
                                                                        .setMaterial(glassMat2));
        
        // PYRAMID 3: Small Deep Purple Glass Pyramid (Front-Right on the table)
        Point pyr3Front = new Point(15, -25, -32);
        Point pyr3Left = new Point(10, -25, -38);
        Point pyr3Right = new Point(20, -25, -38);
        Point pyr3Apex = new Point(15, -15, -35);
        
        scene.geometries.add(new Triangle(pyr3Front, pyr3Left, pyr3Apex).setEmission(new Color(90, 30, 140))
                                                                        .setMaterial(glassMat3));
        scene.geometries.add(new Triangle(pyr3Front, pyr3Right, pyr3Apex).setEmission(new Color(90, 30, 140))
                                                                         .setMaterial(glassMat3));
        scene.geometries.add(new Triangle(pyr3Left, pyr3Right, pyr3Apex).setEmission(new Color(75, 25, 120))
                                                                        .setMaterial(glassMat3));
        
        // ----------------- Multi-Light Sources Setup -----------------
        // 1. Spotlight from top-left pointing to the center to cast clear shadows
        scene.lights.add(new BeamLight(new Color(252, 392, 816), new Point(-100, 50, 100),
                new Vector(35, -70, -150)).setKl(0.0001)
                                          .setKq(0.00001)
                                          .setHeight(40)
                                          .setWidth(40)
                                          .setNarrowBeam(10));
        
        // 2. Spotlight on the right side to add secondary softer cross-shadowing
        scene.lights.add(new BeamLight(new Color(1333, 1306, 490), new Point(100, 50, 50),
                new Vector(-35, -70, -100)).setKl(0.0002)
                                           .setKq(0.0002)
                                           .setHeight(40)
                                           .setWidth(40)
                                           .setNarrowBeam(10));
        
        // 3. Another spotlight above the mirror
        scene.lights.add(new BeamLight(new Color(235, 15, 255), new Point(-45, 50, -155),
                new Vector(45, -25, 115)).setKl(0.0001)
                                         .setKq(0.00001)
                                         .setHeight(15)
                                         .setWidth(15)
                                         .setNarrowBeam(5));
        return scene;
    }
}

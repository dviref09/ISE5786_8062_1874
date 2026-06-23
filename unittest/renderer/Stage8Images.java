package renderer;

import geometries.impl.Plane;
import geometries.impl.Sphere;
import geometries.impl.Triangle;
import lighting.AmbientLight;
import lighting.PointLight;
import lighting.SpotLight;
import org.junit.jupiter.api.Test;
import primitives.Color;
import primitives.Double3;
import primitives.Material;
import primitives.Point;
import primitives.Vector;
import scene.Scene;

/**
 * Creates images that demonstrates all the feature till stage 8
 * @author Dvir Farkash
 */
public class Stage8Images {
    /**
     * The scene object containing all configurations and components
     */
    private final Scene scene = new Scene("Wizard's Laboratory Table");
    
    /**
     * The resolution of the images
     */
    private static final int RESOLUTION = 1000;
    
    /**
     * Produces a custom 3D rendered scene illustrating advanced transparency,
     * reflection, complex multi-shadowing, and diverse material properties.
     */
    @Test
    public void testCustomWizardScene() {
        // ----------------- Camera Setup (Builder Pattern) -----------------
        Camera camera = Camera.getBuilder()
                              .setLocation(new Point(0, 100, 1000))
                              .setDirection(new Point(0, 0, 0))
                              .setVpSize(220, 220)
                              .setVpDistance(Math.sqrt(100 * 100 + 1000 * 1000))
                              .setResolution(RESOLUTION, RESOLUTION)
                              .setRayTracer(scene, RayTracerType.SIMPLE)
                              .build();
        
        // ----------------- Scene Background and Ambient Light -----------------
        scene.setBackground(new Color(10, 15, 25)); // Deep mystical dark-blue background
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
        // 1. Main Spotlight from top-left pointing to the center to cast clear shadows
        scene.lights.add(new SpotLight(new Color(189, 294, 612), new Point(-100, 120, 100),
                new Vector(1, -1.2, -1.5)).setKl(0.0001)
                                          .setKq(0.00001));
        
        // 2. Point Light on the right side to add secondary softer cross-shadowing
        scene.lights.add(new PointLight(new Color(750, 735, 276), new Point(100, 80, 50)).setKl(0.0002).setKq(0.0002));
        
        // ----------------- Execute Rendering and Image Compilation -----------------
        camera.renderImage().writeToImage("Stage8Image");
    }
}

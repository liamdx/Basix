package TestGame;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glViewport;
import java.util.Random;

import EngineCore.*;
import TestGame.Renderer;
import com.sun.javafx.image.impl.BaseIntToByteConverter;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class TestGameLogic implements IBasixLogic {
    // Control variables
    private static final float CAMERA_POS_STEP = 0.13f;
    private static final float MOUSE_SENSITIVITY = 0.25f;

    private final Renderer renderer;

    private BItem[] items;

    private BCamera camera;

    private PointLight pointLight;

    private DirectionalLight dirLight;

    private SpotLight spotLight;

    private Vector3f ambientLight;
    private Vector3f cameraInc;
    private float dirLightAngle;

    Random rand;

    public TestGameLogic() {
        renderer = new Renderer();
        camera = new BCamera();
        cameraInc = new Vector3f();

    }
    
    @Override
    public void init(BasixWindow window) throws Exception {
        rand = new Random();
        glDisable(GL_CULL_FACE);

        renderer.init(window);
        BTexture houseTex = new BTexture("/resources/farmhouse_tex.png");
        //BMaterial houseMat = new BMaterial(houseTex, 0.0f);
        BMaterial houseMat = new BMaterial(new Vector4f(1.0f,1.0f,1.0f,1.0f), 1.0f);

        BMesh mesh = OBJLoader.loadMesh("/resources/bg3_obj.obj");
        mesh.setMaterial(houseMat);
        BItem farm = new BItem(mesh);
        farm.setPosition(0, -5f,0f);
        farm.setScale(0.01f);
        items = new BItem[]{farm};

        ambientLight = new Vector3f(0.3f,0.3f,0.3f);
        Vector3f lightColour = new Vector3f(1.0f, 1.0f,  1.0f);
        Vector3f lightPosition = new Vector3f(5,-5, -10);
        float lightIntensity = 1.7f;
        pointLight = new PointLight(lightColour,lightPosition,lightIntensity);
        PointLight.Attenuation att = new PointLight.Attenuation(0,0,1.0f);
        pointLight.setAttenuation(att);
        dirLight = new DirectionalLight(new Vector3f(1,1,1), new Vector3f(0,0,0), 1.3f);

        //is spot light working?
        Vector3f spotLightDir = new Vector3f(0,0,0);
        Vector3f spotLightColour = new Vector3f(1,0,0);
        spotLight = new SpotLight(spotLightColour,spotLightDir, new Vector3f(0,0,1),180f,5.0f);
    }
    
    @Override
    public void input(BasixWindow window, MouseInput mouseInput) {

        cameraInc.set(0, 0, 0);
        if (window.isKeyPressed(GLFW_KEY_W)) {
            cameraInc.z = -1;
        } else if (window.isKeyPressed(GLFW_KEY_S)) {
            cameraInc.z = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_A)) {
            cameraInc.x = -1;
        } else if (window.isKeyPressed(GLFW_KEY_D)) {
            cameraInc.x = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_Z)) {
            cameraInc.y = -1;
        } else if (window.isKeyPressed(GLFW_KEY_X)) {
            cameraInc.y = 1;
        }

    }

    @Override
    public void update(float interval, MouseInput mouseInput) {

        // Update camera position
        camera.movePosition(cameraInc.x * CAMERA_POS_STEP,
                cameraInc.y * CAMERA_POS_STEP,
                cameraInc.z * CAMERA_POS_STEP);

        // Update camera based on mouse
        if (mouseInput.isRightButtonPressed()) {
            Vector2f rotVec = mouseInput.getDisplVec();
            camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
        }

        float newIntensity = pointLight.getIntensity() + 0.01f;
        if (newIntensity > 1.4f){
            newIntensity = 0f;
        }
        pointLight.setIntensity(newIntensity);
        Vector3f newColour = pointLight.getColour();
        newColour.x += rand.nextFloat() *(0.3 - (-0.3)) - 0.3;
        newColour.y += rand.nextFloat() *(0.3 - (-0.3)) - 0.3;
        newColour.z += rand.nextFloat() *(0.3 - (-0.3)) - 0.3;
        pointLight.setColour(newColour);

        dirLightAngle += 1.1f;
        if(dirLightAngle > 90){
            dirLight.setIntensity(0.0f);
            if(dirLightAngle >= 360f){
                dirLightAngle = -90f;
            }
        }
        else if(dirLightAngle <= -80f || dirLightAngle >= 80f){
            float factor = 1 - (float)(Math.abs(dirLightAngle) - 80) / 10.0f;
            dirLight.setIntensity(factor);
            dirLight.getColour().y = Math.max(factor, 0.9f) ;
            dirLight.getColour().z = Math.max(factor, 0.5f);
        }
        else{
            dirLight.setIntensity(1);
            dirLight.getColour().x = 1;
            dirLight.getColour().y = 1;
            dirLight.getColour().z = 1;
        }

        double angRad = Math.toRadians(dirLightAngle);
        dirLight.getDirection().x = (float) Math.sin(angRad);
        dirLight.getDirection().y = (float) Math.cos(angRad);
    }

    @Override
    public void render(BasixWindow window) {
        renderer.render(window, items, camera, ambientLight, pointLight, spotLight, dirLight);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
    }
}

package TestGame;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glViewport;

import EngineCore.*;
import TestGame.Renderer;
import com.sun.javafx.image.impl.BaseIntToByteConverter;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class TestGameLogic implements IBasixLogic {

    private static final float CAMERA_POS_STEP = 0.09f;

    private static final float MOUSE_SENSITIVITY = 0.25f;

    private final Renderer renderer;

    private BMesh mesh;

    private BItem[] items;

    private BCamera camera;

    private Vector3f cameraInc;

    public TestGameLogic() {
        renderer = new Renderer();
        camera = new BCamera();
        cameraInc = new Vector3f();

    }
    
    @Override
    public void init(BasixWindow window) throws Exception {

        glDisable(GL_CULL_FACE);

        renderer.init(window);

        BTexture houseTex = new BTexture("/resources/farmhouse_tex.png");
        BMesh mesh = OBJLoader.loadMesh("/resources/fh2.obj");
        mesh.setTexture(houseTex);
        BItem farm = new BItem(mesh);
        farm.setPosition(0,-5f,0f);
        farm.setScale(0.1f);
        items = new BItem[]{farm};
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

    }

    @Override
    public void render(BasixWindow window) {
        renderer.render(window, items, camera);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        mesh.cleanUp();
    }
}

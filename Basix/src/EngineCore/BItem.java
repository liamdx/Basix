package EngineCore;


import org.joml.Vector3f;

public class BItem {

    private final BMesh mesh;

    private final Vector3f position;

    private float scale;

    private final Vector3f rotation;

    public BItem(BMesh mesh) {
        this.mesh = mesh;
        position = new Vector3f(0, 0, 0);
        scale = 1f;
        rotation = new Vector3f(0, 0, 0);
    }

        public Vector3f getPosition(){
            return position;
        }

        public void setPosition(float x, float y, float z){
            this.position.x = x;
            this.position.y = y;
            this.position.z = z;
        }

        public float getScale(){
            return scale;
        }

        public void setScale(float scale){
            scale = scale;
        }

        public Vector3f getRotation(){
            return rotation;
        }

        public void setRotation(float x, float y, float z){
            this.rotation.x = x;
            this.rotation.y = y;
            this.rotation.z = z;
        }

        public BMesh getMesh(){
            return mesh;
        }
    }


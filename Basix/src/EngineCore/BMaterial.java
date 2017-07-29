package EngineCore;

import org.joml.Vector4f;

/**
 * Created by Liam on 7/22/2017.
 */
public class BMaterial {

    private static final Vector4f DEFAULT_COLOUR = new Vector4f(1.0f,1.0f,1.0f,1.0f);

    private Vector4f ambientColour;

    private Vector4f diffuseColour;

    private Vector4f specularColour;

    private float reflectance;

    private BTexture tex;



    public Vector4f getAmbientColour() {
        return ambientColour;
    }

    public void setAmbientColour(Vector4f ambientColour) {
        this.ambientColour = ambientColour;
    }

    public Vector4f getDiffuseColour() {
        return diffuseColour;
    }

    public void setDiffuseColour(Vector4f diffuseColour) {
        this.diffuseColour = diffuseColour;
    }

    public Vector4f getSpecularColour() {
        return specularColour;
    }

    public void setSpecularColour(Vector4f specularColour) {
        this.specularColour = specularColour;
    }

    public float getReflectance() {
        return reflectance;
    }

    public void setReflectance(float reflectance) {
        this.reflectance = reflectance;
    }

    public BTexture getTex() {
        return tex;
    }

    public void setTex(BTexture tex) {
        this.tex = tex;
    }

    public boolean isTextured(){
        if(tex == null){
            return false;
        }
        else{
            return true;
        }
    }

    public BMaterial()
    {
        this.ambientColour = DEFAULT_COLOUR;
        this.diffuseColour = DEFAULT_COLOUR;
        this.specularColour = DEFAULT_COLOUR;
        this.tex = null;
        this.reflectance = 0f;
    }

    public BMaterial(Vector4f colour, float reflectance){
        this(colour,colour,colour, null, reflectance);
    }

    public BMaterial(BTexture tex){
        this(DEFAULT_COLOUR,DEFAULT_COLOUR,DEFAULT_COLOUR, tex, 0);
    }

    public BMaterial(BTexture tex, float reflectance){
        this(DEFAULT_COLOUR, DEFAULT_COLOUR, DEFAULT_COLOUR, tex, reflectance);
    }

    public BMaterial(Vector4f ambientColour, Vector4f diffuseColour, Vector4f specularColour,BTexture tex, float reflectance){
        this.ambientColour = ambientColour;
        this.diffuseColour = diffuseColour;
        this.specularColour = specularColour;
        this.tex = tex;
        this.reflectance = reflectance;
    }


}

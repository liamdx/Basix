package EngineCore;

import org.joml.Vector3f;

/**
 * Created by Liam on 7/22/2017.
 */
public class PointLight {

    private Vector3f colour;

    private Vector3f position;

    protected float intensity;

    private Attenuation attenuation;

    public PointLight(Vector3f colour, Vector3f position, float intensity){
        attenuation = new Attenuation(1,0,0);
        this.colour = colour;
        this.position = position;
        this.intensity = intensity;
    }

    public PointLight(Vector3f colour, Vector3f position, float intensity, Attenuation attenuation){
        this(colour,position,intensity);
        this.attenuation = attenuation;
    }

    public PointLight(PointLight pointLight){
        this(new Vector3f(pointLight.getColour()), new Vector3f(pointLight.getPosition()), pointLight.getIntensity(),pointLight.getAttenuation());
    }

    public static class Attenuation{

        private float constant;

        private float linear;

        private float exponent;

        public Attenuation(float constant, float linear, float exponent){
            this.constant = constant;
            this.linear = linear;
            this.exponent = exponent;
        }

        public float getConstant() {
            return constant;
        }

        public void setConstant(float constant) {
            this.constant = constant;
        }

        public float getLinear() {
            return linear;
        }

        public void setLinear(float linear) {
            this.linear = linear;
        }

        public float getExponent() {
            return exponent;
        }

        public void setExponent(float exponent) {
            this.exponent = exponent;
        }
    }

    public Vector3f getColour() {
        return colour;
    }

    public void setColour(Vector3f colour) {
        this.colour = colour;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public float getIntensity() {
        return intensity;
    }

    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }

    public Attenuation getAttenuation() {
        return attenuation;
    }

    public void setAttenuation(Attenuation attenuation) {
        this.attenuation = attenuation;
    }
}

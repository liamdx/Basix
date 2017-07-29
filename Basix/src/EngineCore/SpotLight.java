package EngineCore;

import org.joml.Vector3f;

/**
 * Created by Liam on 7/27/2017.
 */
public class SpotLight {
    private Vector3f colour;

    private Vector3f position;

    private Vector3f coneDirection;

    protected float intensity;

    protected float coneAngle;

    private SpotLight.Attenuation attenuation;

    public Vector3f getConeDirection() {
        return coneDirection;
    }

    public void setConeDirection(Vector3f coneDirection) {
        this.coneDirection = coneDirection;
    }

    public float getConeAngle() {
        return coneAngle;
    }

    public void setConeAngle(float coneAngle) {
        this.coneAngle = coneAngle;
    }

    public SpotLight(Vector3f colour, Vector3f position, Vector3f direction, float angle, float intensity){
        attenuation = new SpotLight.Attenuation(1,0,0);
        this.colour = colour;
        this.position = position;
        this.coneDirection = direction;
        this.coneAngle = angle;
        this.intensity = intensity;
    }

    public SpotLight(Vector3f colour, Vector3f position, Vector3f diredtion, float angle, float intensity, SpotLight.Attenuation attenuation){
        this(colour,position, diredtion, angle, intensity);
        this.attenuation = attenuation;
    }

    public SpotLight(SpotLight spotLight){
        this(new Vector3f(spotLight.getColour()), new Vector3f(spotLight.getPosition()), new Vector3f(spotLight.getConeDirection()), spotLight.getConeAngle(),spotLight.getIntensity(),spotLight.getAttenuation());
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

    public SpotLight.Attenuation getAttenuation() {
        return attenuation;
    }

    public void setAttenuation(SpotLight.Attenuation attenuation) {
        this.attenuation = attenuation;
    }
}

#version 330

in vec2 outTexCoord;
in vec3 mvVertexNormal;
in vec3 mvVertexPos;
out vec4 fragColor;

uniform sampler2D texture_sampler;
uniform vec3 ambientLight;
uniform float specularPower;


struct DirectionalLight
{
    vec3 colour;
    vec3 direction;
    float intensity;
};

uniform DirectionalLight dirLight;

struct Attenuation
{
    float constant;
    float linear;
    float exponent;
};


struct PointLight
{
    vec3 colour;
    //position in view co-ordinates
    vec3 position;
    float intensity;
    Attenuation att;
};

uniform PointLight pointLight;

struct SpotLight
{
    vec3 colour;
    vec3 position;
    vec3 direction;
    float angle;
    float intensity;
    Attenuation att;
};

uniform SpotLight spotLight;

struct Material
{
    vec4 ambient;
    vec4 diffuse;
    vec4 specular;
    int hasTexture;
    float reflectance;
};

uniform Material mat;


vec4 ambientC;
vec4 diffuseC;
vec4 specularC;

void setupColours(Material mat, vec2 texCoords)
{
    if(mat.hasTexture == 1)
    {
        ambientC = texture(texture_sampler, outTexCoord);
        diffuseC = ambientC;
        specularC = ambientC;
    }
    else
    {
        ambientC = mat.ambient;
        diffuseC = mat.diffuse;
        specularC = mat.specular;
    }
}

vec4 calcLightColour(vec3 lightColour, float lightIntensity, vec3 position, vec3 toLightDir, vec3 normal)
{
    vec4 diffuseColour = vec4(0,0,0,0);
    vec4 specColour = vec4(0,0,0,0);

    //diffuse
    float diffuseFactor = max(dot(normal, toLightDir), 0.0);
    diffuseColour = diffuseC * vec4(lightColour, 1.0) * lightIntensity * diffuseFactor;

    //specular
    vec3 cameraDirection = normalize(-position);
    vec3 fromLightDir = -toLightDir;
    vec3 reflectedLight = normalize(reflect(fromLightDir, normal));
    float specularFactor = max(dot(cameraDirection, reflectedLight), 0.0);
    specularFactor = pow(specularFactor, specularPower);
    specColour = specularC * lightIntensity * specularFactor * mat.reflectance * vec4(lightColour, 1.0);

    return (diffuseColour + specColour);
}

vec4 calcPointLight(PointLight light, vec3 position, vec3 normal)
{
    vec3 lightDirection = light.position - position;
    vec3 toLightDir = normalize(lightDirection);
    vec4 lightColour = calcLightColour(light.colour, light.intensity, position, toLightDir, normal);

    //apply attenuation
    float distance = length(lightDirection);
    float attenuationInv = light.att.constant + light.att.linear * distance + light.att.exponent * distance * distance;

    return lightColour / attenuationInv;
}

vec4 calcSpotLight(SpotLight light, vec3 position, vec3 normal)
{
    vec3 lightDirection = light.position - position;
    vec3 toLightDir = normalize(lightDirection);
    vec3 fromLightDir = -toLightDir;
    float spot_alpha = dot(fromLightDir, normalize(light.direction));

    vec4 lightColour = vec4(0,0,0,0);

    if(spot_alpha > light.angle)
    {
        lightColour = calcLightColour(light.colour, light.intensity, position, toLightDir, normal);
        lightColour *= (1.0 - (1.0 - spot_alpha)/(1.0 - light.angle));
        //apply attenuation
        float distance = length(lightDirection);
        float attenuationInv = light.att.constant + light.att.linear * distance + light.att.exponent * distance * distance;

    }
    return lightColour;
}

vec4 calcDirectionalLight(DirectionalLight light, vec3 position, vec3 normal)
{
    return calcLightColour(light.colour, light.intensity, position, normalize(light.direction), normal);
}

void main ()
{
    setupColours(mat, outTexCoord);
    vec4 diffuseSpecularComp = calcDirectionalLight(dirLight, mvVertexPos, mvVertexNormal);
    diffuseSpecularComp += calcPointLight(pointLight, mvVertexPos, mvVertexNormal);
    diffuseSpecularComp += calcSpotLight(spotLight, mvVertexPos, mvVertexNormal);
    fragColor = ambientC * vec4(ambientLight, 1) + diffuseSpecularComp;
}
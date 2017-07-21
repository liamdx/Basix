package EngineCore;

import EngineCore.BasixWindow;


public interface IBasixLogic {
	
	void init(BasixWindow window) throws Exception;

    void input(BasixWindow window, MouseInput mouseInput);

    void update(float interval, MouseInput mouseInput);
    
    void render(BasixWindow window);
	
    void cleanup();
}

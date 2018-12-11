package support;

import com.pi4j.component.temperature.TemperatureSensor;
import com.pi4j.io.w1.W1Master;
import com.pi4j.temperature.TemperatureScale;

public class TempSensor {
	public double getTemp(){
		W1Master w1Master = new W1Master();

        System.out.println(w1Master);

        for (TemperatureSensor device : w1Master.getDevices(TemperatureSensor.class)) {
            if(device.getName().contains("28-05168044b8ff"))
            	return device.getTemperature(TemperatureScale.CELSIUS);
        	//return 32;
        }
		return 0;
	}
	
}

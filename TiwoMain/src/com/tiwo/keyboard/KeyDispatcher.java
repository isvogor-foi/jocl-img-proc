package com.tiwo.keyboard;

import java.awt.KeyEventDispatcher;
import java.awt.event.KeyEvent;

import com.tiwo.communication.Commands.MOVEMENT;
import com.tiwo.communication.Serial;

// custom key dispatcher
public class KeyDispatcher implements KeyEventDispatcher {
    public boolean dispatchKeyEvent(KeyEvent e) {
        if(e.getID() == KeyEvent.KEY_RELEASED)
	    	System.out.println(e.getExtendedKeyCode());
        	try{
	        	switch(e.getExtendedKeyCode()){
	        		case 38: // up key
	        			Serial.getInstance().sendMessage(MOVEMENT.FORWARD.toString()); break;
	        		case 40: // down key
	        			Serial.getInstance().sendMessage(MOVEMENT.BACKWARD.toString()); break;
	        		case 37: // left key
	        			Serial.getInstance().sendMessage(MOVEMENT.LEFT.toString()); break;
	        		case 39: // right key
	        			Serial.getInstance().sendMessage(MOVEMENT.RIGHT.toString()); break;
	        		case 32: // space key
	        			Serial.getInstance().sendMessage(MOVEMENT.STOP.toString()); break; 
	        	}
        	} catch (Exception exception){
        		exception.printStackTrace();
        	}
        //Allow the event to be redispatched
        return false;
    }
}
package org.elliotnash.razer;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Test implements NativeKeyListener {

    private static RazerController controller;

    public static void main(String[] args) {







        // key listeners
        try {
            Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
            logger.setLevel(Level.OFF);
            GlobalScreen.registerNativeHook();
            // Don't forget to disable the parent handlers.
            logger.setUseParentHandlers(false);
        } catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());
            System.exit(1);
        }
        GlobalScreen.addNativeKeyListener(new Test());

        controller = new RazerController();
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent arg0) {
        String text = NativeKeyEvent.getKeyText(arg0.getKeyCode());

        try {
            int i = Integer.parseInt(text);
            if (i > 0) {
                controller.activeSlot = i;
                controller.draw();
            }
        } catch (NumberFormatException ignored) {
        }

    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent arg0) {
        // TODO Auto-generated method stub

    }
}

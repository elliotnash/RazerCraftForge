package org.elliotnash.razer;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.freedesktop.dbus.connections.impl.DBusConnection;
import org.freedesktop.dbus.connections.impl.DBusConnection.DBusBusType;
import org.freedesktop.dbus.exceptions.DBusException;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import org.elliotnash.razer.device.DeviceManager;
import org.elliotnash.razer.lighting.LightingManager;

public class RazerController {

  public DBusConnection connection = null;
  public LightingManager lightingManager;
  public Integer lastNum = null;

  RazerController() {
    try {
      connection = DBusConnection.getConnection(DBusBusType.SESSION);
    } catch (DBusException e) {
      e.printStackTrace();
    }

    DeviceManager deviceManager = new DeviceManager(connection);

    lightingManager = new LightingManager(connection, deviceManager.getDevices().get(0));

    draw();

  }

  public void draw() {
    int[][][] matrix = lightingManager.newMatrix();

    for (int i = 0; i < matrix.length; i++) {
      for (int j = 0; j < matrix[i].length; j++) {
        // testMatrix[i][j] = new int[] { 0, 255, 0 };
      }
    }

    // colours
    int[] red = new int[] { 255, 0, 0 };
    int[] white = new int[] { 255, 255, 255 };
    int[] blue = new int[] {255, 0, 255};
    int[] purple = new int[] {255, 0, 255};


    // wasd
    matrix[2][3] = red;
    matrix[3][2] = red;
    matrix[3][3] = red;
    matrix[3][4] = red;

    //hotbar
    for (int i = 2; i < 11; i++) {
      matrix[1][i] = blue;
    }

    // active item
    if (lastNum != null) {
      // then we draw last pressed key
      matrix[1][lastNum+1] = white;
    }

    lightingManager.drawMatrix(matrix);
  }

}

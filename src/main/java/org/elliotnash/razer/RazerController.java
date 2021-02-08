package org.elliotnash.razer;

import org.elliotnash.razer.lighting.Color;
import org.freedesktop.dbus.connections.impl.DBusConnection;
import org.freedesktop.dbus.connections.impl.DBusConnection.DBusBusType;
import org.freedesktop.dbus.exceptions.DBusException;

import org.elliotnash.razer.device.DeviceManager;
import org.elliotnash.razer.lighting.LightingManager;

import java.util.ArrayList;

public class RazerController {

  public DBusConnection connection = null;
  public LightingManager lightingManager;
  public Integer activeSlot = null;
  public ArrayList<Integer> filledSlots = new ArrayList<>(9);

  public RazerController() {
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
    Color[][] matrix = lightingManager.newMatrix();

    for (int i = 0; i < matrix.length; i++) {
      for (int j = 0; j < matrix[i].length; j++) {
        // matrix[i][j] = new int[] { 15, 50, 0 };
      }
    }

    // colours
    Color red = new Color(108, 41, 242);
    Color white = new Color(255, 255, 255);
    Color blue = new Color(0, 0, 255);
    Color purple = new Color(94, 1, 74);


    // wasd
    matrix[2][3] = red;
    matrix[3][2] = red;
    matrix[3][3] = red;
    matrix[3][4] = red;
    //ctrl+shift
    matrix[4][1] = purple;
    matrix[5][1] = purple;

    //hotbar
    for (int i = 2; i < 11; i++) {
      matrix[1][i] = blue;
    }

    //filled hotbar slots
    for (int slot: filledSlots){
      matrix[1][slot+2] = red;
    }

    // active item
    if (activeSlot != null) {
      // then we draw last pressed key
      matrix[1][activeSlot +2] = white;
    }

    lightingManager.drawMatrix(matrix);
  }

}

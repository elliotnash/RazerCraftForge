package org.elliotnash.razer.lighting;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.freedesktop.dbus.annotations.DBusInterfaceName;
import org.freedesktop.dbus.connections.impl.DBusConnection;
import org.freedesktop.dbus.exceptions.DBusException;
import org.freedesktop.dbus.interfaces.DBusInterface;

public class LightingManager {

  private LightingDBus lightingBus = null;
  private miscBus miscBus = null;

  private int rows = 0;
  private int columns = 0;

  public LightingManager(DBusConnection connection, String serial) {
    try {
      lightingBus = connection.getRemoteObject("org.razer", "/org/razer/device/" + serial, LightingDBus.class);
      miscBus = connection.getRemoteObject("org.razer", "/org/razer/device/" + serial, miscBus.class);
    } catch (DBusException e) {
      e.printStackTrace();
    }

    // Set matrix dimensions
    List<Integer> dimensions = miscBus.getMatrixDimensions();
    rows = dimensions.get(0);
    columns = dimensions.get(1);

  }

  public int[][][] newMatrix() {
    return new int[rows][columns][3];
  }

  public void drawMatrix(int[][][] matrix) {
    if (matrix.length != rows || matrix[rows - 1].length != columns || matrix[rows - 1][columns - 1].length != 3) {
      System.out.println("Invalid matrix!!");
      return;
    }

    try {
      lightingBus.setKeyRow(toByteArr(matrix));
    } catch (IOException e) {
      e.printStackTrace();
    }

    lightingBus.setCustom();

  }

  public void setBreathRandom() {
    lightingBus.setBreathRandom();
  }

  // AHhahah
  private byte[] toByteArr(int[][][] matrix) throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    DataOutputStream dout = new DataOutputStream(out);
    for (int i = 0; i < matrix.length; i++) {
      dout.writeByte(i);
      dout.writeByte(0);
      dout.writeByte(21);
      for (int j = 0; j < matrix[i].length; j++) {
        for (int k = 0; k < matrix[i][j].length; k++) {
          dout.writeByte(matrix[i][j][k]);
        }
      }
    }
    return out.toByteArray();
  }

}

@DBusInterfaceName("razer.device.lighting.chroma")
interface LightingDBus extends DBusInterface {

  public void setKeyRow(byte[] byteArr);

  public void setCustom();

  public void setBreathRandom();

}

@DBusInterfaceName("razer.device.misc")
interface miscBus extends DBusInterface {

  public List<Integer> getMatrixDimensions();

}

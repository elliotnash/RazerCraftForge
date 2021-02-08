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

  public Color[][] newMatrix() {
    return new Color[rows][columns];
  }

  public void drawMatrix(Color[][] matrix) {
    if (matrix.length != rows || matrix[rows - 1].length != columns) {
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
  private byte[] toByteArr(Color[][] matrix) throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    DataOutputStream dout = new DataOutputStream(out);
    for (int i = 0; i < matrix.length; i++) {
      dout.writeByte(i);
      dout.writeByte(0);
      dout.writeByte(21);
      for (int j = 0; j < matrix[i].length; j++) {
        if (matrix[i][j] != null) {
          dout.writeByte(matrix[i][j].red);
          dout.writeByte(matrix[i][j].green);
          dout.writeByte(matrix[i][j].blue);
        } else {
          dout.writeByte((byte)0);
          dout.writeByte((byte)0);
          dout.writeByte((byte)0);
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

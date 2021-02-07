package org.elliotnash.razer.device;

import java.util.List;

import org.freedesktop.dbus.annotations.DBusInterfaceName;
import org.freedesktop.dbus.connections.impl.DBusConnection;
import org.freedesktop.dbus.exceptions.DBusException;
import org.freedesktop.dbus.interfaces.DBusInterface;

public class DeviceManager {

  DevicesDBus devicesBus = null;

  public DeviceManager(DBusConnection connection) {
    try {
      devicesBus = connection.getRemoteObject("org.razer", "/org/razer", DevicesDBus.class);
    } catch (DBusException e) {
      e.printStackTrace();
    }
  }

  public List<String> getDevices() {
    return devicesBus.getDevices();
  }

}

@DBusInterfaceName("razer.devices")
interface DevicesDBus extends DBusInterface {
  public List<String> getDevices();
}

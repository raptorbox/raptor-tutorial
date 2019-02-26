package createnet.raptorbox.quickstart.client;

import java.io.IOException;

import org.createnet.raptor.models.objects.Device;
import org.createnet.raptor.models.objects.RecordSet;
import org.createnet.raptor.models.objects.Stream;
import org.createnet.raptor.models.payload.DevicePayload;
import org.createnet.raptor.sdk.Raptor;
import org.createnet.raptor.sdk.events.callback.DataCallback;
import org.createnet.raptor.sdk.events.callback.DeviceCallback;

import createnet.raptorbox.quickstart.Utils.Raptorbox;

/**
 * Hello world!
 *
 */
public class Client {

	public static void main(String[] args) throws IOException {
		new Client().run();
	}

	public void run() throws IOException, IOException {

		Raptor raptor = Raptorbox.getRaptor();

		String deviceId = "====device id====";

		Device dev = raptor.Inventory().load(deviceId);
		// System.out.println(dev.toJSON().toString());

		raptor.Inventory().subscribe(dev, new DeviceCallback() {
			@Override
			public void callback(Device obj, DevicePayload message) {
				System.out.println("Device data received: " + message.toString());
			}
		});

		raptor.Inventory().subscribe(dev, new DataCallback() {
			@Override
			public void callback(Stream stream, RecordSet record) {
				System.out.println("Device data received: " + record.toJson());
			}
		});

	}
}
